package com.example.huikeli.facebooksearch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huikeli.adapter.resultAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huikeli on 2017/4/20.
 */

public class FavFragment extends Fragment{
    private Fragment users, pages, events, places, groups;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> frags = new ArrayList<Fragment>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fav_fragment, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager_fav);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs_fav);

        Bundle bundle1 = new Bundle();
        bundle1.putString("type","user");
        users = new FavListFrag();
        users.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString("type","page");
        pages = new FavListFrag();
        pages.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("type","event");
        events = new FavListFrag();
        events.setArguments(bundle3);

        Bundle bundle4 = new Bundle();
        bundle4.putString("type","place");
        places = new FavListFrag();
        places.setArguments(bundle4);

        Bundle bundle5 = new Bundle();
        bundle5.putString("type","group");
        groups = new FavListFrag();
        groups.setArguments(bundle5);

        frags.add(users);
        frags.add(pages);
        frags.add(events);
        frags.add(places);
        frags.add(groups);

        resultAdapter adapter = new resultAdapter(getChildFragmentManager(),frags);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);

        return view;
    }
}
