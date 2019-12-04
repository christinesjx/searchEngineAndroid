package com.example.searchengine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.searchengine.Database.LuceneManager;
import com.example.searchengine.Database.MongoManager;
import com.example.searchengine.Database.MysqlManager;
import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Model.Message;


public class ChatActivity extends BaseActivity {

    private Context context;
    private LayoutInflater inflater;
    private MongoManager mongoManager;
    private BruteForce bruteForce;
    private LuceneManager luceneManager;
    private MysqlManager mysqlManager;

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private Mode mode;
    private Mode modeSelected;

    enum Mode {bruteforce, mongoDB, mysql, lucene}

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
                try{
                    getReply();

                }catch (Exception e){

                }
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
        switch (id) {
            case R.id.item1:
                modeSelected = mode.bruteforce;
                Toast.makeText(getApplicationContext(), "BruteForce Selected", Toast.LENGTH_LONG).show();
                bruteForce = new BruteForce(context);
                return true;
            case R.id.item2:
                modeSelected = mode.mysql;
                Toast.makeText(getApplicationContext(), "MySql Selected", Toast.LENGTH_LONG).show();
                try {
                    mysqlManager = new MysqlManager(context);
                } catch (Exception e) {}
                return true;
            case R.id.item3:
                modeSelected = mode.lucene;
                Toast.makeText(getApplicationContext(), "Lucene Selected", Toast.LENGTH_LONG).show();
                try {
                    luceneManager = new LuceneManager(context);
                    luceneManager.createIndex();
                } catch (Exception e) {}
                return true;
            case R.id.item4:
                modeSelected = mode.mongoDB;
                Toast.makeText(getApplicationContext(), "MongoDB Selected", Toast.LENGTH_LONG).show();
                mongoManager = new MongoManager(context);
                return true;
            case R.id.item5:
                Toast.makeText(getApplicationContext(), "History...", Toast.LENGTH_LONG).show();
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
        mAdapter.appendMessage(message);
    }


    private void getReply() throws Exception{

        String lastMessageFromUser = messageList.get(messageList.size()-1).getMessage();

        String msg = "";

        long startTime = System.nanoTime();


        if(modeSelected == null){
            msg = "please select a searching system";

        }else if(modeSelected == mode.bruteforce){
            msg = bruteForce.searchInFile(lastMessageFromUser);

        }else if(modeSelected == mode.mysql){
            msg = mysqlManager.search(lastMessageFromUser);

        }else if(modeSelected == mode.mongoDB){
            msg = mongoManager.search(lastMessageFromUser);

        }else if(modeSelected == mode.lucene){
            msg = luceneManager.search(lastMessageFromUser);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        Message message = new Message();
        message.setSuccess(1);
        message.setMessage(msg);
        message.setSelf(false);
        message.setMode(modeSelected);
        message.setExecutionTime(duration);

        mAdapter.notifyDataSetChanged();
        mAdapter.appendMessage(message);
    }


}
