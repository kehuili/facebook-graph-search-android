package com.example.huikeli.facebooksearch;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huikeli.adapter.MyListAdapter;
import com.example.huikeli.tools.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huikeli on 2017/4/21.
 */

public class ResultListFrag extends Fragment {
    private String sUrl;
    private View view;
    private ListView lv;
    private Button prev;
    private Button next;
    private MyListAdapter adapter = null;
    private LinearLayout ll;
    private LinearLayout llbutton;
    private int tag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.result_fragment, container, false);
            lv = (ListView) view.findViewById(R.id.result_list);
            next = (Button)view.findViewById(R.id.next);
            prev = (Button)view.findViewById(R.id.prev);
            ll = (LinearLayout)view.findViewById(R.id.llresult);
            llbutton = (LinearLayout)view.findViewById(R.id.llbutton);
            final Data app = (Data)getActivity().getApplication();
            sUrl = app.getUrl();

            Bundle bundle = this.getArguments();
            if(bundle != null) {
                sUrl += bundle.getString("url");
                //Log.i("test", sUrl);
                new SearchTask().execute(sUrl);
            }
        }
        //view = inflater.inflate(R.layout.result_fragment, container, false);

        return view;
    }

    private class SearchTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //read from inputstream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    //Log.i("data",line);
                    total.append(line);
                }
                urlConnection.disconnect();
                return total.toString();
            }catch (IOException e){
            //CharSequence text = "Wrong";
            //int duration = Toast.LENGTH_SHORT;
            //Toast toast = Toast.makeText(mContext, text, duration);
            //toast.show();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            final String type;
            if(sUrl.endsWith("user")){
                type = "user";
            }else if(sUrl.endsWith("page")){
                type = "page";
            }else if(sUrl.endsWith("event")){
                type = "event";
            }else if(sUrl.endsWith("group")){
                type = "group";
            }else {
                type = "place";
            }
            if(result.isEmpty()||result==null||result.equals("{\"data\":[]}")){
                TextView tv = new TextView(getActivity());
                tv.setText("No results available to display");
                tv.setTextSize(20);
                tv.setTextColor(Color.parseColor("#000000"));
                ll.addView ( tv );
                ((ViewGroup)lv.getParent()).removeView(lv);
                ((ViewGroup)llbutton.getParent()).removeView(llbutton);
            }else {
                final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                final ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
                final ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = null;
                try {
                    JSONObject data = new JSONObject(result);
                    JSONArray ja = data.optJSONArray("data");

                    for (int i = 0; i < ja.length();i++) {
                        map = new HashMap<String, Object>();
                        //an item
                        JSONObject jsonObject = ja.optJSONObject(i);
                        String id = jsonObject.optString("id");
                        String name = jsonObject.optString("name");
                        //picture
                        JSONObject pic = jsonObject.optJSONObject("picture");
                        pic = pic.optJSONObject("data");
                        String url = pic.optString("url");
                        map.put("id",id);
                        map.put("name",name);
                        map.put("url",url);
                        if(i<10) {
                            list.add(map);
                        }else if(i<20){
                            list1.add(map);
                        }else{
                            list2.add(map);
                        }
                    }
                    adapter = new MyListAdapter(getActivity(),list,type,false);
                    lv.setAdapter(adapter);
                    final Context c = getActivity();
                    prev.setEnabled(false);
                    if(list1!=null) {
                        next.setEnabled(true);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                prev.setEnabled(true);
                                prev.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(tag==2) {
                                            next.setEnabled(true);
                                            adapter = new MyListAdapter(c, list1, type, false);
                                            lv.setAdapter(adapter);
                                        }else if(tag==1){
                                            next.setEnabled(true);
                                            adapter = new MyListAdapter(c, list, type, false);
                                            lv.setAdapter(adapter);
                                            prev.setEnabled(false);
                                        }else{
                                            next.setEnabled(true);
                                            prev.setEnabled(false);
                                        }
                                        tag--;
                                    }
                                });

                                if(tag==0) {
                                    adapter = new MyListAdapter(c, list1, type, false);
                                    lv.setAdapter(adapter);
                                    if(list2==null){
                                        next.setEnabled(false);
                                    }
                                }else if(tag==1){
                                    adapter = new MyListAdapter(c, list2, type, false);
                                    lv.setAdapter(adapter);
                                    next.setEnabled(false);
                                }else{
                                    Log.i("aaa", Integer.toString(tag));
                                    next.setEnabled(false);
                                }
                                tag++;
                            }
                        });
                    }else{
                        next.setEnabled(false);
                    }


                    /*JSONObject paging = data.optJSONObject("paging");
                    final String prevb = paging.optString("previous");
                    final String nextb = paging.optString("next");
                    if(!prevb.isEmpty()) {
                        prev.setEnabled(true);
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SearchTask().execute(prevb);
                            }
                        });
                    }else{
                        prev.setEnabled(false);
                    }
                    if(!nextb.isEmpty()) {
                        next.setEnabled(true);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SearchTask().execute(nextb);
                            }
                        });
                    }else{
                        next.setEnabled(false);
                    }*/
                    //adapter = new MyListAdapter(getActivity(),list,type,false);
                    //lv.setAdapter(adapter);
                } catch (JSONException e) {
                }
            };
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}

