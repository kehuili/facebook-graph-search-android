package com.example.huikeli.facebooksearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.huikeli.adapter.DetailAdapter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by huikeli on 2017/4/20.
 */

public class DetailsActivity extends AppCompatActivity{
    private Fragment album, post;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> frags = new ArrayList<Fragment>();
    private String id;
    private String name;
    private String url;
    private String type;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        c = this;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(c, "You shared this post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(c, "You cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(c, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("More Details");

        viewPager = (ViewPager)findViewById(R.id.viewpager_details);
        tabLayout = (TabLayout)findViewById(R.id.tabs_details);

        Bundle bundle1 = new Bundle();
        bundle1.putString("url","id="+id);
        album = new AlbumFragment();
        album.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString("url","id="+id);
        bundle2.putString("name",name);
        bundle2.putString("urlp",url);
        post= new PostFragment();
        post.setArguments(bundle2);

        frags.add(album);
        frags.add(post);
        DetailAdapter adapter = new DetailAdapter(getSupportFragmentManager(),frags);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.albums);
        tabLayout.getTabAt(1).setIcon(R.drawable.posts);
    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_to_fav:
                SharedPreferences pre;
                switch (type){
                    case "user":
                        pre=getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                    case "page":
                        pre=getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
                        break;
                    case "event":
                        pre=getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
                        break;
                    case "place":
                        pre=getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
                        break;
                    case "group":
                        pre=getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
                        break;
                    default:
                        pre=getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                }

                SharedPreferences.Editor editor=pre.edit();
                if(pre.contains(id)){
                    editor.remove(id);
                    editor.apply();
                    item.setTitle("Add to Favorites");
                    CharSequence text = "Removed from Favorites";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();
                }else{
                    String sp= "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"url\":\""+
                            url+"\"}";
                    editor.putString(id,sp);
                    editor.commit();
                    item.setTitle("Delete from Favorites");
                    CharSequence text = "Added to Favorites";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();
                }
                return true;
            case R.id.share:
                CharSequence text = "Facebook share";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, text, duration);
                toast.show();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(" http://facebook.com/"+id))
                            .setContentTitle(name)
                            .setImageUrl(Uri.parse(url))
                            .setContentDescription("FB SEARCH FROM USC CSCI571")
                            .build();
                    shareDialog.show(linkContent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       SharedPreferences pre;
       switch (type){
           case "user":
               pre=getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
               break;
           case "page":
               pre=getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
               break;
           case "event":
               pre=getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
               break;
           case "place":
               pre=getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
               break;
           case "group":
               pre=getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
               break;
           default:
               pre=getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
               break;
       }
       if(pre.contains(id)){
           getMenuInflater().inflate(R.menu.del_toolbar_menu, menu);
       }else{
           getMenuInflater().inflate(R.menu.toolbar_menu, menu);
       }
       return super.onCreateOptionsMenu(menu);
   }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
