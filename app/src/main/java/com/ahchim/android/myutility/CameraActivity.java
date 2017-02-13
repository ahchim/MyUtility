package com.ahchim.android.myutility;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class CameraActivity extends AppCompatActivity {
    private final int REQ_PERMISSION = 100; // 권한 요청코드
    private final int REQ_CAMERA = 101;     // 카메라 요청코드
    private final int REQ_GALLERY = 102;    // 갤러리 요청코드

    private Button btnCamera;
    private Button btnGallery;
    private ImageView imageView;

    // 사진촬영 후 임시로 저장할 파일 공간
    private Uri fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // 1. 위젯을 세팅
        setWidget();
        // 2. 버튼관련 컨트롤러 활성화처리
        buttonDisable();
        // 3. 리스너 계열을 등록
        setListener();
        checkPermission();
    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 프로그램 실행
            if(PermissionControl.checkPermission(this, REQ_PERMISSION)){
                init();
            }
        } else {
            // 프로그램 실행
            init();
        }
    }

    // 버튼 비활성화하기
    private void buttonDisable(){
        btnCamera.setEnabled(false);
    }
    // 버튼 활성화하기
    private void buttonEnable(){
        btnCamera.setEnabled(true);
    }

    private void init(){
        // 권한처리가 통과되었을때만 버튼을 활성화시켜준다.
        buttonEnable();
    }


    private void setWidget() {
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallary);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    
    private void setListener(){
        btnCamera.setOnClickListener(clickListener);
        btnGallery.setOnClickListener(clickListener);
    }
    // 리스너 정의
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch(v.getId()){
                case R.id.btnCamera: // 카메라 버튼 동작
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 누가 버전부터 기본 Action Image Capture로는 처리 안됨

                    // 마시멜로까지만 됩니다.
//                    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//                    Uri output = Uri.fromFile(file);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

                    // 모든 버전 지원합니다.
                    // 카메라 촬영 후 미디어 컨텐트 uri를 생성해서 외부저장소에 저장함
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // contentvalue 안에 들어가는 값은 콘텐트 갯수 할당이다
                        // 저장할 미디어 속성을 정의하는 클래스
                        ContentValues values = new ContentValues(1);
                        // 속성중에 파일의 종류를 정의
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                        // 전역변수로 정의하는 fileUri에 외부저장소 컨텐츠가 있는 Uri를 임시로 생성해서 넣어준다.
                        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        // 위에서 생성한 fileUri를 사진저장공간으로 사용하겠다고 설정
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        // Uri에 읽기와 쓰기 권한을 시스템에 요청
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    // --- 여기까지 컨텐트 uri 강제세팅 ---

                    startActivityForResult(intent, REQ_CAMERA);
                    break;
                case R.id.btnGallary:  // 갤러리에서 이미지 불러오기
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기 위한 필터링
                    // createChooser 함수로 intent의 제목을 지정할 수 있다.
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REQ_CAMERA:
                // data != null && data.getData() != null 대신 resultCode == RESULT_OK
                if(requestCode == REQ_CAMERA && resultCode == RESULT_OK){  // 사진 확인처리됨 RESULT_OK == -1
                    // 마시멜로 체크
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        fileUri = data.getData();
                    }

                    if(fileUri != null){
                        Glide.with(this).load(fileUri).into(imageView);
                        //imageView.setImageURI(fileUri);
                    } else {
                        Toast.makeText(this, "사진파일이 없습니다", Toast.LENGTH_SHORT).show();
                    }

                } else{
                    // resultCode가 0이고 사진이 찍혔으면 URI가 남는데
                    // uri가 있을 경우 삭제처리...

                }
                break;
            case REQ_GALLERY:
                if (resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    Glide.with(this).load(fileUri).into(imageView);
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 되면
        if(requestCode == REQ_PERMISSION){
            // 2.2 프로그램 실행
            if(PermissionControl.onCheckResult(grantResult)){
                init();
            } else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
