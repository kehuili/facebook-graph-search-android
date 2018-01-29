package com.example.huikeli.facebooksearch;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huikeli.adapter.PostAdapter;
import com.example.huikeli.tools.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by huikeli on 2017/4/24.
 */

public class PostFragment extends Fragment {
    private View view;
    private ListView lv;
    private String sUrl;
    private String id;
    private LinearLayout ll;
    private String name;
    private String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.post_fragment, container, false);
            lv = (ListView) view.findViewById(R.id.post_list);
            ll = (LinearLayout)view.findViewById(R.id.ll_post);
            final Data app = (Data) getActivity().getApplication();
            sUrl = app.getUrl();
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                sUrl += bundle.getString("url");
                name = bundle.getString("name");
                url = bundle.getString("urlp");
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
                    //Log.i("datap",line);
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
                tv.setText("No posts available to display");
                tv.setTextSize(20);
                tv.setTextColor(Color.parseColor("#000000"));
                ll.addView ( tv );
            }else {
                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = null;
                try {
                    JSONObject data = new JSONObject(result);
                    JSONObject posts = data.optJSONObject("posts");
                    if(posts!=null) {
                        JSONArray p = posts.getJSONArray("data");
                        for (int i = 0; i < p.length(); i++) {
                            map = new HashMap<String, Object>();
                            JSONObject jsonObject = p.optJSONObject(i);
                            String message = jsonObject.optString("message");
                            String time = jsonObject.optString("created_time");
                            //android.text.format.DateFormat df = new android.text.format.DateFormat();
                            //Date date = new Date(time);
                            //time = df.format("yyyy-mm-dd HH:mm:ss",date).toString();
                            long timeMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS")
                                    .parse(time)
                                    .getTime();
                            Date date = new Date(timeMillis);
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            time = formatter.format(date);
                            map.put("message",message);
                            map.put("time", time);
                            map.put("name",name);
                            map.put("url",url);
                            list.add(map);
                        }
                    }else{
                        TextView tv = new TextView(getActivity());
                        tv.setText("No posts available to display");
                        tv.setTextSize(20);
                        tv.setTextColor(Color.parseColor("#000000"));
                        ll.addView ( tv );
                    }

                    PostAdapter adapter = new PostAdapter(getActivity(),list);
                    //SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.list_item, strings, ids);
                    lv.setAdapter(adapter);
                } catch (Exception e) {
                }
            }
        }
    }
}
