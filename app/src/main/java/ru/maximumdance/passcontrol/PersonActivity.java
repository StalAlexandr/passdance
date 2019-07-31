package ru.maximumdance.passcontrol;

import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maximumdance.passcontrol.model.CourseLevel;
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

    void onPersonSaveSuccess() {
        Toast.makeText(getApplicationContext(), "Пользователь сохранен", Toast.LENGTH_LONG).show();
    }

    private void onPersonSaveFail(Throwable t) {
        Toast.makeText(getApplicationContext(), "Ошибка сохранения: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

        List<String> p = person.getPasses().stream().map(pass -> pass.getCourse().getName() + " " + pass.getCurrentItemCount() + " / " + pass.getItemCount()).collect(Collectors.toList());
        ;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p);
        passesList.setAdapter(adapter);


        passesList.setOnItemClickListener((adapterView, view, i, l) -> {

            CourseLevel[] levels = new CourseLevel[2];
            CourseLevel level = new CourseLevel();
            level.setName("начальный");
            CourseLevel level2 = new CourseLevel();
            level2.setName("продолжающий");
            levels[0] = level;
            levels[1] = level2;

            ArrayAdapter<CourseLevel> courseLevelArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_singlechoice, levels);

            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(courseLevelArrayAdapter, 0, (dialogInterface, i1) -> System.out.println(i1))
                    .setPositiveButton("Списать", (dialog, whichButton) -> {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    })
                    .setNegativeButton("Отмена", (dialog, whichButton) -> {
                        dialog.dismiss();

                    })
                    .show();

        });

    }


}
