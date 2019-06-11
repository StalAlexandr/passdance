package ru.maximumdance.passcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.model.Person;

public class SearchPersonActivity extends AppCompatActivity {

    @BindView(R.id.searchUserButton)
    Button searchUserButton;

    @BindView(R.id.searchСardNumberText)
    EditText searchСardNumberText;

    @BindView(R.id.searchLastNameText)
    EditText searchLastNameText;

    @BindView(R.id.userList)
    ListView userList;

    @BindString(R.string.wrongSearchParams)
    String wrongSearchParams;

    @BindString(R.string.errorGetPersons)
    String errorGetUsers;

    @BindString(R.string.personsNotFound)
    String usersNotFound;

    private  List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_person);
        ButterKnife.bind(this);


        userList.setOnItemClickListener((parent, view, position, id) -> {

            if (personList.size()>position){
                Intent intent =  PersonActivity.createIntent(this, personList.get(position));
                startActivity(intent);
            }
        });
    }


    @OnClick(R.id.searchUserButton)
    public void onSearchUser() {
      if (searchLastNameText.getText().toString().length()>1){
          findByName(searchLastNameText.getText().toString());
      }

      else {
          if (searchСardNumberText.getText().toString().length()>1){
              findByCard(Integer.parseInt(searchСardNumberText.getText().toString()));
          }
          else {
              Toast.makeText(this.getApplicationContext(), wrongSearchParams, Toast.LENGTH_SHORT).show();
          }
      }

    }

    private void findByCard(Integer cardNumber) {
        App.getApi().getByCardNumber(cardNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {

                if (response.body()==null){
                    Toast.makeText(getApplicationContext(), errorGetUsers,Toast.LENGTH_LONG).show();
                    return;
                }

                personList = Collections.singletonList(response.body());

                List<String> p = personList.stream().map(person-> person.getLastName() + " " + person.getFirstName() + " " +person.getCardNumber()).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
                userList.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "ERROR"  + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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

                List<String> p = personList.stream().map(person-> person.getLastName() + " " + person.getFirstName() + " " +person.getCardNumber()).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
                userList.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Не удалось найти пользователя"  ,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onSelectUser() {

        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onSelectUser();

    }


}
