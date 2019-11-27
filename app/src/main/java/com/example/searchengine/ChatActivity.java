package com.example.searchengine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import com.example.searchengine.Database.AddToMongoDB;
import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Model.Message;


public class ChatActivity extends BaseActivity {

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    Context context;
    LayoutInflater inflater;
    //private MessagesListAdapter mAdapter;

    AddToMongoDB addToMongoDB;
    Mode mode;
    Mode modeSelected;

    BruteForce bruteForce;

    enum Mode {bruteforce, mongoDB, mysql, lucene;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view);

        context = getApplicationContext();
        inflater = getLayoutInflater();
        listViewMessages.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                getReply();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                modeSelected = mode.bruteforce;
                Toast.makeText(getApplicationContext(),"BruteForce Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                modeSelected = mode.mysql;
                Toast.makeText(getApplicationContext(),"MySql Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                modeSelected = mode.lucene;
                Toast.makeText(getApplicationContext(),"lucene Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item4:
                modeSelected = mode.mongoDB;
                Toast.makeText(getApplicationContext(),"MongoDB Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item5:
                addToMongoDB = new AddToMongoDB(context);
                //addToMongoDB.databaseSetUp();
                //addToMongoDB.transferToMongoDB();
                bruteForce = new BruteForce(context);

                Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item6:
                Toast.makeText(getApplicationContext(),"history...",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void sendMessage()
    {
        String msg = inputMsg.getText().toString().trim();

        if( msg.length()==0) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        inputMsg.setText("");

        Message message = new Message();
        message.setSuccess(1);
        message.setMessage(msg);
        message.setSelf(true);

        //history
        mAdapter.notifyDataSetChanged();
        msg = msg.replace(" ","+");

        Log.i("msg", msg);
        mAdapter.appendMessage(message);
    }


    private void getReply(){

        messageList.get(messageList.size()-1).getMessage();

        String msg = "please select a searching system";
//        if(modeSelected == mode.bruteforce){
//
//            msg = bruteForce.searchInFile("activfit","{\"activity\":\"unknown\",\"duration\":41143403}}");
//
//        }

        if(modeSelected == mode.bruteforce){
            bruteForce = new BruteForce(context);
            msg = bruteForce.searchInFile("activfit","\"start_time\":\"Mon Mar 6 12:47:01 EST 2017\"");
        }

        Message message = new Message();
        message.setSuccess(1);
        message.setMessage(msg);
        message.setSelf(false);
        message.setMode(modeSelected);

        mAdapter.notifyDataSetChanged();
        mAdapter.appendMessage(message);
    }


}
