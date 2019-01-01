package com.appzone.dukkan.language_helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import java.util.Locale;

public class LanguageHelper {

    private static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";

    public static Context onAttach(Context context , String language)
    {
        String lang = getLanguage(context,language);
        return setLocality(context,lang);
    }

    public static Context setLocality(Context context,String language)
    {
        saveLanguage(context,language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return updateResource(context,language);
        }else
            {
                return updateLegacy(context,language);
            }

    }

    private static Context updateLegacy(Context context ,String lang)
    {
        Locale locale = new Locale(lang);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }else
            {
                context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
            }

            return context;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Context updateResource(Context context, String lang)
    {
        Locale locale = new Locale(lang);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void saveLanguage(Context context,String lang)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE,lang);
        editor.apply();
    }

    private static String getLanguage(Context context ,String default_lang)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = preferences.getString(SELECTED_LANGUAGE,default_lang);
        return lang;
    }
}
