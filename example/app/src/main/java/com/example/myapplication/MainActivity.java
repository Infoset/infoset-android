package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import app.infoset.android.InfosetChatActivity;
import app.infoset.android.InfosetChatConfiguration;

public class MainActivity extends AppCompatActivity {

    public static final String INFOSET_API_KEY = "your_api_key";
    public static final String INFOSET_ANDROID_KEY = "your_android_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void startChatActivity() {
        Intent intent = new Intent(this, InfosetChatActivity.class);
        final InfosetChatConfiguration config = BaseApplication.getInfosetChatConfiguration();
        intent.putExtras(config.asBundle());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        final Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragmentById instanceof OnBackPressedListener && ((OnBackPressedListener) fragmentById).onBackPressed()) {
            //Let fragment handle this
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void startFullScreenWindowExample(View view) {
        startActivity(new Intent(this, FullScreenWindowActivityExample.class));
    }

    public void startEmbeddedWindowExample(View view) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, EmbeddedInfosetChatFragmentExample.newInstance()).addToBackStack("EmbeddedFragmentExample").commit();
    }

    public void startOwnActivityExample(View view) {
        startChatActivity();
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

}
