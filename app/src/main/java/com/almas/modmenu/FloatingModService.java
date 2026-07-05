package com.almas.modmenu;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FloatingModService extends Service {
    private WindowManager windowManager;
    private WebView webView;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.TRANSPARENT);
        
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(), "ModBridge");
        webView.loadUrl("file:///android_asset/menu.html");
        webView.setWebViewClient(new WebViewClient());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : 
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );
        
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 50;
        params.y = 50;

        windowManager.addView(webView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) windowManager.removeView(webView);
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    private class WebAppInterface {
        @JavascriptInterface
        public void toggleFeature(String featureName, boolean isEnabled) {
            String status = isEnabled ? "ҚОСЫЛДЫ" : "ӨШІРІЛДІ";
            Toast.makeText(FloatingModService.this, featureName + " " + status, Toast.LENGTH_SHORT).show();
        }
    }
}
