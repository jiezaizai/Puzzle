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

import com.example.gj.puzzle.entity.System_Languate;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class Language_util {

    private static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(7) {{
        put(System_Languate.ENGLISH, Locale.ENGLISH);
        put(System_Languate.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(System_Languate.JAPANESE, Locale.JAPANESE);
    }};

    public static void setLanAtr(String language, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting_share", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.commit();
    }

    public static String getLanAtr(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting_share", 0);
        String lanAtr = sharedPreferences.getString("language", "zh");
        return lanAtr;
    }

    //    public static Context initAppLanguage(Context context, String language) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //7.0及以上的方法
//            return createConfiguration(context, language);
//        } else {
//            updateConfiguration(context, language);
//            return context;
//        }
//    }
//
//    /**
//     * 7.0以下的修改app语言的方法
//     *
//     * @param context  context
//     * @param language language
//     */
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private static void updateConfiguration(Context context, String language) {
//        Resources resources = context.getResources();
//        Locale locale = new Locale(language);
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(locale);
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        resources.updateConfiguration(configuration, displayMetrics);
//    }
    public static Context initAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
            return context.createConfigurationContext(configuration);

        } else {
            configuration.locale = locale;

        }
        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
        return context;

    }
    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language);
        } else {
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.ENGLISH;
    }

    /**
     * java.util.HashMap.containsKey()方法用于检查特定键是否被映射到HashMap。它将key元素作为参数，如果该元素在map中映射，则返回True。
     * @param language
     * @return
     */
    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

}
