package com.example.aaron.congressapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

/**
 * Created by aaron on 3/1/16.
 */
public class CoolAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;

    public CoolAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[] BG_IMAGES = new int[] {};

    // A simple container for static data in each page
    private static class Page {
        // static resources
        int titleRes;
        int textRes;
        int iconRes;
    }

    // Create a static set of pages in a 2D array
    private final Page[][] PAGES = {  };

        // Override methods in FragmentGridPagerAdapter
        //...


    @Override
    public Fragment getFragment(int row, int col) {
        //Page page = PAGES[row][col];
        String title = "lol";
        //        page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
        String text = "abc";
        //        page.textRes != 0 ? mContext.getString(page.textRes) : null;
        CardFragment fragment = CardFragment.create(title, text);

        // Advanced settings (card gravity, card expansion/scrolling)
        //fragment.setCardGravity(page.cardGravity);
        //fragment.setExpansionEnabled(page.expansionEnabled);
        //fragment.setExpansionDirection(page.expansionDirection);
        //fragment.setExpansionFactor(page.expansionFactor);
        return fragment;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return 5;
    }
}