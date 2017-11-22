package com.navfresh.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String url = "http://www.navfresh.com/";
    //    String[] PERMISSIONS = {Manifest.permission.ACCESS_NETWORK_STATE};
//    private final int PERMISSION_ALL = 11;
    private WebView webView;
    private ProgressBar progressBar;
    private TextView txtInternetConnection;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }*/
        init();
        showWebPage();
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void init() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        txtInternetConnection = findViewById(R.id.txtInternetConnection);
        txtInternetConnection.setOnClickListener(this);
        swipeLayout = findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(this);
    }

    private void showWebPage() {
        if (isDeviceOnline()) {
            txtInternetConnection.setVisibility(View.GONE);
            webView.loadUrl(url);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new NavFreshWebClient());
        } else {
            txtInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        try {
            switch (vId) {
                case R.id.txtInternetConnection:
                    showWebPage();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDeviceOnline() {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeviceOnLine;
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

    private class NavFreshWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
        }
    }
}
