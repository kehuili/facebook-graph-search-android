package com.example.huikeli.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by huikeli on 2017/4/24.
 */

public class DetailAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private List<Fragment> frags;

    public DetailAdapter(FragmentManager fm, List<Fragment> l) {
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
                title = "Albums";
                break;
            case 1:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.pages);
                title = "Posts";
                break;

            default:
                //drawable = ContextCompat.getDrawable(mContext, R.drawable.users);
                title = "Albums";
                break;
        }

        return title;
    }
}
