package ru.maximumdance.passcontrol;

import android.app.AlertDialog;
import android.os.Bundle;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.listadapter.PassAdapter;
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
    RecyclerView passesList;

    @BindView(R.id.addPass)
    View addPass;

    @BindView(R.id.personHistoryButton)
    View personHistory;

    private IntentManager intentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        intentManager = App.getAppComponent().intentManager();
        App.getAppComponent().currentPerson().observe(this, this::render);


        personCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.personCard && !hasFocus) {
                   App.hideKeyboard(PersonActivity.this);
                }
            }
        });


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
        App.getAppComponent().currentPass().setValue(null);
        startActivity(intentManager.onPass());
    }

    private Person bind() {
        Person person =  App.getAppComponent().currentPerson().getValue();
        person.setError(null);

        if (person ==null) {
            person = new Person();
        }

        person.setLastName(personLastName.getText().toString().trim());
        person.setFirstName(personFirstName.getText().toString().trim());
        try {
            person.setCardNumber(Integer.parseInt(personCard.getText().toString().trim()));
        } catch (NumberFormatException e) {
            person.setCardNumber(null);
        }
        return person;
    }


    @OnClick(R.id.personHistoryButton)
    public void onPersonHistory(){
       startActivity(intentManager.onPersonHistory());
    }

    private void render(Person person) {

        personHistory.setVisibility(View.VISIBLE);

        addPass.setVisibility(View.INVISIBLE);

        if (person.getCardNumber() == null) { //новый = нечего рендерить
            return;
        }

        if (person.getLessons().isEmpty()){
            personHistory.setVisibility(View.INVISIBLE);
        }

        addPass.setVisibility(View.VISIBLE);
        personLastName.setText(person.getLastName());
        personFirstName.setText(person.getFirstName());
        personCard.setText(String.format("%d", person.getCardNumber()));
        passesList.setLayoutManager( new LinearLayoutManager(this));

        passesList.setAdapter(new PassAdapter(person.getPasses(), this::onPassClick));

    }

    private void onPassClick(int i, PassAdapter.PassEvent event) {
        if (event.equals(PassAdapter.PassEvent.ADDLESSON)){
            onAddLesson(i);
        }

        if (event.equals(PassAdapter.PassEvent.UPDATE)){
            onUpdatePass(i);
        }

    }

    private void onUpdatePass(int i) {
        Person person =  App.getAppComponent().currentPerson().getValue();
        App.getAppComponent().currentPass().setValue(person.getPasses().get(i));

       startActivity(intentManager.onPass());
    }

    private void onAddLesson(int i) {

        Pass pass = App.getAppComponent().currentPerson().getValue().getPasses().get(i);

        if (!pass.isActive()){
            Toast.makeText(getApplicationContext(), "На абонементе нет уроков или истек срок его действия " , Toast.LENGTH_LONG).show();
            return;
        }

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
    }





    public void onLessonSave(Person person){
        App.getAppComponent().currentPerson().setValue(person);
        Toast.makeText(getApplicationContext(), "Урок сохранен", Toast.LENGTH_LONG).show();
    }
    public void onLessonSaveFail(Throwable t){
        Toast.makeText(getApplicationContext(), "Ошибка сохранения урока: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    void onPersonSaveSuccess(Person person) {

        if (person.getError()!=null){
            Toast.makeText(getApplicationContext(), person.getError(), Toast.LENGTH_LONG).show();
            return;
        }

        App.getAppComponent().currentPerson().setValue(person);
        Toast.makeText(getApplicationContext(), "Пользователь сохранен", Toast.LENGTH_LONG).show();
    }

    private void onPersonSaveFail(Throwable t) {
        Toast.makeText(getApplicationContext(), "Ошибка сохранения пользователя: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }



}
