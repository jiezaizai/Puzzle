package com.example.gj.puzzle.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class Language_util {
    public static void setLanAtr(String language,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting_share", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",language);
        editor.commit();
    }
    public static String getLanAtr(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting_share", 0);
        String lanAtr = sharedPreferences.getString("language", "zh");
        return  lanAtr;
    }
    public static Context initAppLanguage(Context context, String language) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0及以上的方法
            return createConfiguration(context, language);
        } else {
            updateConfiguration(context, language);
            return context;
        }
    }
//    private static boolean isSupportLanguage(String language) {
//        return mAllLanguages.containsKey(language);
//    }

    /**
     * 7.0及以上的修改app语言的方法
     *
     * @param context  context
     * @param language language
     * @return context
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Context createConfiguration(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 7.0以下的修改app语言的方法
     *
     * @param context  context
     * @param language language
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void updateConfiguration(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);
    }

    /**
     * 判断当前的语言是否是简体中文
     *
     * @param context context
     * @return boolean
     */
    private static boolean currentLanguageIsSimpleChinese(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        return TextUtils.equals("zh", language);
    }

    /**
     * 获取当前的语言
     *
     * @param context context
     * @return boolean
     */
    public static String getCurrentLanguage(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        return language;
    }
}
