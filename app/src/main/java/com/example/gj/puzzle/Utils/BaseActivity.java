package com.example.gj.puzzle.Utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.example.gj.puzzle.Utils.Language_util;
import com.example.gj.puzzle.Utils.MyApplication;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            super.attachBaseContext(newBase);
        } else {
            //zh：中文
            super.attachBaseContext(Language_util.initAppLanguage(newBase, Language_util.getLanAtr(MyApplication.context)));
        }
    }

}
