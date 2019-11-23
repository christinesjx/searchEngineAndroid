package com.example.searchengine.AddToMongoDB;

import android.content.Context;
import android.util.Log;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AddToMongoDB {

    MongoDatabase dbObj;
    Context context;

    public AddToMongoDB(Context context) {
        this.context = context;
    }

    public void transferToMongoDB(){
        String [] list;
        try {
            list = context.getAssets().list("");
            for (String file : list) {
                if(file.contains(".txt")){
                    transferToMongoDB(file);
                }
            }
        } catch (IOException e) {
            Log.i("error","error");
        }
    }


    public void transferToMongoDB(String filename){
        String fileNameStr = filename.split(".txt")[0];

        MongoCollection<Document> col = dbObj.getCollection(fileNameStr);

        System.out.println(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.contains("[")) {
                    mLine = mLine.substring(1);
                } else if (mLine.contains("]")) {
                    mLine = mLine.substring(0, mLine.length() - 1);
                }
                col.insertOne(Document.parse(mLine));
            }

            System.out.println("total docs "+col.countDocuments());

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

    }

    public void databaseSetUp(){

        final StitchAppClient client = Stitch.initializeAppClient("stitch_client_app_id");
        final MongoClient mobileClient = client.getServiceClient(LocalMongoDbService.clientFactory);
        MongoDatabase dbObj = mobileClient.getDatabase("admin");
        this.dbObj = dbObj;

        for (String name : dbObj.listCollectionNames()) {
            System.out.println("Collections inside this db:" + name);
            //dbObj.getCollection(name).deleteMany(new Document());
        }
    }

    public void search(String searchStr){

        String[] strings = searchStr.split(";");
        System.out.println(strings[0]);
        System.out.println(strings[1]);
        System.out.println(strings[2]);

        MongoCollection<Document> col = dbObj.getCollection(strings[0]);

        List<Bson> filters = new ArrayList<>();
        filters.add(eq(strings[1], new BasicDBObject("$regex", strings[2])));

        List<Document> docs = col.find(and(filters)).into(new ArrayList<>());

        for(Document doc: docs){
            System.out.println(doc.toString());
        }

    }


    public boolean processString(String searchField){

        Set<String> notString = new HashSet<>();
        notString.add("bpm");
        notString.add("percent");
        notString.add("step_counts");
        notString.add("step_delta");
        notString.add("charging");

        return notString.contains(searchField);
    }


}
