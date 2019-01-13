package com.appzone.dukkan.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.tags.Tags;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preferences {
    private static Preferences instance=null;

    private Preferences() {
    }

    public static Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_userData(Context context, UserModel userModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",userData);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data","");
        UserModel userModel = gson.fromJson(user_data,UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",session);
        editor.apply();

    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }
    public void addRecentSearchQuery(Context context ,String query)
    {

        SharedPreferences preferences = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        String queries = preferences.getString("queries","");
        if (!TextUtils.isEmpty(queries))
        {

            try {
                String [] resultList = new Gson().fromJson(queries,String [].class);

                List<String> queriesResults = Arrays.asList(resultList);
                if (!queriesResults.contains(query))
                {
                    queriesResults.add(0,queries);

                    if (queriesResults.size()>10)
                    {
                        queriesResults.remove(queriesResults.size()-1);
                    }

                    SaveQueryList(queriesResults,preferences);

                }

            }catch (Exception e)
            {
                Log.e("Error",e.getMessage()+"Error");
            }
        }else
            {
                List<String> queriesList = new ArrayList<>();
                queriesList.add(0,query);
                SaveQueryList(queriesList,preferences);
            }

    }
    private void SaveQueryList(List<String> queryList,SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        String gson = new Gson().toJson(queryList.toArray());
        editor.putString("queries",gson);
        editor.apply();
    }
    public List<String> getAllQueries(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        String queries = preferences.getString("queries","");
        return Arrays.asList(new Gson().fromJson(queries,String[].class));

    }
    public void saveVisitedProductIds(Context context,String id)
    {
        SharedPreferences preferences = context.getSharedPreferences("visited",Context.MODE_PRIVATE);
        String gson = preferences.getString("ids","");
        if (!TextUtils.isEmpty(gson))
        {
            List<String> visitedIds = Arrays.asList(new Gson().fromJson(gson,String[].class));
            if (!visitedIds.contains(id))
            {
                visitedIds.add(0,id);
                if (visitedIds.size()>10)
                {
                    visitedIds.remove(visitedIds.size()-1);
                }

                String gsonArray = new Gson().toJson(visitedIds.toArray());
                SaveVisitedIds(gsonArray,preferences);

            }
        }else
            {
                String [] idsArray ={id};
                String gsonArray = new Gson().toJson(idsArray);
                SaveVisitedIds(gsonArray,preferences);
            }
    }
    public List<String> getAllVisitedIds(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("visited",Context.MODE_PRIVATE);
        String gson = preferences.getString("ids","");
        return Arrays.asList(new Gson().fromJson(gson,String[].class));
    }
    private void SaveVisitedIds(String gsonArray, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ids",gsonArray);
        editor.apply();
    }

    public void ClearData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences preferences_session = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_session = preferences_session.edit();
        editor_session.clear();
        editor_session.apply();

        SharedPreferences preferences_search = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_search = preferences_search.edit();
        editor_search.clear();
        editor_search.apply();

        SharedPreferences preferences_visited = context.getSharedPreferences("visited",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_visited = preferences_visited.edit();
        editor_visited.clear();
        editor_visited.apply();


    }
}
