package ru.maximumdance.passcontrol;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private final Integer[] imgId = {
            R.drawable.ico_search_user,
            R.drawable.ico_create_user,
            R.drawable.ico_lessons
    };

    private int state = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] itemName = new String[3];
        itemName[0] = getResources().getString(R.string.menu_search_user);
        itemName[1] = getResources().getString(R.string.menu_create_user);
        itemName[2] = getResources().getString(R.string.menu_lessons);

        CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId);
        ListView list;
        list = findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                state = (int) id;
                if (id == 0) {
                    onUserSearchClick();
                }
                if (id == 1) {
                    onUserAddClick();
                }
                if (id == 2) {
                    onLessonClick();
                }
            }

        });

    }

    private void onLessonClick() {

      //  new HttpRequestTask().execute();
    }

    public void onUserSearchClick() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }


    public void onUserAddClick() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

}
