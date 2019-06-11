package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.api.PersonApi;
import ru.maximumdance.passcontrol.model.Person;
import ru.maximumdance.passcontrol.model.util.PersonValidationException;
import ru.maximumdance.passcontrol.model.util.PersonValidator;

public class PersonActivity extends AppCompatActivity {


    private static final int SEND_PASS = 0;
    @BindView(R.id.personLastName)
    EditText personLastName;
    @BindView(R.id.personFirstName)
    EditText personFirstName;
    @BindView(R.id.personCard)
    EditText personCard;

    @BindView(R.id.passesList)
    ListView passesList;

    private Person person;

    private static final String PERSON_KEY = "PERSON";

    public static Intent createIntent(Activity from, Person person) {
        Intent intent = new Intent(from, PersonActivity.class);
        intent.putExtra(PERSON_KEY, person);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        person = getIntent().getParcelableExtra(PERSON_KEY);

        render();
    }

    @OnClick(R.id.addPersonButton)
    public void addPerson() {

        bind();

        try {
            PersonValidator.validate(person);
        } catch (PersonValidationException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }


        if (person.getId()==null) {
            App.getApi().create(person).enqueue(new PersonCallback());
        }
        else {
            App.getApi().update(person).enqueue(new PersonCallback());
        }

    }

    @OnClick(R.id.addPass)
    public void onAddPass(){
        Intent intent = PassActivity.createIntent(this, null);
        startActivityForResult(intent, SEND_PASS);
    }

    private void bind() {
        if (person == null) {
            person = new Person();
        }

        person.setFirstName(personFirstName.getText().toString().trim());
       try {
           person.setCardNumber(Integer.parseInt(personCard.getText().toString().trim()));
       } catch (NumberFormatException e){person.setCardNumber(null);}
        person.setLastName(personLastName.getText().toString().trim());

    }

    private void render() {

        if (person == null) {
            return;
        }
        personLastName.setText(person.getLastName());
        personFirstName.setText(person.getFirstName());
        personCard.setText(String.format("%d", person.getCardNumber()));

        List<String> p = person.getPasses().stream().map(pass->pass.getCourse().getName() + " "+ pass.getCurrentItemCount() + " / " + pass.getItemCount()).collect(Collectors.toList());;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
        passesList.setAdapter(adapter);

    }


    class PersonCallback implements Callback<Person> {

        @Override
        public void onResponse(Call<Person> call, Response<Person> response) {
            if (response.body()!=null){
                Toast.makeText(getApplicationContext(), "Пользователь сохранен",Toast.LENGTH_LONG).show();
                person = response.body();
                render();
            }
        }

        @Override
        public void onFailure(Call<Person> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Ошибка сохранения: " + t.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
