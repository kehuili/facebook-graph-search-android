package com.example.huikeli.facebooksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by huikeli on 2017/4/20.
 */

public class HomeFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        final EditText et = (EditText)view.findViewById(R.id.FBkeyword);
        Button clear = (Button)view.findViewById(R.id.clear);
        Button b = (Button)view.findViewById(R.id.search);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kw = et.getText().toString();
                if(kw == null || kw.equals("")){
                    CharSequence text = "Please enter a keyword!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }else {
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    kw=kw.replaceAll("\\s+", "%20");
                    Log.i("kw",kw);
                    intent.putExtra("kw",kw);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
