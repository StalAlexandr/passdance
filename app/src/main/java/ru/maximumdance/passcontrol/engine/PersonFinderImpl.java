package ru.maximumdance.passcontrol.engine;

import android.arch.lifecycle.MutableLiveData;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.api.PersonApi;
import ru.maximumdance.passcontrol.model.Person;

public class PersonFinderImpl {

    MutableLiveData<List<Person>> liveData;
    PersonApi personApi;

    public PersonFinderImpl(MutableLiveData<List<Person>> liveData, PersonApi personApi) {
        this.liveData = liveData;
        this.personApi = personApi;
    }

    public void findByCard(Integer cardNumber) {
        personApi.getByCardNumber(cardNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {

                if (response.body()!=null){
                    liveData.setValue(Collections.singletonList(response.body()));
                } else{
                    liveData.setValue(Collections.emptyList());
                }

            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
               throw new RuntimeException(t);
            }
        });
    }

    public void findByName(String name) {

        personApi.getByName(name).enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {

                if (response.body()!=null){
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });

    }

}
