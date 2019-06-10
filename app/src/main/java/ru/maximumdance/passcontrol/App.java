package ru.maximumdance.passcontrol;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.maximumdance.passcontrol.api.PersonApi;

public class App extends Application {

    private Retrofit retrofit;

    private static PersonApi personApi;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://passcontrol.herokuapp.com") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        personApi = retrofit.create(PersonApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }
    public static PersonApi getApi() {
        return personApi;
    }

}
