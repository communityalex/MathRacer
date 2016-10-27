package com.community.zenith.mathracer.loginactivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.community.zenith.mathracer.GenericPagerAdapter;
import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.google.SlidingTabLayoutWhite;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tabs) SlidingTabLayoutWhite tabs;
    @BindView(R.id.pager) ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tabs.setCustomTabView(R.layout.custom_tab, 0);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayoutWhite.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.green) ;   //define any color in xml resources and set it here, I have used white
            }

        });
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        pager.setAdapter(new GenericPagerAdapter(getSupportFragmentManager(), this)
                .titles("Login", "Sign Up")
                .fragments(LoginFragment.class, SignUpFragment.class));
        pager.setOffscreenPageLimit(1);
        tabs.setViewPager(pager);
    }
}
