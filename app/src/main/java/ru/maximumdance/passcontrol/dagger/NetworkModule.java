package ru.maximumdance.passcontrol.dagger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();



        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic("user", "maximum"));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //Базовая часть адреса
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
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
