package com.example.huikeli.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huikeli.facebooksearch.R;
import com.example.huikeli.tools.ImageDownloadTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huikeli on 2017/4/25.
 */

public class PostAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;
    public PostAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final PostAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_item, null);
            holder = new PostAdapter.ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.post_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.post_image);
            holder.timeView = (TextView) convertView.findViewById(R.id.post_time);
            holder.messageView = (TextView) convertView.findViewById(R.id.post_content);
            convertView.setTag(holder);
        } else {
            holder = (PostAdapter.ViewHolder) convertView.getTag();
        }

        final HashMap<String, Object> map = listData.get(position);
        holder.nameView.setText(map.get("name").toString());
        holder.timeView.setText(map.get("time").toString());
        holder.messageView.setText(map.get("message").toString());
        if (holder.imageView != null) {
            new ImageDownloadTask(holder.imageView).execute(map.get("url").toString());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        ImageView imageView;
        TextView timeView;
        TextView messageView;
    }
}
