package ru.maximumdance.passcontrol.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.maximumdance.passcontrol.IntentManager;
import ru.maximumdance.passcontrol.model.Person;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context context() {
        return context;
    }

    @Provides
    @Singleton
    public IntentManager intentManager(){
        return new IntentManager(context());
    }



}
