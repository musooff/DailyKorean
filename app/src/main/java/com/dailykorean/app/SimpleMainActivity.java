package com.dailykorean.app;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import beautifiers.FontTextView;


public class SimpleMainActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
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

        SimpleDateFormat printingDate = new SimpleDateFormat("dd MMM yyyy", Locale.KOREAN);

        Bundle extras = getIntent().getExtras();
        String day = extras.getString("day");
        try {
            Date current = new SimpleDateFormat("yyyyMMdd",Locale.KOREAN).parse(extras.getString("day"));
            day = printingDate.format(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int page = extras.getInt("page");
        mViewPager.setCurrentItem(page);

        final String finalDay = day;
        iv_convos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                tv_title.setText("Dialog of "+ finalDay);
            }
        });
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                tv_title.setText("Expression of "+ finalDay);
            }
        });
        iv_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
                tv_title.setText("Words of " + finalDay);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==1){
                    iv_convos.setAlpha(0.3f);
                    iv_words.setAlpha(0.3f);
                    iv_home.setAlpha(1f);
                    tv_title.setText("Expression of "+finalDay);
                }
                else if (position==0){
                    iv_home.setAlpha(0.3f);
                    iv_words.setAlpha(0.3f);
                    iv_convos.setAlpha(1f);
                    tv_title.setText("Dialog of "+finalDay);
                }
                else {
                    iv_convos.setAlpha(0.3f);
                    iv_home.setAlpha(0.3f);
                    iv_words.setAlpha(1f);
                    tv_title.setText("Words and Phrases of " + finalDay);
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
                    return new SimpleDescriptionActivity();

                case 1:
                    return new SimpleTitlesActivity();

                case 2:
                    return new SimpleCardsActivityNew();

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
