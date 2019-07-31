package ru.maximumdance.passcontrol;

import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.model.CourseLevel;
import ru.maximumdance.passcontrol.model.Lesson;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;
import ru.maximumdance.passcontrol.model.util.PersonValidationException;
import ru.maximumdance.passcontrol.model.util.PersonValidator;

public class PersonActivity extends AppCompatActivity {

    @BindView(R.id.personLastName)
    EditText personLastName;
    @BindView(R.id.personFirstName)
    EditText personFirstName;
    @BindView(R.id.personCard)
    EditText personCard;

    @BindView(R.id.passesList)
    ListView passesList;

    private IntentManager intentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        intentManager = App.getAppComponent().intentManager();
        App.getAppComponent().currentPerson().observe(this, this::render);

    }


    @OnClick(R.id.addPersonButton)
    public void addPerson() {

        Person person = bind();

        try {
            PersonValidator.validate(person);
        } catch (PersonValidationException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        App.getAppComponent().networkProvider().savePerson(person, this::onPersonSaveSuccess, this::onPersonSaveFail);

    }


    @OnClick(R.id.addPass)
    public void onAddPass() {
        startActivity(intentManager.onPass());
    }

    private Person bind() {
        Person person = new Person();

        person.setLastName(personLastName.getText().toString().trim());
        person.setFirstName(personFirstName.getText().toString().trim());
        try {
            person.setCardNumber(Integer.parseInt(personCard.getText().toString().trim()));
        } catch (NumberFormatException e) {
            person.setCardNumber(null);
        }
        return person;
    }

    private void render(Person person) {

        if (person == null) {
            return;
        }
        personLastName.setText(person.getLastName());
        personFirstName.setText(person.getFirstName());
        personCard.setText(String.format("%d", person.getCardNumber()));

        ArrayAdapter<Pass> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, person.getPasses());
        passesList.setAdapter(adapter);


        passesList.setOnItemClickListener((adapterView, view, i, l) -> {

            Pass pass = App.getAppComponent().currentPerson().getValue().getPasses().get(i);

            ArrayAdapter<CourseLevel> courseLevelArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_singlechoice, pass.getCourse().getCourseLevels());

            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(courseLevelArrayAdapter, 0, (dialogInterface, i1) -> System.out.println(i1))
                    .setPositiveButton("Списать", (dialog, whichButton) -> {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        CourseLevel level = pass.getCourse().getCourseLevels().get(selectedPosition);
                        Lesson lesson = new Lesson();
                        lesson.setDate(new Date());
                        lesson.setCourseLevel(level);
                        App.getAppComponent().networkProvider().addLesson(pass, lesson, this::onLessonSave, this::onLessonSaveFail);

                    })
                    .setNegativeButton("Отмена", (dialog, whichButton) -> {
                        dialog.dismiss();

                    })
                    .show();

        });

    }

    public void onLessonSave(){
        Toast.makeText(getApplicationContext(), "Урок сохранен", Toast.LENGTH_LONG).show();
    }
    public void onLessonSaveFail(Throwable t){
        Toast.makeText(getApplicationContext(), "Ошибка сохранения урока: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    void onPersonSaveSuccess() {
        Toast.makeText(getApplicationContext(), "Пользователь сохранен", Toast.LENGTH_LONG).show();
    }

    private void onPersonSaveFail(Throwable t) {
        Toast.makeText(getApplicationContext(), "Ошибка сохранения пользователя: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }



}
