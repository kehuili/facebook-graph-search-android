package com.example.huikeli.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huikeli.facebooksearch.R;
import com.example.huikeli.tools.ImageDownloadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by huikeli on 2017/4/25.
 */

public class AlbumAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<ArrayList<String>> pics;
    private ArrayList<String> names;
    public AlbumAdapter(Context context, ArrayList<ArrayList<String>> pics, ArrayList<String> names) {
        this.pics = pics;
        this.names = names;
        //layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }
    @Override
    public int getGroupCount() {
        return names.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return pics.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return names.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pics.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.album_name_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv = (TextView) convertView.findViewById(R.id.album_name);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tv.setText(names.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.album_pic_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.iv = (ImageView) convertView.findViewById(R.id.album_pic);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if(!pics.get(groupPosition).get(childPosition).equals("NO")) {
            Picasso.with(mContext)
                    .load(pics.get(groupPosition).get(childPosition))
                    .resize(400, 400)
                    .centerCrop()
                    .into(childViewHolder.iv);
        }else{
        }
        //new ImageDownloadTask(childViewHolder.iv).execute(pics.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class GroupViewHolder {
        TextView tv;
    }
    static class ChildViewHolder {
        ImageView iv;
    }
}
