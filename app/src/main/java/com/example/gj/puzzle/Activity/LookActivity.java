package com.example.gj.puzzle.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gj.puzzle.Adapter.MySimpleAdapter;
import com.example.gj.puzzle.R;
import com.example.gj.puzzle.Utils.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class LookActivity extends BaseActivity{
    private ListView myListView;
    public SQLiteDatabase sqLiteDatabase;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        contentResolver = this.getContentResolver();
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.list));
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        (toolbar.findViewById(R.id.refresh_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LookActivity.this.setAdapter();
                Toast.makeText(LookActivity.this,getResources().getString(R.string.refresh_success),Toast.LENGTH_SHORT).show();
            }
        });
        (toolbar.findViewById(R.id.back_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(LookActivity.this,MainActivity.class);
//                startActivity(intent);
                LookActivity.this.finish();
            }
        });
        myListView = findViewById(R.id.myList);
        this.setAdapter();
    }

    private void setAdapter() {
        int id=1;
        /**
         * 通过provider查询出数据
         */
        Cursor cursor = contentResolver.query(Uri.parse("content://org.gj.providers.playerProvider/players"), new String[]{"id", "name", "score"},null,null, "score asc");
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
                    this,
                    players,
                    R.layout.player_list_items,
                    new String[]{"id","name","score"},
                    new int[]{R.id.pid,R.id.pname,R.id.pscore}
            );
            myListView.setAdapter(mySimpleAdapter);
        }else{
            Toast.makeText(LookActivity.this,getResources().getString(R.string.empty),Toast.LENGTH_SHORT).show();

        }
    }
}
