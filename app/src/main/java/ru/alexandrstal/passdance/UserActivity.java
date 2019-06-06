package ru.alexandrstal.passdance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UserActivity extends AppCompatActivity {

    private ListView list;

    private final Integer[] imgId = {
            R.drawable.ico_search_user,  R.drawable.ico_search_user

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        findViewById(R.id.add_pass_btn).setOnClickListener(v -> onAddPass());

        findViewById(R.id.edit_pass_btn).setOnClickListener(v -> onEditPass());

        list = findViewById(R.id.userpasslist);

        String[] itemName = {"Латина 01.09.2019-01.10.2019 (5)", "Базовый 15.09.2019-15.10.2019 (5)"};

        CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId);

        list.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void onEditPass() {
        Intent intent = new Intent(this, PassActivity.class);
        startActivityForResult(intent, 1);
    }

    private void onAddPass() {
        Intent intent = new Intent(this, PassActivity.class);
        startActivityForResult(intent, 2);
    }


}
