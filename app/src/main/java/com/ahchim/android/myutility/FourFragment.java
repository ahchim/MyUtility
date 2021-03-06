package com.ahchim.android.myutility;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends Fragment implements OnMapReadyCallback {
    // 나는 구글맵을 쓰겠다!
    //private MapView mapView;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private MainActivity activity;
    private LocationManager manager;
    private View view = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        manager = activity.getLocationManager();
    }

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapView.getMapAsync(this); // 콜백인자에 나를 넘겨준다. impliments에 구현되어 있다.
        // 리스너 등록
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                3000, // 통지사이의 최소 시간간격 (milliSecond) // 업데이트 간격
                10,   // 통지사이의 최소 변경거리 (m)
                locationListener);
        // 정보제공자로 네트워크 프로바이더 등록
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,  // 등록할 위치제공자
                3000, // 통지사이의 최소 시간간격 (milliSecond) // 업데이트 간격
                10,   // 통지사이의 최소 변경거리 (m)
                locationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_four, container, false);

        // findViewById가 아니라 ㅠㅠㅠ 굉장히 복잡함.
        // Fragment 에서 mapView를 호출하는 방법
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // 리스너 해제
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        manager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker in Daegi Building and move the camera
//        LatLng seoulSinsaDaegiBuilding = new LatLng(37.515696, 127.021345);
//        this.googleMap.addMarker(new MarkerOptions().position(seoulSinsaDaegiBuilding).title("Marker in Seoul Sinsa Daegi Building"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoulSinsaDaegiBuilding));
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude(); // 경도
            double latitude = location.getLatitude();   // 위도
            double altitude = location.getAltitude();   // 고도
            float accuracy = location.getAccuracy();    // 정확도
            String provider = location.getProvider();   // 위치제공자

            // 내위치
            LatLng myPosition = new LatLng(latitude, longitude);
                                            // 위도,   경도                        마커 타이틀
            googleMap.addMarker(new MarkerOptions().position(myPosition).title("I am here!"));
            // 화면을 내 위치로 이동시키는 함수
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18));
        }

        // 세개 다 사용안함ㅋㅋ
        @Override // provider의 상태변경 시 호출
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override // gps가 사용할 수 없었다가 사용가능해지면 호출
        public void onProviderEnabled(String provider) { }

        @Override // gps가 사용할 수 없을 때 호출
        public void onProviderDisabled(String provider) { }
    };
}
