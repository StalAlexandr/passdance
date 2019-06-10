package ru.maximumdance.passcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.model.Person;

public class UserActivity extends AppCompatActivity {


    @BindView(R.id.userLastName) EditText userLastName;
    @BindView(R.id.userFirstName) EditText userFirstName;
    @BindView(R.id.userCard) EditText userCard;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        Integer personId = getIntent().getIntExtra("PERSON",-1);

        if (personId>0){

            App.getApi().getById(personId).enqueue(new Callback<Person>() {
                @Override
                public void onResponse(Call<Person> call, Response<Person> response) {

                    if (response.body()!=null){
                        person = response.body();
                        userLastName.setText(person.getLastName());
                        userFirstName.setText(person.getFirstName());
                        userCard.setText(String.format("%d",person.getCardNumber()));
                    }


                }

                @Override
                public void onFailure(Call<Person> call, Throwable t) {

                }
            });
        }



    }

@OnClick(R.id.addUserButton)
    public void addUser(){

        Person person = new Person();


    person.setFirstName(userFirstName.getText().toString());
    person.setCardNumber(Integer.parseInt(userCard.getText().toString()));
    person.setLastName(userLastName.getText().toString());


    App.getApi().create(person).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
System.out.println("OK!");
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
System.out.println("ERR " + t.getMessage());
            }
        });

    }


    /*
    *    if (getIntent().hasExtra("PERSON")) {
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

    * */

}
