/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zam.todolist.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zam.todolist.R;
import com.zam.todolist.home.all.AllTaskFragment;
import com.zam.todolist.home.finished.FinishedTaskFragment;
import com.zam.todolist.home.pending.PendingTaskFragment;
import com.zam.todolist.utils.AppConstants;

public class MainActivity extends AppCompatActivity{

    private Toolbar tMa;
    private TabLayout tlMa;
    private ViewPager2 vpMa;
    private final String[] sTabs = {
            AppConstants.TAB_PENDING,
            AppConstants.TAB_FINISHED,
            AppConstants.TAB_ALL
    };
    private FloatingActionButton fabMa;
    private final Fragment[] myFragments = {
            new PendingTaskFragment(),
            new FinishedTaskFragment(),
            new AllTaskFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        tMa = findViewById(R.id.tMa);
        tlMa = findViewById(R.id.tlMa);
        vpMa = findViewById(R.id.vpMa);
        fabMa = findViewById(R.id.fabMa);

        setupViews();
    }

    private void setupViews() {
        tMa.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tMa);

        vpMa.setAdapter(new MyViewPagerAdapter(this));
        new TabLayoutMediator(tlMa, vpMa, (tab, position) -> tab.setText(sTabs[position])).attach();

        fabMa.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewTask.class);
            startActivity(intent);
        });

        loadAd(this, findViewById(R.id.avMa));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        return true;
    }

    private class MyViewPagerAdapter extends FragmentStateAdapter {

        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return myFragments[position];
        }

        @Override
        public int getItemCount() {
            return myFragments.length;
        }
    }

    public static void loadAd(Context context, View myAdView) {
        MobileAds.initialize(context, initializationStatus -> {});

        AdView adView = (AdView) myAdView;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public static void loadNativeAd(Context context, TemplateView myTemplate) {
        MobileAds.initialize(context);
        AdLoader adLoader = new AdLoader.Builder(context, AppConstants.AD_UNIT_ID)
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    myTemplate.setStyles(styles);
                    myTemplate.setNativeAd(nativeAd);
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
