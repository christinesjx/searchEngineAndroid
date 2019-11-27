package com.example.searchengine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class HistoryActivity extends BaseActivity {
    private ListView listViewMessages;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewMessages = (ListView) findViewById(R.id.history_list_view);
        inflater = getLayoutInflater();
        //listViewMessages.setAdapter(mAdapter);

    }


}
