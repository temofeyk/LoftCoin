package com.temofey.loftcoin.screens.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.temofey.loftcoin.App;
import com.temofey.loftcoin.R;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.screens.start.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, WelcomeActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.start_btn)
    Button startBtn;

    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        final Prefs prefs = ((App) getApplication()).getPrefs();

        pager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager, true);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setFirstLaunch(false);
                StartActivity.startInNewTask(WelcomeActivity.this);
            }
        });

    }
}
