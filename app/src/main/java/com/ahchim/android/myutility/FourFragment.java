package com.ahchim.android.myutility;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
        //mapView.getMapAsync(this); // 콜백인자에 나를 넘겨준다. impliments에 구현되어 있다.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        // findViewById가 아니라 ㅠㅠㅠ 굉장히 복잡함.
        // Fragment 에서 mapView를 호출하는 방법
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
