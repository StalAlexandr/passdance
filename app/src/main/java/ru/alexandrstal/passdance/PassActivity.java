package ru.alexandrstal.passdance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PassActivity extends AppCompatActivity {

    private TextView launchDateView;
    private Calendar launchDate;
    private TextView expireDateView;
    private Calendar expireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);


        findViewById(R.id.pass_ok).setOnClickListener(v -> onPassOK());

        launchDate = Calendar.getInstance();
        launchDateView = findViewById(R.id.pass_dtlaunch_text);

        expireDate = Calendar.getInstance();
        expireDateView = findViewById(R.id.pass_dtexpire_text);

        setInitialBirthDate();
        setInitialIssuedDate();

        findViewById(R.id.pass_dtlaunch_text).setOnClickListener(v->onSelectLaunchDate());
        findViewById(R.id.pass_dtexpire_text).setOnClickListener(v->onSelectExpireDate());

    }

    private void onSelectExpireDate() {
        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, expireListener,
                expireDate.get(Calendar.YEAR),
                expireDate.get(Calendar.MONTH),
                expireDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void onSelectLaunchDate() {
        new DatePickerDialog(PassActivity.this, R.style.DatePickerDialog, launchListener,
                launchDate.get(Calendar.YEAR),
                launchDate.get(Calendar.MONTH),
                launchDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialBirthDate() {
        launchDateView.setText(calendar2Str(launchDate));
    }

    private void setInitialIssuedDate() {
        expireDateView.setText(calendar2Str(expireDate));
    }


    private void onPassOK() {
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
            setInitialIssuedDate();
        }
    };

    private final DatePickerDialog.OnDateSetListener launchListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            launchDate.set(Calendar.YEAR, year);
            launchDate.set(Calendar.MONTH, monthOfYear);
            launchDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialBirthDate();
        }
    };

}
