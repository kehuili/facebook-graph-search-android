package com.example.huikeli.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.huikeli.facebooksearch.R;
import com.example.huikeli.facebooksearch.ResultListFrag;

import java.util.List;

/**
 * Created by huikeli on 2017/4/21.
 */

public class resultAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 5;
    private List<Fragment> frags;

    public resultAdapter(FragmentManager fm, List<Fragment> l) {
        super(fm);
        frags = l;
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Drawable drawable;
        String title;
        switch (position) {
            case 0:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.users);
                title = "Users";
                break;
            case 1:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.pages);
                title = "Pages";
                break;
            case 2:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.events);
                title = "Events";
                break;
            case 3:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.places);
                title = "Places";
                break;
            case 4:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.groups);
                title = "Groups";
                break;
            default:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.users);
                title = "Users";
                break;
        }

        return title;
    }
}

