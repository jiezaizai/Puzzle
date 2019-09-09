package com.example.gj.puzzle.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gj.puzzle.Adapter.MySimpleAdapter;
import com.example.gj.puzzle.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gj
 * Created on 19-9-9
 * Description
 */

public class MyFragment extends Fragment {

    private ContentResolver contentResolver;
    ListView listView;

    public static MyFragment newInstance(String text){
        Bundle bundle = new Bundle();
        bundle.putString("text",text);
        MyFragment myFragment = new MyFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.my_fragment,container,false);
        listView  = rootView.findViewById(R.id.myList);
        TextView textView=rootView.findViewById(R.id.tv_if);
        contentResolver = getActivity().getContentResolver();
            int id = 1;
            /**
             * 通过provider查询出数据
             */

            Cursor cursor = contentResolver.query(Uri.parse("content://org.gj.providers.playerProvider/players"),
                    new String[]{"id", "name", "score"}, "difficulty = ?", new String[]{getArguments().getString("text")}, "score asc");
            List<Map<String, Object>> players = new ArrayList<Map<String, Object>>();
            while (cursor.moveToNext()) {
                HashMap<String, Object> playerHashMap = new HashMap<String, Object>();
                playerHashMap.put("id", id);
                playerHashMap.put("name", cursor.getString(1));
                playerHashMap.put("score", cursor.getString(2));
                id++;
                players.add(playerHashMap);
            }
            cursor.close();
            if (!players.isEmpty()) {
                MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(
                        getActivity().getApplicationContext(),
                        players,
                        R.layout.player_list_items,
                        new String[]{"id", "name", "score"},
                        new int[]{R.id.pid, R.id.pname, R.id.pscore}
                );
                listView.setAdapter(mySimpleAdapter);}
            else {
               // Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.empty), Toast.LENGTH_SHORT).show();
                textView.setText(getResources().getString(R.string.empty));

            }


        return rootView;
    }


}
