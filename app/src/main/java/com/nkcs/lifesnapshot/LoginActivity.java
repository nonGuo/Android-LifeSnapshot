package com.nkcs.lifesnapshot;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText text_eamil;
    private EditText text_password;
    private WebView webView;
    private static final String domin = "https://imd-1gm5e8rzbcaec277-1259622528.tcloudbaseapp.com/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text_eamil = findViewById(R.id.editText_email);
        text_password = findViewById(R.id.editText_password);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                finish();
            }
        };
        webView.setWebViewClient(webViewClient);
    }

    public void Login(View v){
        String eamil = text_eamil.getText().toString();
        String password = text_password.getText().toString();
        webView.loadUrl(domin + "login/?eamil=" + eamil + "&" + "pwd=" + password);
    }

    public void Signup(View v){
        String eamil = text_eamil.getText().toString();
        String password = text_password.getText().toString();
        webView.loadUrl(domin + "signup/?eamil=" + eamil + "&" + "pwd=" + password);
    }
}
