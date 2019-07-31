package ru.maximumdance.passcontrol;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.maximumdance.passcontrol.listadapter.ActionListAdapter;

public class MainActivity extends AppCompatActivity {

    private IntentManager intentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Action> actions = createActions();

        ActionListAdapter adapter = new ActionListAdapter(actions);

        RecyclerView list = findViewById(R.id.listmenu);
        list.setLayoutManager( new LinearLayoutManager(this));
        list.setAdapter(adapter);

        intentManager = App.getAppComponent().intentManager();
    }

    public void onPersonCreate() {
        startActivity(intentManager.onPerson());
    }

    public void onPersonSearch() {
        startActivity(intentManager.onPersonSearch());
    }

    public void onStatistic() {}

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

        return  actions;
    }


}
