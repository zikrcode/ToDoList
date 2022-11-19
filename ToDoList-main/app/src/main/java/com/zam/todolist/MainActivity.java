package com.zam.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity{

    private Toolbar tMa;
    private TabLayout tlMa;
    private ViewPager2 vpMa;
    private final String[] sTabs={"PENDING","FINISHED","ALL"};
    private FloatingActionButton fabMa;
    private final Fragment[] myFragments = {new PendingTaskFragment(), new FinishedTaskFragment(), new AllTaskFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tMa=findViewById(R.id.tMa);
        tMa.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tMa);

        tlMa=findViewById(R.id.tlMa);
        vpMa=findViewById(R.id.vpMa);
        vpMa.setAdapter(new MyViewPagerAdapter(this));

        new TabLayoutMediator(tlMa, vpMa, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(sTabs[position]);
            }
        }).attach();

        fabMa=findViewById(R.id.fabMa);
        fabMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, NewTask.class);
                startActivity(intent);
            }
        });

        loadAd(this,findViewById(R.id.avMa));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
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
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView adView = (AdView) myAdView;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public static void loadNativeAd(Context context, TemplateView myTemplate) {
        MobileAds.initialize(context);
        AdLoader adLoader = new AdLoader.Builder(context, "--------------------------------------")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        myTemplate.setStyles(styles);
                        myTemplate.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
