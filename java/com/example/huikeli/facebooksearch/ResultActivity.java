package com.example.huikeli.facebooksearch;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.huikeli.adapter.resultAdapter;
import com.example.huikeli.tools.GetLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huikeli on 2017/4/20.
 */

public class ResultActivity extends AppCompatActivity{
    private Fragment users, pages, events, places, groups;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> frags = new ArrayList<Fragment>();
    private String location = "34.022352,-118.285117";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //location
        GetLocation glocation = new GetLocation(this);
        String latitude = "",longitude = "";
        if (glocation.canGetLocation()) {
            latitude = String.valueOf(glocation.latitude);
            longitude = String.valueOf(glocation.longitude);
        }
        if(!latitude.isEmpty()&&!longitude.isEmpty()) {
            location = latitude + "," + longitude;
        }

        Intent intent = getIntent();
        String kw = intent.getStringExtra("kw");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Results");

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);

        Bundle bundle1 = new Bundle();
        bundle1.putString("url","search="+kw+"&type=user");
        users = new ResultListFrag();
        users.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString("url","search="+kw+"&type=page");
        pages = new ResultListFrag();
        pages.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("url","search="+kw+"&type=event");
        events = new ResultListFrag();
        events.setArguments(bundle3);

        Bundle bundle4 = new Bundle();
        Log.i("lo",location);
        if(location!=null) {
            bundle4.putString("url", "search=" + kw + "&type=place"+"&location="+location);
        }else{
            bundle4.putString("url", "search=" + kw + "&type=place");
        }
        places = new ResultListFrag();
        places.setArguments(bundle4);

        Bundle bundle5 = new Bundle();
        bundle5.putString("url","search="+kw+"&type=group");
        groups = new ResultListFrag();
        groups.setArguments(bundle5);

        frags.add(users);
        frags.add(pages);
        frags.add(events);
        frags.add(places);
        frags.add(groups);

        resultAdapter adapter = new resultAdapter(getSupportFragmentManager(),frags);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
