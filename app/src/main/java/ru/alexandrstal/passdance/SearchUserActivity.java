package ru.alexandrstal.passdance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class SearchUserActivity extends AppCompatActivity {

    private ListView list;

    private final Integer[] imgId = {
            R.drawable.ico_search_user
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        findViewById(R.id.search_user_btn).setOnClickListener(v -> onSearchUser());

        findViewById(R.id.search_user_select_btn).setOnClickListener(v -> onSelectUser());

        findViewById(R.id.scan_user_btn).setOnClickListener(v -> onScanUser());

        list = findViewById(R.id.userlist);

    }

    private void onScanUser() {
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivityForResult(intent, 1);
    }

    private void onSearchUser() {

        String[] itemName = {"Иванов Иван 777"};

        CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId);

        list.setAdapter(adapter);
    }

    private void onSelectUser() {

        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onSelectUser();

    }


}
