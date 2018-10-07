package com.temofey.loftcoin.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import com.temofey.loftcoin.App;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.screens.start.StartActivity;
import com.temofey.loftcoin.screens.welcome.WelcomeActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App) getApplication()).getPrefs();

        if (prefs.isFirstLaunch()) {
            WelcomeActivity.startInNewTask(this);
        } else {
            StartActivity.startInNewTask(this);
        }
    }
}
