package com.cloudchewie.ingenuity.activity.global;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ui.general.BottomSheet;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.ui.general.ProgressWebView;
import com.cloudchewie.util.system.ClipBoardUtil;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.StatusBarUtil;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    ProgressWebView webView;
    ImageView closeButton;
    ImageView backButton;
    ImageView moreButton;
    TextView titleView;
    RelativeLayout titleLayout;
    String originUrl;
    boolean enabledCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        originUrl = intent.getStringExtra("url");
        if (intent.getStringExtra("enabledCache") == null) {
            enabledCache = SharedPreferenceUtil.getBoolean(this, SharedPreferenceCode.ENABLE_WEB_CACHE.getKey(), true);
        } else {
            enabledCache = Boolean.parseBoolean(intent.getStringExtra("enabledCache"));
        }
        if (originUrl == null) {
            IToast.makeTextBottom(this, "网址解析异常", Toast.LENGTH_SHORT).show();
            finish();
        }
        backButton = findViewById(R.id.activity_webview_back);
        closeButton = findViewById(R.id.activity_webview_close);
        moreButton = findViewById(R.id.activity_webview_more);
        titleView = findViewById(R.id.activity_webview_title);
        webView = findViewById(R.id.activity_webview_webview);
        titleLayout = findViewById(R.id.activity_webview_titlebar);
        backButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        initWebview();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initWebview() {
        webView.setTitleView(titleView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setAppCacheEnabled(enabledCache);
        webView.loadUrl(originUrl);
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        }
        if (v == closeButton) {
            finish();
        } else if (v == moreButton) {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_webview_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.webview_more_refresh).setOnClickListener(v1 -> {
                webView.reload();
                bottomSheet.dismiss();
            });
            bottomSheet.findViewById(R.id.webview_more_browser).setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                startActivity(intent);
                bottomSheet.dismiss();
            });
            bottomSheet.findViewById(R.id.webview_more_copy_url).setOnClickListener(v1 -> {
                ClipBoardUtil.copy(webView.getUrl());
                IToast.makeTextBottom(WebViewActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                bottomSheet.dismiss();
            });
            bottomSheet.findViewById(R.id.webview_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        }
    }

    public void refresh() {
        webView.reload();
    }

    public void clearCache() {
        webView.clearCache(true);
    }

    public void clearData() {
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearMatches();
        deleteDatabase("WebView.db");
        deleteDatabase("WebViewCache.db");
        getCacheDir().delete();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
