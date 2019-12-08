package com.example.searchengine;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Database.LuceneManager;
import com.example.searchengine.Database.MongoManager;
import com.example.searchengine.Database.MysqlManager;
import com.example.searchengine.Model.Chat;
import com.example.searchengine.Model.Message;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static ArrayList<Message> messageList = new ArrayList<>(); //store message history
    public static ArrayList<Chat> chatList = new ArrayList<>(); //store message history
    public static MongoManager mongoManager;
    public static BruteForce bruteForce;
    public static LuceneManager luceneManager;
    public static MysqlManager mysqlManager;



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




