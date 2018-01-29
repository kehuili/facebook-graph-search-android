package com.example.huikeli.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huikeli.facebooksearch.DetailsActivity;
import com.example.huikeli.facebooksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huikeli on 2017/4/24.
 */

public class MyListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String type;
    private Boolean fav;

    public MyListAdapter(Context context, ArrayList<HashMap<String, Object>> listData, String type,Boolean fav) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.type = type;
        this.fav = fav;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.list_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_photo);
            holder.detailView = (ImageView) convertView.findViewById(R.id.list_detail);
            holder.favView = (ImageView) convertView.findViewById(R.id.list_fav);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HashMap<String, Object> map = listData.get(position);
        holder.nameView.setText(map.get("name").toString());
        if (holder.imageView != null) {
            Picasso.with(mContext).load(map.get("url").toString()).into(holder.imageView);
            //new ImageDownloadTask(holder.imageView).execute(map.get("url").toString());
        }

        SharedPreferences pre;
        switch (type){
            case "user":
                pre=mContext.getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                break;
            case "page":
                pre=mContext.getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
                break;
            case "event":
                pre=mContext.getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
                break;
            case "place":
                pre=mContext.getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
                break;
            case "group":
                pre=mContext.getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
                break;
            default:
                pre=mContext.getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                break;
        }
        String s = pre.getString(map.get("id").toString(),"");
        if(s.isEmpty()){
                holder.favView.setImageResource(R.drawable.favorites_off);

        }else{
            holder.favView.setImageResource(R.drawable.favorites_on);
        }

        holder.favView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre;
                switch (type){
                    case "user":
                        pre=mContext.getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                    case "page":
                        pre=mContext.getSharedPreferences("favorites_page", Context.MODE_PRIVATE);
                        break;
                    case "event":
                        pre=mContext.getSharedPreferences("favorites_event", Context.MODE_PRIVATE);
                        break;
                    case "place":
                        pre=mContext.getSharedPreferences("favorites_place", Context.MODE_PRIVATE);
                        break;
                    case "group":
                        pre=mContext.getSharedPreferences("favorites_group", Context.MODE_PRIVATE);
                        break;
                    default:
                        pre=mContext.getSharedPreferences("favorites_user", Context.MODE_PRIVATE);
                        break;
                }
                //Log.i("type",type);
                SharedPreferences.Editor editor=pre.edit();
                String s = pre.getString(map.get("id").toString(),"");
                if(s.isEmpty()){
                    holder.favView.setImageResource(R.drawable.favorites_on);
                    String sp= "{\"id\":\""+map.get("id").toString()+"\",\"name\":\""+map.get("name").toString()+"\",\"url\":\""+
                            map.get("url").toString()+"\"}";
                    editor.putString(map.get("id").toString(),sp);
                    editor.commit();
                }else{
                    holder.favView.setImageResource(R.drawable.favorites_off);
                    editor.remove(map.get("id").toString());
                    editor.apply();
                    if(fav==true){
                        listData.remove(position);
                        notifyDataSetChanged();
                    }
                }
            }
        });
        holder.detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("id",map.get("id").toString());
                intent.putExtra("name",map.get("name").toString());
                intent.putExtra("url",map.get("url").toString());
                intent.putExtra("type",type);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        ImageView imageView;
        ImageView detailView;
        ImageView favView;
    }
}

