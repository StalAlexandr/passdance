package ru.maximumdance.passcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import ru.maximumdance.passcontrol.engine.PersonFinderImpl;
import ru.maximumdance.passcontrol.model.Person;

public class PersonSearchActivity extends AppCompatActivity {

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

    private IntentManager intentManager;
    private PersonFinderImpl personFinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_person);
        ButterKnife.bind(this);

        intentManager = App.getAppComponent().intentManager();
        personFinder = App.getAppComponent().personFinder();

        App.getAppComponent().aviablePersons().observe(this, this::render);


        userList.setOnItemClickListener((parent, view, position, id) -> {
           List<Person> people = App.getAppComponent().aviablePersons().getValue();

            if (people.size()>position){
                App.getAppComponent().currentPerson().setValue(people.get(position));
                Intent intent =  intentManager.onPerson();
                startActivity(intent);
            }
        });

    }

    private void render(List<Person> people) {

        List<String> p = people.stream().map(person-> person.getLastName() + " " + person.getFirstName() + " " +person.getCardNumber()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
        userList.setAdapter(adapter);
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
        personFinder.findByCard(cardNumber);
    }

    private void findByName(String name) {
        personFinder.findByName(name);
    }

}
