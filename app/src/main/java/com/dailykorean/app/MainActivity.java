package com.dailykorean.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;

import beautifiers.FontTextView;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;
    ImageView iv_convos,iv_home,iv_words, iv_favorites,iv_saveds;
    FontTextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("Download Data",0);

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = sharedPreferences.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, MainIntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = sharedPreferences.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        setContentView(R.layout.activity_inflator);



        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.icon_saved);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDarker)));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        iv_convos = (ImageView)findViewById(R.id.iv_conveos);
        iv_home = (ImageView)findViewById(R.id.iv_home);
        iv_words = (ImageView)findViewById(R.id.iv_words);

        iv_favorites = (ImageView)findViewById(R.id.iv_main_star);
        iv_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorites = new Intent(getApplicationContext(),FavoritesActivityNew.class);
                startActivity(favorites);
            }
        });
        iv_saveds = (ImageView)findViewById(R.id.iv_main_saved);
        iv_saveds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorites = new Intent(getApplicationContext(),SavedsActivity.class);
                startActivity(favorites);
            }
        });

        tv_title = (FontTextView)findViewById(R.id.tv_title);

        iv_convos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                tv_title.setText("Today's Dialog");
            }
        });
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                tv_title.setText("Today's Expression");
            }
        });
        iv_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
                tv_title.setText("Today's Words");
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==1){
                    iv_convos.setAlpha(0.3f);
                    iv_words.setAlpha(0.3f);
                    iv_home.setAlpha(1f);
                    tv_title.setText("Today's Expression");
                }
                else if (position==0){
                    iv_home.setAlpha(0.3f);
                    iv_words.setAlpha(0.3f);
                    iv_convos.setAlpha(1f);
                    tv_title.setText("Today's Dialog");
                }
                else {
                    iv_convos.setAlpha(0.3f);
                    iv_home.setAlpha(0.3f);
                    iv_words.setAlpha(1f);
                    tv_title.setText("Today's Words and Phrases");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new DescriptionActivity();

                case 1:
                    return new TitlesActivity();

                case 2:
                    return new CardsActivityNew();

                default:
                    break;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages. Offline and Online
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Online";
                case 1:
                    return "Offline";
                case 2:
                    return "Offline";
            }
            return null;
        }
    }


}
