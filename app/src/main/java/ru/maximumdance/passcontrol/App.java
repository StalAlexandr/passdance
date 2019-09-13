package ru.maximumdance.passcontrol;

import android.app.Application;

import com.google.gson.Gson;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.maximumdance.passcontrol.api.PersonApi;
import ru.maximumdance.passcontrol.dagger.AppComponent;
import ru.maximumdance.passcontrol.dagger.AppModule;
import ru.maximumdance.passcontrol.dagger.DaggerAppComponent;
import ru.maximumdance.passcontrol.dagger.NetworkModule;
import ru.maximumdance.passcontrol.model.Lesson;

public class App extends Application {


    private static AppComponent component;

 //   private final String baseUrl = "https://passcontrol.herokuapp.com";
  private final String baseUrl = "http://10.0.2.2:8080";

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(baseUrl))
                .appModule(new AppModule(this))
                .build();

    }
    public static AppComponent getAppComponent() {
        return component;
    }

}
