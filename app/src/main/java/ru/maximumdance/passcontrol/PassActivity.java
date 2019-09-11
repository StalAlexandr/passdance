package ru.maximumdance.passcontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    private Calendar launchDate;
    private Calendar expireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        ButterKnife.bind(this);

        App.getAppComponent().aviableCourses().observe(this, value -> {

            CourseAdapter adapter = new CourseAdapter(this,value.toArray(new Course[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            passCourses.setAdapter(adapter);

        });

        List<String> lessons = App.getAppComponent().getLessonCount().stream().map(Object::toString).collect(Collectors.toList());
        ArrayAdapter<String> adapterLesson = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessons);
        adapterLesson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lessonCounts.setAdapter(adapterLesson);

        init();
    }

    private void render(Pass pass) {

        expireDate.setTime(pass.getTerminateDate());
        launchDate.setTime(pass.getLaunchDate());

        setInitialLaunchDate();
        setInitialExpireDate();

    }

    private void init() {

        launchDate = Calendar.getInstance();
        expireDate = Calendar.getInstance();
        expireDate.add(Calendar.MONTH, 1);
        setInitialLaunchDate();
        setInitialExpireDate();

    }

    @OnClick({R.id.passDtexpire})
    public void onSelectExpireDate() {
        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, expireListener,
                expireDate.get(Calendar.YEAR),
                expireDate.get(Calendar.MONTH),
                expireDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @OnClick({R.id.passDtLaunch})
    public void onSelectLaunchDate() {
        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, launchListener,
                launchDate.get(Calendar.YEAR),
                launchDate.get(Calendar.MONTH),
                launchDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialLaunchDate() {
        launchDateView.setText(calendar2Str(launchDate));
    }

    private void setInitialExpireDate() {
        expireDateView.setText(calendar2Str(expireDate));
    }


    @OnClick(R.id.passOk)
    public void onPassOK() {

        Pass pass = new Pass();
        Course selectedCourse = (Course)passCourses.getSelectedItem();
        Integer selectedItemCount = Integer.parseInt(lessonCounts.getSelectedItem().toString());
        pass.setItemCount(selectedItemCount);
        pass.setCourse(selectedCourse);

        pass.setLaunchDate(launchDate.getTime());
        pass.setTerminateDate(expireDate.getTime());

        App.getAppComponent().networkProvider().addPass(pass, this::onPassSaved, this::onPassSaveFail);


    }

    private void onPassSaved(Person person) {
        Toast.makeText(getApplicationContext(), "Абонимент сохранен", Toast.LENGTH_LONG).show();
        App.getAppComponent().currentPerson().setValue(person);
        startActivity(App.getAppComponent().intentManager().onPerson());
    }

    private void onPassSaveFail(Throwable t) {
        Toast.makeText(getApplicationContext(), "Ошибка сохранения: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private String calendar2Str(Calendar cal) {
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return format1.format(date);
    }




    private final DatePickerDialog.OnDateSetListener expireListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            expireDate.set(Calendar.YEAR, year);
            expireDate.set(Calendar.MONTH, monthOfYear);
            expireDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialExpireDate();
        }
    };

    private final DatePickerDialog.OnDateSetListener launchListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            launchDate.set(Calendar.YEAR, year);
            launchDate.set(Calendar.MONTH, monthOfYear);
            launchDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialLaunchDate();
        }
    };

}
