package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.maximumdance.passcontrol.model.Pass;


public class PassActivity extends AppCompatActivity {


    private static final String PASS_KEY = "PASS_KEY";
    @BindView(R.id.passDtLaunch)
    TextView launchDateView;

    @BindView(R.id.passDtexpire)
    TextView expireDateView;

    @BindView(R.id.passCourses)
    Spinner passCourses;


    private Calendar launchDate;
    private Calendar expireDate;

    Pass pass;

    public static Intent createIntent(Activity from, Pass pass) {

        Intent intent = new Intent(from, PassActivity.class);
        if (pass!=null){
            intent.putExtra(PASS_KEY, pass);
        }
        return  intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        ButterKnife.bind(this);

        pass = getIntent().getParcelableExtra(PASS_KEY);

        if (pass == null){
            init();
        } else {
            render();
        }

    }

    private void render() {

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
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
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
