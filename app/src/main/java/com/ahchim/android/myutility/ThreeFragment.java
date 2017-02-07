package com.ahchim.android.myutility;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {
    private View view = null;
    private WebView webView;


    public ThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            return view;
        } else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_three, container, false);

            // 1. 사용할 위젯을 가져온다.
            webView = (WebView) view.findViewById(R.id.webView);

            // js 필수로 설정해야함!! js없는 웹은 상상할 수 엄쒀
            webView.getSettings().setJavaScriptEnabled(true);
            // zoom 사용설정
            //webView.getSettings().setSupportZoom(true);  // 이거 동작 안함;;
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);

            // 3. 웹뷰 클라이언트를 지정... (안하면 내장 웹브라우저가 팝업된다.)
            webView.setWebViewClient(new WebViewClient());
            // 3.1 https 등을 처리하기 위한 핸들러 : 프로토콜에 따라 클라이언트가 선택되는것으로 파악됨...
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }
            });

            // 최초 접속시 구글접속
            webView.loadUrl("https://www.google.co.kr");

            return view;
        }
    }
}
