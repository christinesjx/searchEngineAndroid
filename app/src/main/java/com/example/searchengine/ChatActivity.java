package com.example.searchengine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchengine.Model.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    Context context;
    LayoutInflater inflater;
    private ArrayList<Message> messageList;
    private MessagesListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view);

        context = getApplicationContext();
        inflater = getLayoutInflater();
        messageList = new ArrayList<>();


        mAdapter = new MessagesListAdapter(this, messageList);
        listViewMessages.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

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

        //messageList.add(message);
        mAdapter.notifyDataSetChanged();
        msg = msg.replace(" ","+");

        //TODO: search(msg) in db
        Log.i("msg", msg);
        mAdapter.appendMessage(message);
    }

    public class MessagesListAdapter extends BaseAdapter {

        private Context context;
        private List<Message> messagesItems;

        public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
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

            TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
            TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

            txtMsg.setText(m.getMessage());

            return convertView;
        }


        private void appendMessage(final Message m) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    messageList.add(m);
                    mAdapter.notifyDataSetChanged();
                    // Playing device's notification
                }
            });
        }

    }

}
