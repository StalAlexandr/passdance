package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.client.PersonClient;
import ru.maximumdance.passcontrol.model.Person;

public class SearchUserActivity extends AppCompatActivity {

    @BindView(R.id.searchUserButton)
    Button searchUserButton;

    @BindView(R.id.searchСardNumberText)
    EditText searchСardNumberText;

    @BindView(R.id.searchLastNameText)
    EditText searchLastNameText;

    @BindView(R.id.userlist)
    ListView userlist;

    @BindString(R.string.wrongSearchParams)
    String wrongSearchParams;

    @BindString(R.string.errorGetUsers)
    String errorGetUsers;

    @BindString(R.string.usersNotFound)
    String usersNotFound;

    private  List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);


        userlist.setOnItemClickListener((parent, view, position, id) -> {

            if (personList.size()>position){
                Integer personId = personList.get(position).getId();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("PERSON", personId);
                startActivity(intent);

            }

        });

    }





    @OnClick(R.id.searchUserButton)
    public void onSearchUser() {
        Toast.makeText(this.getApplicationContext(), "SEARCH", Toast.LENGTH_SHORT);
      if (searchLastNameText.getText().toString().length()>1){
          findByName(searchLastNameText.getText().toString());
      }

      else {
          if (searchСardNumberText.getText().toString().length()>1){
              findByCard(searchСardNumberText.getText().toString());
          }
          else {
              Toast.makeText(this.getApplicationContext(), wrongSearchParams, Toast.LENGTH_SHORT);
          }
      }

    }

    private void findByCard(String cardNumber) {
    }

    private void findByName(String name) {

        App.getApi().getByName(name).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {

                if (response.body()==null){
                    Toast.makeText(getApplicationContext(), errorGetUsers,Toast.LENGTH_LONG).show();
                    return;
                }

                personList = response.body();

                if (personList.isEmpty()){
                    Toast.makeText(getApplicationContext(), usersNotFound,Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> p = personList.stream().map(person-> person.getFirstName() + " " + person.getCardNumber()).collect(Collectors.toList());


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
                userlist.setAdapter(adapter);


                //Toast.makeText(getApplicationContext(), response.body().toString(),Toast.LENGTH_SHORT);
                //Данные успешно пришли, но надо проверить response.body() на null
            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR"  + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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


    private void onPersonSelect(Person person) {



        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("PERSON", person);
        startActivity(intent);

    }


}
