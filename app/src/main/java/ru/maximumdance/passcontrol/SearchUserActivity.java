package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.alexandrstal.passdance.R;
import ru.maximumdance.passcontrol.client.PersonClient;
import ru.maximumdance.passcontrol.model.Person;

public class SearchUserActivity extends AppCompatActivity {


    @BindView(R.id.searchUserButton)
    Button searchUserButton;

    private ListView list;

    private EditText cardNumber;

    private final Integer[] imgId = {
            R.drawable.ico_search_user
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

     //   findViewById(R.id.searchUserButton).setOnClickListener(v -> onSearchUser());

    //    findViewById(R.id.search_user_select_btn).setOnClickListener(v -> onSelectUser());



        findViewById(R.id.scan_user_btn).setOnClickListener(v -> onScanUser());

        cardNumber = findViewById(R.id.search_user_card_text);

        //search_user_card_text

        list = findViewById(R.id.userlist);
        ButterKnife.bind(this);
      //  searchUserButton.setText("123");

    }

    Activity getThis(){return  this;};

    private void onScanUser() {
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(value = R.id.searchUserButton)
    public void onSearchUser() {
        Toast.makeText(this.getApplicationContext(), "YES", Toast.LENGTH_SHORT);
        new SearchUserTask(cardNumber.getText().toString()).execute();

      //  String[] itemName = {"Иванов Иван 777"};

       // CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId);

      //  list.setAdapter(adapter);
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


    private class SearchUserTask extends AsyncTask<Void, Void, Person> {

        String cardNumber;

        public SearchUserTask(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        @Override
        protected Person doInBackground(Void... params) {
           return new PersonClient().getByCardNumber(cardNumber);
        }

        @Override
        protected void onPostExecute(Person  person) {

            if (person==null){
                return;
            }

            onPersonSelect(person);
          //  String[] itemName = {person.getFirstName()};
          //  CustomListAdapter adapter = new CustomListAdapter(getThis(), itemName, imgId);

          //  list.setAdapter(adapter);
        }

    }

    private void onPersonSelect(Person person) {

        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("PERSON", person);
        startActivity(intent);

    }


}
