package com.example.searchengine;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.searchengine.Model.Chat;
import com.example.searchengine.Model.Message;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ChatActivity extends MainActivity {

    private MessagesListAdapter mAdapter = new MessagesListAdapter(this, messageList);

    private Button btnSend;
    private MenuItem currentMode;
    private EditText inputMsg;
    private ListView listViewMessages;
    private Mode mode;
    private Mode modeSelected = Mode.bruteforce;

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
                Chat chat = new Chat();
                chat.setMsgFromUser(sendMessage());
                try{
                    chat.setMsgFromChatbot(getReply());
                }catch (Exception e){

                }finally {
                    chatList.add(chat);
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
                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private Message sendMessage()
    {
        String msg = inputMsg.getText().toString().trim().toLowerCase();

        if( msg.length()==0) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
        }

        inputMsg.setText("");

        Message message = new Message();
        message.setSuccess(1);
        message.setMessage(msg);
        message.setSelf(true);

        //history
        mAdapter.notifyDataSetChanged();
        mAdapter.appendMessage(message);

        return message;
    }


    private Message getReply() throws Exception{

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
                msg = mongoManager.search("heartrate;bpm;79");
                if (msg.length() == 0) {
                    msg = "mongodb";
                }

            } else if (modeSelected == mode.lucene) {
                msg = luceneManager.search(lastMessageFromUser);
                if (msg.length() == 0) {
                    msg = "lucene";
                }
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

        return message;
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




    public class MessagesListAdapter extends BaseAdapter {

        private ChatActivity context;
        private List<Message> messagesItems;

        public MessagesListAdapter(ChatActivity context, List<Message> navDrawerItems) {
            this.context = context;
            this.messagesItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return messagesItems.size();
        }

        @Override
        public Object getItem(int position) {
            return messagesItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message m = messagesItems.get(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (messagesItems.get(position).isSelf()) {
                convertView = mInflater.inflate(R.layout.list_item_msg_right, null);
            } else {
                convertView = mInflater.inflate(R.layout.list_item_msg_left, null);
            }

            TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

            txtMsg.setText(m.getMessage());

            return convertView;
        }

        public void appendMessage(final Message m) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    messageList.add(m);
                }
            });
        }

    }
}
