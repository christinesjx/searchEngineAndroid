package com.example.searchengine;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import com.example.searchengine.Model.Message;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ChatActivity extends MainActivity {

    private MessagesListAdapter mAdapter = new MessagesListAdapter(this, messageList);

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
                return true;
            case R.id.item2:
                modeSelected = mode.mysql;
                Toast.makeText(getApplicationContext(), "MySql Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                modeSelected = mode.lucene;
                Toast.makeText(getApplicationContext(), "Lucene Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item4:
                modeSelected = mode.mongoDB;
                Toast.makeText(getApplicationContext(), "MongoDB Selected", Toast.LENGTH_LONG).show();
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

        String msg = "invalid input";
        long startTime = System.nanoTime();

        if(valid(lastMessageFromUser)) {

            if (modeSelected == null) {
                msg = "please select a searching system";

            } else if (modeSelected == mode.bruteforce) {
                msg = bruteForce.searchInFile(lastMessageFromUser);
                if (msg.length() == 0) {
                    msg = "bruteforce";
                }

            } else if (modeSelected == mode.mysql) {
                msg = mysqlManager.search(lastMessageFromUser);
                if (msg.length() == 0) {
                    msg = "bruteforce";
                }

            } else if (modeSelected == mode.mongoDB) {
                msg = "mongodb";
                //msg = mongoManager.search(lastMessageFromUser);

            } else if (modeSelected == mode.lucene) {
                msg = "lucene";
                msg = luceneManager.search(lastMessageFromUser);
            }
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


    public boolean valid(String str){
        String[] filenames = new String[]{"activfit","heartrate", "batterysensor","bluetooth","activity"};
        Set<String> files = new HashSet<String>(Arrays.asList(filenames));


        String[] strings = str.split(";");
        if(strings.length != 3){
            return false;
        }

        String  filename = strings[0];
        String field = strings[1];
        String value = strings[2];

        if(!files.contains(filename)){
            return false;
        }

        return true;
    }


}
