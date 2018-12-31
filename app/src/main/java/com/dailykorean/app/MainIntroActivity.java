package com.dailykorean.app;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by moshe on 05/05/2017.
 */

public class MainIntroActivity extends IntroActivity {
    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Add slides, edit configuration...
        addSlide(new SimpleSlide.Builder()
                .title("Today's Expression")
                .description("Improve your Korean skills with fresh daily expressions")
                .image(R.drawable.intro_image_1)
                .background(R.color.screen1)
                .backgroundDark(R.color.screen1Darker)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Today's Dialog")
                .description("Daily Conversations. Tap to get translation.")
                .image(R.drawable.intro_image_2)
                .background(R.color.screen2)
                .backgroundDark(R.color.screen2Darker)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("New Words")
                .description("Learn new words everyday. Tap to see meaning.")
                .image(R.drawable.intro_image_3_3)
                .background(R.color.screen3)
                .backgroundDark(R.color.screen3Darker)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Favorites")
                .description("Save your expressions or words for easy access")
                .image(R.drawable.intro_image_4)
                .background(R.color.screen4)
                .backgroundDark(R.color.screen4Darker)
                .scrollable(true)
                .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build());

    }
}