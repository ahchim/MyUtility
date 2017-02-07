package com.ahchim.android.myutility;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment implements View.OnClickListener {
    View view = null;

    Button btnLength, btnArea, btnWeight, btnVolume;
    LinearLayout layoutResult;
    Spinner spnDefaultUnit, spnAfterUnit;
    ArrayList<String> units;
    ArrayAdapter<String> adaptDefault;
    ArrayAdapter<String> adaptAfter;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            return view;
        } else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_two, container, false);

            btnLength = (Button) view.findViewById(R.id.btnLength);
            btnArea = (Button) view.findViewById(R.id.btnArea);
            btnWeight = (Button) view.findViewById(R.id.btnWeight);
            btnVolume = (Button) view.findViewById(R.id.btnVolume);

            layoutResult = (LinearLayout) view.findViewById(R.id.layoutResult);

            btnLength.setOnClickListener(this);
            btnArea.setOnClickListener(this);
            btnWeight.setOnClickListener(this);
            btnVolume.setOnClickListener(this);

            // 3. 스피너
            spnDefaultUnit = (Spinner) view.findViewById(R.id.spnDefaultUnit);
            spnAfterUnit = (Spinner) view.findViewById(R.id.spnAfterUnit);

            return view;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLength:
                spinnerRun("센티미터(cm)#밀리미터(mm)#미터(m)#킬로미터(km)#인치(in)#마일(mile)");
                break;
            case R.id.btnArea:
                spinnerRun("제곱미터(m2)#아르(a)#헥타르(ha)#제곱킬로미터(km2)#평");
                break;
            case R.id.btnWeight:
                spinnerRun("그램(g)#밀리그램(mg)#킬로그램(kg)#톤(t)#파운드(lb)#근");
                break;
            case R.id.btnVolume:
                spinnerRun("시시(cc)#밀리리터(㎖)#데시리터(㎗)#리터(ℓ)#세제곱센티미터(cm3)#세제곱미터(m3)#세제곱인치(in3)");
                break;
        }
    }

    public void spinnerRun(String s){
        // 3.pre 스피너에 들어갈 데이터를 정의
        units = new ArrayList<>(Arrays.asList(s.split("#")));

        // 3.1 스피너 데이터 등록
        adaptDefault = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, units
        );

        adaptAfter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, units
        );

        // 3.2 스피너에 아답터 등록
        spnDefaultUnit.setAdapter(adaptDefault);
        spnAfterUnit.setAdapter(adaptAfter);

        // 3.3 스피너 리스너에 등록
        spnDefaultUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                Toast.makeText(getContext(), "선택된 아이템 = " + units.get(position), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        spnAfterUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                Toast.makeText(getContext(), "선택된 아이템 = " + units.get(position), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });
    }
}
