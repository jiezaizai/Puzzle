package com.example.gj.puzzle.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gj.puzzle.Adapter.MyFragmentPagerAdapter;
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

public class LookActivity extends BaseActivity {
    private ListView myListView;
    public SQLiteDatabase sqLiteDatabase;
    private ContentResolver contentResolver;
    private List<String> tabIndicators;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        contentResolver = this.getContentResolver();
        viewPager= findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        tabLayout =  findViewById(R.id.tabLayout);
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.list));
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        (toolbar.findViewById(R.id.back_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(LookActivity.this,MainActivity.class);
//                startActivity(intent);
                LookActivity.this.finish();
            }
        });
//        myListView = findViewById(R.id.myList);
//        this.setAdapter();

//        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(myFragmentPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        initContent();
        initTab();
    }
    private void initTab(){
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(tabLayout, 10);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            tabIndicators.add("难度 " + i);
        }
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);
    }

}
