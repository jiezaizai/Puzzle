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
    /**
     * 这个方法会在oncreate方法之前执行。
     */
    protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(Language_util.initAppLanguage(newBase, Language_util.getLanAtr(MyApplication.context)));

    }

}
