package com.example.searchengine.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class AddToMysql {


    private SQLiteDatabase mydatabase;
    private Context context;

    public AddToMysql(Context context) {
        this.context = context;
       // MysqlDatabaseHelper myDb = new MysqlDatabaseHelper(context);
    }

    public void transferToMysql() throws Exception{
        String [] list;
        try {
            list = context.getAssets().list("");
            for (String file : list) {
                if(file.contains(".txt")){
                    System.out.println(file);
                    //transferToMysql(file);
                }
            }
        } catch (IOException e) {
            Log.i("error","error");
        }
    }

    public void transferToMysql(String filename) {

        try {
            String jsonStr = convertStreamToString(filename);
            JSONArray jsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                System.out.println(c.toString());
            }
        }catch (Exception e){

        }




    }

    public String convertStreamToString(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }




}
