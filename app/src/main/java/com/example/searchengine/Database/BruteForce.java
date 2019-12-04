package com.example.searchengine.Database;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BruteForce {

    Context context;

    public BruteForce(Context context) {
        this.context = context;
    }

    public String searchInFile(String searchStr) {

        String[] strings = searchStr.split(";");
        String  filename = strings[0];
        String field = strings[1];
        String value = strings[2];
        BufferedReader reader = null;

        StringBuilder sb = new StringBuilder();

        try {
            filename += ".txt";
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.contains("[")) {
                    mLine = mLine.substring(1);
                } else if (mLine.contains("]")) {
                    mLine = mLine.substring(0, mLine.length() - 1);
                }
                if(mLine.contains(field) && mLine.contains(value)){
                    sb.append(mLine + "\n");
                }

            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return sb.toString();
    }
}
