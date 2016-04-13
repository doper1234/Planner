package com.example.chris.planner;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class SlideScreenActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_screen);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.slideScreenPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);

        ImageView homeIcon = (ImageView) findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
                homeFragment();
            }
        });
        ImageView calendarIcon = (ImageView) findViewById(R.id.calendarIcon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
                calendarFragment();
            }
        });
        ImageView historyIcon = (ImageView) findViewById(R.id.historyIcon);
        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
                historyFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Toast.makeText(SlideScreenActivity.this, "Position: " + position, Toast.LENGTH_LONG).show();
            if(position == 0){
                homeFragment();
                return new TodaysEventsFragment(SlideScreenActivity.this, SlideScreenActivity.this);
            }else if (position == 1){
                calendarFragment();
                return new CalendarFragment(SlideScreenActivity.this, SlideScreenActivity.this);
            }else{
                historyFragment();
                return new HistoryFragment(SlideScreenActivity.this, SlideScreenActivity.this);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

    public void homeFragment(){
        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon_pressed);
        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon);
        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon);
    }

    public void calendarFragment(){
        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon);
        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon);
        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon_pressed);
    }
    public void historyFragment(){
        ((ImageView) findViewById(R.id.homeIcon)).setImageResource(R.drawable.home_icon);
        ((ImageView) findViewById(R.id.calendarIcon)).setImageResource(R.drawable.calendar_icon);
        ((ImageView) findViewById(R.id.historyIcon)).setImageResource(R.drawable.history_icon_pressed);
    }
}

