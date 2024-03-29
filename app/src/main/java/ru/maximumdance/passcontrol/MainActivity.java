package ru.maximumdance.passcontrol;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.maximumdance.passcontrol.listadapter.ActionListAdapter;
import ru.maximumdance.passcontrol.model.Person;

public class MainActivity extends AppCompatActivity {

    private IntentManager intentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Action> actions = createActions();

        ActionListAdapter adapter = new ActionListAdapter(actions);

        RecyclerView list = findViewById(R.id.listmenu);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        intentManager = App.getAppComponent().intentManager();
    }

    public void onPersonCreate() {
        App.getAppComponent().currentPerson().setValue(new Person());
        startActivity(intentManager.onPerson());
    }

    public void onPersonSearch() {
        App.getAppComponent().aviablePersons().setValue(Collections.emptyList());
        startActivity(intentManager.onPersonSearch());
    }

    public void onStatistic() {
    }

    private List<Action> createActions() {


        List<Action> actions = new ArrayList<>();

        actions.add(new Action(Action.Code.CREATEPERSON,
                getResources().getString(R.string.menu_create_person),
                R.drawable.ico_create_person, null,
                this::onPersonCreate));


        actions.add(new Action(Action.Code.SEARCHPERSON,
                getResources().getString(R.string.menu_search_person),
                R.drawable.ico_search_person, null,
                this::onPersonSearch));


        actions.add(new Action(Action.Code.STAT,
                getResources().getString(R.string.menu_stat),
                R.drawable.ico_statistic, null,
                this::onStatistic));

        return actions;
    }


}
