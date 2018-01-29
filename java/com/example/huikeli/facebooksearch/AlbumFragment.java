package com.example.huikeli.facebooksearch;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.huikeli.adapter.AlbumAdapter;
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
import java.util.List;

/**
 * Created by huikeli on 2017/4/24.
 */
public class AlbumFragment extends Fragment {
    private View view;
    private ExpandableListView lv;
    private String sUrl;
    private String URL;
    private String id;
    private LinearLayout ll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.album_fragment, container, false);
            lv = (ExpandableListView) view.findViewById(R.id.album_list);
            ll = (LinearLayout)view.findViewById(R.id.ll_album);
            final Data app = (Data) getActivity().getApplication();
            sUrl = app.getUrl();
            URL = sUrl;
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                sUrl += bundle.getString("url");
                //Log.i("test", sUrl);
                new SearchTask().execute(sUrl);
            }
        }
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
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if(result.isEmpty()||result==null||result.equals("{\"data\":[]}")){
                TextView tv = new TextView(getActivity());
                tv.setText("No albums available to display");
                tv.setTextSize(20);
                tv.setTextColor(Color.parseColor("#000000"));
                ll.addView ( tv );
            }else {
                //ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                //HashMap<String, Object> map = null;
                ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
                ArrayList<String> list = new ArrayList<String>();
                try {
                    JSONObject data = new JSONObject(result);
                    JSONObject albums = data.optJSONObject("albums");
                    if(albums!=null) {
                        JSONArray al = albums.optJSONArray("data");
                        for (int i = 0; i < al.length(); i++) {
                            //map = new HashMap<String, Object>();
                            //an item
                            JSONObject jsonObject = al.optJSONObject(i);
                            String name = jsonObject.optString("name");
                            list.add(name);
                            JSONObject temp = jsonObject.optJSONObject("photos");

                            ArrayList<String> l = new ArrayList<String>();
                            if(temp!=null){
                                JSONArray pic = temp.getJSONArray("data");
                                for(int j = 0; j<pic.length();j++){
                                    JSONObject jo = pic.optJSONObject(j);
                                    String url = jo.optString("picture");
                                    //url = URL+"picture="+url;
                                    Log.i("aaaa",url);
                                    if(url!=null||url!=""){
                                        l.add(url);
                                    }
                                }
                                //map.put("name",name);
                                //map.put("pics",l);
                                lists.add(l);
                            }else{
                                l.add("NO");
                                lists.add(l);
                            }
                            //Log.i("json", id+" "+name+" "+url);
                            //map.put("id",id);
                        }
                    }else{
                        //Log.i("album","xxxx");
                        TextView tv = new TextView(getActivity());
                        tv.setText("No albums available to display");
                        tv.setTextSize(20);
                        tv.setTextColor(Color.parseColor("#000000"));
                        ll.addView ( tv );
                    }
                    Log.i("list",lists.toString());
                    final AlbumAdapter adapter = new AlbumAdapter(getActivity(),lists,list);
                    //SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.list_item, strings, ids);
                    lv.setAdapter(adapter);
                    lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            for (int i = 0; i <adapter.getGroupCount(); i++) {
                                if (groupPosition!=i) {
                                    lv.collapseGroup(i);
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                }
            }
        }
    }

}
