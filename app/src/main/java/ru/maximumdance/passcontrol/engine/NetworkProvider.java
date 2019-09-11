package ru.maximumdance.passcontrol.engine;

import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.App;
import ru.maximumdance.passcontrol.PersonActivity;
import ru.maximumdance.passcontrol.api.PersonApi;
import ru.maximumdance.passcontrol.model.Lesson;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;

public class NetworkProvider {


    public interface CallbackSuccess<T>{
        void call(T t);
    }

    public interface CallbackFail{
        void call(Throwable t);
    }


    PersonApi personApi;

    MutableLiveData<Person> currentPerson;

    public NetworkProvider(PersonApi personApi, MutableLiveData<Person> currentPerson) {
        this.personApi = personApi;
        this.currentPerson = currentPerson;
    }

    public void savePerson(Person person, CallbackSuccess<Person> success, CallbackFail fail){


        if (person.getId()==null) {
            personApi.create(person).enqueue(new PersonCallback(success,fail));
        }
        else {
            personApi.update(person).enqueue(new PersonCallback(success,fail));
        }
    };

    public void addPass(Pass pass, CallbackSuccess<Person> success, CallbackFail fail){


        personApi.addPass(currentPerson.getValue().getId(), pass).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                currentPerson.setValue(response.body());
                success.call(response.body());
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                fail.call(t);
            }
        });

    };

    public void addLesson(Pass pass, Lesson lesson, CallbackSuccess<Person> success, CallbackFail fail){

        personApi.addLesson(pass.getId(), lesson).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                currentPerson.setValue(response.body());
                success.call(response.body());
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                fail.call(t);
            }
        });

    };


    class PersonCallback implements Callback<Person> {

        CallbackSuccess<Person> success;
        CallbackFail fail;

        public PersonCallback(CallbackSuccess success, CallbackFail fail) {
            this.success = success;
            this.fail = fail;
        }

        @Override
        public void onResponse(Call<Person> call, Response<Person> response) {
            if (response.body()!=null){
                currentPerson.setValue(response.body());
                success.call(response.body());
            }
        }
        @Override
        public void onFailure(Call<Person> call, Throwable t) {
            fail.call(t);
        }
    }

 }
