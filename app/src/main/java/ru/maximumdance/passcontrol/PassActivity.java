package ru.maximumdance.passcontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.maximumdance.passcontrol.listadapter.CourseAdapter;
import ru.maximumdance.passcontrol.model.Course;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;


public class PassActivity extends AppCompatActivity {


    @BindView(R.id.passDtLaunch)
    TextView launchDateView;

    @BindView(R.id.passDtexpire)
    TextView expireDateView;

    @BindView(R.id.passCourses)
    Spinner passCourses;


    @BindView(R.id.lessonCounts)
    Spinner lessonCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        ButterKnife.bind(this);


        App.getAppComponent().currentPass().observe(this, this::observePass);

        App.getAppComponent().aviableCourses().observe(this, value -> {

            CourseAdapter adapter = new CourseAdapter(this,value.toArray(new Course[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            passCourses.setAdapter(adapter);

        });

        List<String> lessons = App.getAppComponent().getLessonCount().stream().map(Object::toString).collect(Collectors.toList());
        ArrayAdapter<String> adapterLesson = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessons);
        adapterLesson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lessonCounts.setAdapter(adapterLesson);

      //  init();
    }

    private void observePass(Pass pass) {
        if (pass == null){
            pass = new Pass();
            pass.setLaunchDate(new Date());
            pass.setTerminateDate(calcTerminateDate(pass.getLaunchDate()));
            App.getAppComponent().currentPass().setValue(pass);
           return;
        }

        launchDateView.setText(calendar2Str(pass.getLaunchDate()));
        expireDateView.setText(calendar2Str(pass.getTerminateDate()));

    }


    private Date calcTerminateDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTime();
    }

    @OnClick({R.id.passDtexpire})
    public void onSelectExpireDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(App.getAppComponent().currentPass().getValue().getTerminateDate());

        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, expireListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @OnClick({R.id.passDtLaunch})
    public void onSelectLaunchDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(App.getAppComponent().currentPass().getValue().getLaunchDate());

        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, launchListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }



    @OnClick(R.id.passOk)
    public void onPassOK() {

        Pass pass = App.getAppComponent().currentPass().getValue();
        if (pass==null){
            pass = new Pass();
        }

        Course selectedCourse = (Course)passCourses.getSelectedItem();
        Integer selectedItemCount = Integer.parseInt(lessonCounts.getSelectedItem().toString());
        pass.setItemCount(selectedItemCount);
        pass.setCourse(selectedCourse);

        App.getAppComponent().networkProvider().addPass(pass, this::onPassSaved, this::onPassSaveFail);


    }

    private void onPassSaved(Person person) {
        Toast.makeText(getApplicationContext(), "Абонемент сохранен", Toast.LENGTH_LONG).show();
        App.getAppComponent().currentPerson().setValue(person);
        startActivity(App.getAppComponent().intentManager().onPerson());
    }

    private void onPassSaveFail(Throwable t) {
        Toast.makeText(getApplicationContext(), "Ошибка сохранения: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private String calendar2Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return format.format(date);
    }




    private final DatePickerDialog.OnDateSetListener expireListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear, dayOfMonth);

            Pass pass = App.getAppComponent().currentPass().getValue();
            pass.setTerminateDate(calendar.getTime());
            App.getAppComponent().currentPass().setValue(pass);
        }
    };

    private final DatePickerDialog.OnDateSetListener launchListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear, dayOfMonth);

            Pass pass = App.getAppComponent().currentPass().getValue();
            pass.setLaunchDate(calendar.getTime());
            App.getAppComponent().currentPass().setValue(pass);
        }
    };

}
