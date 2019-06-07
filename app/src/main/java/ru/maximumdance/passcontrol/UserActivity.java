package ru.maximumdance.passcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.alexandrstal.passdance.R;
import ru.maximumdance.passcontrol.model.Person;

public class UserActivity extends AppCompatActivity {

    private ListView list;
    EditText lastName;
    EditText firstName;

    private final Integer[] imgId = {
            R.drawable.ico_search_user,  R.drawable.ico_search_user

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        findViewById(R.id.add_pass_btn).setOnClickListener(v -> onAddPass());

        findViewById(R.id.edit_pass_btn).setOnClickListener(v -> onEditPass());

        lastName  = findViewById(R.id.user_lastname_text);
        firstName  = findViewById(R.id.user_firstname_text);

        list = findViewById(R.id.userpasslist);

        if (getIntent().hasExtra("PERSON")) {
            Person person = getIntent().getParcelableExtra("PERSON");
            lastName.setText(person.getLastName());
            firstName.setText(person.getFirstName());


            List<String> p= new ArrayList<>();
            List<Integer> ico = new ArrayList<>();
            person.getPasses().forEach(pass->{
                p.add(pass.getCourse().getName() + "(" + pass.getCurrentItemCount()+")/" + "("+pass.getItemCount()+")");
                ico.add(R.drawable.ico_search_user);

                CustomListAdapter adapter = new CustomListAdapter(this, p.toArray(new String[p.size()]), ico.toArray(new Integer[ico.size()]));

                list.setAdapter(adapter);


            });

        //    String[] itemName = {"Латина 01.09.2019-01.10.2019 (5)", "Базовый 15.09.2019-15.10.2019 (5)"};

         //   CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId);

          //  list.setAdapter(adapter);

        }






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
