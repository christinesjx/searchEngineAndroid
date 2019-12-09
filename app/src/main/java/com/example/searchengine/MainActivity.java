package com.example.searchengine;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Database.LuceneManager;
import com.example.searchengine.Database.MongoManager;
import com.example.searchengine.Database.MysqlManager;
import com.parse.ParseUser;


public class MainActivity extends BaseActivity {

    //private MainActivity context;
    private LoadingDialog loadingDialog;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.context = this;
        loadingDialog = new LoadingDialog(this);

        button = (Button) findViewById(R.id.setup_dbs);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                loadingDialog.showLoadingDialog();
                Toast.makeText(getApplicationContext(), "loading...", Toast.LENGTH_LONG).show();

                try {
                    System.out.println("bruteforce");
                    bruteForce = new BruteForce(MainActivity.this);

                    System.out.println("mongoManager");
                    mongoManager = new MongoManager(MainActivity.this);

                    System.out.println("luceneManager");
                    luceneManager = new LuceneManager(MainActivity.this);
                    luceneManager.createIndex();

                    System.out.println("mysqlManager");
                    mysqlManager = new MysqlManager(MainActivity.this);


                    Toast.makeText(getApplicationContext(), "chatbot...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(i);

                } catch (Exception e) {
                    loadingDialog.dismissLoadingDialog();
                }
                loadingDialog.dismissLoadingDialog();
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
        if(item.equals(R.id.logout)){

            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
