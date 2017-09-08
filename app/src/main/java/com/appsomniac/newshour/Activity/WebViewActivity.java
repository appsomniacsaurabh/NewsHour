package com.appsomniac.newshour.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.appsomniac.newshour.Config.Config;
import com.appsomniac.newshour.R;

public class WebViewActivity extends AppCompatActivity {

    String newsUrl = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView myWebView = (WebView) findViewById(R.id.webview);
         myWebView.getSettings().setJavaScriptEnabled(false);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        myWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        Intent i = getIntent();
        String url = i.getStringExtra("articleUrl");
        newsUrl = url;
        myWebView.loadUrl(url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.shareNews:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = newsUrl;
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "NewsHour: The all in one news app");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share news via: "));

                return true;

            case R.id.shareApp:

                Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "NewsHour: The all in one news app");
                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, Config.PLAYSTORE_LINK);
                startActivity(Intent.createChooser(sendIntent, "Share App via: "));

                return true;



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}