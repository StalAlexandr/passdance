package ru.maximumdance.passcontrol.dagger;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.maximumdance.passcontrol.api.CourseApi;
import ru.maximumdance.passcontrol.api.PersonApi;
import ru.maximumdance.passcontrol.engine.NetworkProvider;
import ru.maximumdance.passcontrol.engine.PersonFinderImpl;
import ru.maximumdance.passcontrol.model.Course;
import ru.maximumdance.passcontrol.model.Person;

@Module
public class NetworkModule {

    private Retrofit retrofit;

    MutableLiveData<List<Course>> courses;


    public NetworkModule(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
    }

    @Singleton
    @Provides
    public List<Integer> getLessonCount(){

        List<Integer> counts = new ArrayList<>();
        counts.add(1);
        counts.add(4);
        counts.add(8);
        counts.add(12);
        counts.add(16);
        return counts;
    }

    @Singleton
    @Provides
    public CourseApi getCourseApi(){
        return retrofit.create(CourseApi.class);
    }

    @Singleton
    @Provides
    public PersonApi getPersonApi(){
        return retrofit.create(PersonApi.class);
    }


    @Provides
    @Singleton
    public LiveData<List<Course>> getCourses(CourseApi courseApi) {
        if (courses == null) {
            courses = new MutableLiveData<>();
            loadData(courseApi);
        }
        return courses;
    }

    @Provides
    @Singleton
    public MutableLiveData<List<Person>> aviablePersons(){
        return new MutableLiveData<>();
    }

    @Provides
    @Singleton
    MutableLiveData<Person> currentPerson(){
        return new MutableLiveData<>();
    }

    @Provides
    @Singleton
    public PersonFinderImpl personFinder(MutableLiveData<List<Person>> data, PersonApi personApi){
        return new PersonFinderImpl(data,personApi);
    }

    @Provides
    @Singleton
    NetworkProvider networkProvider(PersonApi personApi, MutableLiveData<Person> currentPerson){
        return new NetworkProvider(personApi, currentPerson);
    }



    private void loadData(CourseApi courseApi) {
        courseApi.getAll().enqueue(new CourseCallback());
    }

    class CourseCallback implements Callback<List<Course>> {

        @Override
        public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
            courses.setValue(response.body()); ;
        }

        @Override
        public void onFailure(Call<List<Course>> call, Throwable t) {
            courses.setValue(new ArrayList<>());
        }
    }




}