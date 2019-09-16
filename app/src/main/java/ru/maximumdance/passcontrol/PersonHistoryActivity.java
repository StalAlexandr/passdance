package ru.maximumdance.passcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.maximumdance.passcontrol.model.CourseLevel;
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

        lessonsList.setOnItemClickListener((adapterView, view, i, l) -> {



            new AlertDialog.Builder(this)
                    .setPositiveButton("Удалить урок", (dialog, whichButton) -> {
                        dialog.dismiss();

                        Lesson lesson = (Lesson)adapterView.getItemAtPosition(i);
                        App.getAppComponent().networkProvider().removeLesson(lesson, this::onLessonRestore, this::onLessonRestoreFail);

                    })
                    .setNegativeButton("Отмена", (dialog, whichButton) -> {
                        dialog.dismiss();

                    })
                    .show();

        });


    }

    private void onLessonRestoreFail(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Ошибка отмены:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void onLessonRestore(Person person) {
        Toast.makeText(getApplicationContext(), "Урок отменен", Toast.LENGTH_LONG).show();
    }

}
