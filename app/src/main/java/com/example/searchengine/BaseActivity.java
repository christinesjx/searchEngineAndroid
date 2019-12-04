package com.example.searchengine;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Database.LuceneManager;
import com.example.searchengine.Database.MongoManager;
import com.example.searchengine.Database.MysqlManager;
import com.example.searchengine.Model.Message;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static ArrayList<Message> messageList = new ArrayList<>(); //store message history

    public static MongoManager mongoManager;
    public static BruteForce bruteForce;
    public static LuceneManager luceneManager;
    public static MysqlManager mysqlManager;


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



    public class HistoryMessagesListAdapter extends BaseAdapter {

        private HistoryActivity context;
        private List<Message> messagesItems;

        public HistoryMessagesListAdapter(HistoryActivity context, List<Message> navDrawerItems) {
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

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (messagesItems.get(position).isSelf()) {
                convertView = mInflater.inflate(R.layout.list_item_msg_right, null);
                TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
                txtMsg.setText(m.getMessage());

            } else {
                convertView = mInflater.inflate(R.layout.list_item_msg_left, null);
                TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
                txtMsg.setText(m.getMode() + " " + m.getMessage() + " time:" + m.getExecutionTime() + "ms");
            }

            return convertView;
        }

    }



    public class LoadingDialog {

        Activity activity;
        private AlertDialog alertDialog;

        public LoadingDialog(Activity activity) {
            this.activity = activity;
        }

        public void showLoadingDialog() {
            alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
            alertDialog.setCancelable(false);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK);
                }
            });
            alertDialog.show();
            alertDialog.setContentView(R.layout.view_loading);
            alertDialog.setCanceledOnTouchOutside(false);
        }



        public void dismissLoadingDialog() {
            if (null != alertDialog && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }


    }




}




