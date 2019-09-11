package ru.maximumdance.passcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.maximumdance.passcontrol.model.Lesson;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;

public class PersonHistoryActivity extends AppCompatActivity {

    @BindView(R.id.lessonsList)
    ListView lessonsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_history);
        ButterKnife.bind(this);

        App.getAppComponent().currentPerson().observe(this, this::render);
    }

    private void render(Person person) {



        ArrayAdapter<Lesson> adapter = new ArrayAdapter<Lesson>(this, android.R.layout.simple_list_item_1, person.getLessons());
        lessonsList.setAdapter(adapter);



    }
}
