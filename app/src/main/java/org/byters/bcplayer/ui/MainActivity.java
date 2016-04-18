package org.byters.bcplayer.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.byters.bcplayer.R;
import org.byters.bcplayer.controllers.adapters.PagesAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagesAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
    }
}
