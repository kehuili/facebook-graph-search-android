package com.example.huikeli.facebooksearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.huikeli.adapter.MyListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huikeli on 2017/4/25.
 */

public class FavListFrag extends Fragment {
    private View view;
    private ListView lv;
    private String type;
    private MyListAdapter adapter;
    private ArrayList<HashMap<String, Object>> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Left", "onCreate()");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
            Log.i("b","b");
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            Log.i("a","a");
            view = inflater.inflate(R.layout.favorite_list, container, false);
            lv = (ListView) view.findViewById(R.id.fav_list);

            Bundle bundle = this.getArguments();
            if(bundle != null) {
                type = bundle.getString("type");
                Log.i("type",type);
                SharedPreferences pre;
                switch (type){
                    case "user":
                        pre=getActivity().getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                    case "page":
                        pre=getActivity().getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
                        break;
                    case "event":
                        pre=getActivity().getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
                        break;
                    case "place":
                        pre=getActivity().getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
                        break;
                    case "group":
                        pre=getActivity().getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
                        break;
                    default:
                        pre=getActivity().getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                }
                list = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = null;

                Map<String, ?> allEntries = pre.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    String s = entry.getValue().toString();
                    try {
                        map = new HashMap<String, Object>();
                        JSONObject data = new JSONObject(s);
                        String id = data.optString("id");
                        String name = data.optString("name");
                        String url = data.optString("url");
                        Log.i("entry",id+" "+name+" "+url+" "+type);
                        map.put("id",id);
                        map.put("name",name);
                        map.put("url",url);
                        list.add(map);
                    }catch (JSONException e){}
                }

                adapter = new MyListAdapter(getActivity(),list,type,true);
                lv.setAdapter(adapter);

            }
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(adapter!=null) {
            SharedPreferences pre;
            switch (type){
                case "user":
                    pre=getActivity().getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                    break;
                case "page":
                    pre=getActivity().getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
                    break;
                case "event":
                    pre=getActivity().getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
                    break;
                case "place":
                    pre=getActivity().getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
                    break;
                case "group":
                    pre=getActivity().getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
                    break;
                default:
                    pre=getActivity().getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                    break;
            }
            list = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = null;

            Map<String, ?> allEntries = pre.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                String s = entry.getValue().toString();
                try {
                    map = new HashMap<String, Object>();
                    JSONObject data = new JSONObject(s);
                    String id = data.optString("id");
                    String name = data.optString("name");
                    String url = data.optString("url");
                    map.put("id",id);
                    map.put("name",name);
                    map.put("url",url);
                    list.add(map);
                }catch (JSONException e){}
            }
            adapter = new MyListAdapter(getActivity(),list,type,true);
            lv.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }
}
