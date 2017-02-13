package com.ahchim.android.myutility;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ahchim.android.myutility.dummy.DummyContent;

/*
 * GPS 센서 사용하기
 * 1. manifest에 FINE, COURSE 권한추가
 * 2. runtime Permission 소스코드에 추가
 * 3. GPS Location Manager 정의
 * 4. GPS가 켜져있는지 확인. 꺼져있다면 GPS 화면으로 이동
 *
 * 5. GPS Location Listener 정의
 * 6. Listener 실행
 * 7. Listener 해제
 */

public class MainActivity extends AppCompatActivity implements FiveFragment.OnListFragmentInteractionListener {

    // 탭 및 페이저 속성 정의
    final int TAB_COUNT = 5;
    // 현재 페이지
    int page_position = 0;
    // 페이지 이동경로를 저장하는 history stack 변수 - 커스텀 스택 클래스
    Stack<Integer> pageStack = new Stack<>();
    boolean backPress = false;

    private OneFragment one;
    private TwoFragment two;
    private ThreeFragment three;
    private FourFragment four;
    private FiveFragment five;

    ViewPager viewPager;

    // 위치정보 관리자
    private LocationManager manager;
    public LocationManager getLocationManager(){
        return manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.startMethodTracing("trace_result");
        if(savedInstanceState == null)
            setContentView(R.layout.activity_main);
        else return;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        } else{
            init();
        }
        Debug.stopMethodTracing();
    }

    private void init(){
        // 프래그먼트 init
        one = new OneFragment();
        two = new TwoFragment();
        three = new ThreeFragment();
        four = new FourFragment();
        five = FiveFragment.newInstance(3);  // 미리 정해진 그리드 가로축 개수
        //four.setActivity(this);

        // 탭 Layout 정의
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        // 구조가 희안함
        // 탭 생성 및 타이틀 입력
        tabLayout.addTab(tabLayout.newTab().setText("계산기"));
        tabLayout.addTab(tabLayout.newTab().setText("단위환산"));
        tabLayout.addTab(tabLayout.newTab().setText("검색엔진"));
        tabLayout.addTab(tabLayout.newTab().setText("내위치"));
        tabLayout.addTab(tabLayout.newTab().setText("앨범"));

        // 프래그먼트 페이저 작성
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 아답터 생성
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 1. 페이저 리스너: 페이저가 변경되었을 때 탭을 바꿔주는 리스너 (add라 리스너 추가가능함)
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 2. 페이지의 변경사항을 체크한다.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 스택배열에 포지션을 저장해 놓는다.
                // 뒤로가기를 했을 때는 스택에 포지션을 쌓으면 안된다.
                // 뒤로가기를 누르지 않았을 때만 stack에 포지션을 더한다.
                if(!backPress){
                    pageStack.push(page_position);
                    // 뒤로가기를 눌렀으면 false로 다시 세팅해준다.
                }else{
                    backPress = false;
                }
                page_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 3. 탭이 변경되었을 때 페이지를 바꿔주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        ///////////////////////////
        // LocationManager 객체를 얻어온다
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPS 센서가 켜져있는지 확인
        // 꺼져있다면 GPS를 켜는 페이지로 이동
        if(!gpsCheck()){
            // - 팝업창 만들기
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // 1. 팝업창 제목
            alertDialog.setTitle("GPS 켜기");
            // 2. 팝업창 메시지
            alertDialog.setMessage("GPS가 꺼져있습니다. \n 설정창으로 이동하시겠습니까?");
            // 3. Yes 버튼 생성
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    //intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            // 4. Cancle 버튼 생성 - 누르면 종료.
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // 5. show 함수로 팝업창에 띄운다
            alertDialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        switch(page_position){
            case 2:
                // 커스텀 goBack함수 : goBack 되면 true, 아니면 false
                if(three.goBack()){ } // 뒤로가기가 가능하면 아무런 동작을 하지 않는다.
                else goBackStack();   // 뒤로가기가 안되면 stack 뒤로가기 로직 호출
                break;
            // 위의 조건에 해당되지 않는 모든 케이스는 아래 로직을 처리한다.
            default:
                goBackStack();
                break;
        }
    }

    // stack 뒤로가기
    private void goBackStack(){
        // stack의 사이즈가 0이면 앱을 종료
        if(pageStack.size() < 1){
            super.onBackPressed();
            // stack에 position 값이 있으면
        }else{
            // View Pager 리스너에서 stack에 더해지는 것을 방지하기 위해 backpress 상태값을 미리 세팅
            backPress = true;
            // 페이지를 stack의 가장 마지막에 있는 위치값으로 이동시키고 stack에서 위치값 제거
            viewPager.setCurrentItem(pageStack.pop());
        }
    }

    // GPS 가 꺼져있는지 체크 - 롤리팝 이하버전...
    private boolean gpsCheck(){
        // 롤리팝 이상버전에서는 LocationManager로 GPS 꺼짐 여부 체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 롤리팝 이하버전에서는 LOCATION_PROVIDERS_ALLOWED로 체크
        }else{
            String gps = android.provider.Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(gps.matches(",*gps.*")){
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0: fragment = one; break;
                case 1: fragment = two; break;
                case 2: fragment = three; break;
                case 3: fragment = four; break;
                case 4: fragment = five; break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }


    private final int REQ_CODE = 100;

    // 1. 권한체크
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        // 1.1 런타임 권한체크
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
           || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
           || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // 1.3 시스템에 권한 요청
            requestPermissions(permArr, REQ_CODE);
        } else{
            init();
        }
    }

    // 2. 권한체크 후 콜백 < 사용자가 확인후 시스템이 호출하는 함수
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 되면
        if(requestCode == REQ_CODE){
            if(grantResult[0] == PackageManager.PERMISSION_GRANTED &&
               grantResult[1] == PackageManager.PERMISSION_GRANTED &&
               grantResult[2] == PackageManager.PERMISSION_GRANTED){
                // 2.2 프로그램 실행
                init();
            } else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다", Toast.LENGTH_SHORT).show();
                //checkPermission();
                finish();
            }
        }
    }
}
