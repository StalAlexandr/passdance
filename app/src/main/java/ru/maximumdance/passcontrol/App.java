package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.maximumdance.passcontrol.dagger.AppComponent;
import ru.maximumdance.passcontrol.dagger.AppModule;
import ru.maximumdance.passcontrol.dagger.DaggerAppComponent;
import ru.maximumdance.passcontrol.dagger.NetworkModule;

public class App extends Application {


    private static AppComponent component;

    private final String baseUrl = "https://passcontrol.herokuapp.com";
  //  private final String baseUrl = "http://10.0.2.2:8080";

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


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
