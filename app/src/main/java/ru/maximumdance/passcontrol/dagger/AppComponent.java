package ru.maximumdance.passcontrol.dagger;

import java.util.List;

import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dagger.Component;
import ru.maximumdance.passcontrol.IntentManager;
import ru.maximumdance.passcontrol.engine.NetworkProvider;
import ru.maximumdance.passcontrol.engine.PersonFinderImpl;
import ru.maximumdance.passcontrol.model.Course;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    IntentManager intentManager();

    NetworkProvider networkProvider();

    MutableLiveData<Person> currentPerson();

    MutableLiveData<Pass> currentPass();

    MutableLiveData<List<Person>> aviablePersons();

    LiveData<List<Course>> aviableCourses();

    PersonFinderImpl personFinder();

    List<Integer> getLessonCount();


}
