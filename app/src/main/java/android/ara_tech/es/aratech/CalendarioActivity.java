package android.ara_tech.es.aratech;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class CalendarioActivity extends AppCompatActivity {
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar cal = Calendar.getInstance();
    private TextView tvDate;
    private Button bDate, bTime, bConfirmar;
    private int año,mes,dia,hora,minutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        tvDate = (TextView) findViewById(R.id.tvDate);
        bDate = (Button) findViewById(R.id.bDate);
        bTime = (Button) findViewById(R.id.bTime);
        bConfirmar = (Button)findViewById(R.id.bConfirmar);

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });

        bTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
            }
        });
        bConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToCalendar(CalendarioActivity.this,dia,mes,año,hora,minutos);
            }
        });

        updateTextLabel();
    }

    private void updateDate(){
        new DatePickerDialog(this,lisDate,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(this,lisTime,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }


    DatePickerDialog.OnDateSetListener lisDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfTheYear, int dayOfTheMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfTheYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfTheMonth);
            año=year;
            mes=monthOfTheYear;
            dia=dayOfTheMonth;
            updateTextLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener lisTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            hora=hourOfDay;
            minutos=minute;
            updateTextLabel();
        }
    };

    private void updateTextLabel(){
        tvDate.setText(formatDateTime.format(cal.getTime()));
    }
    private void addEventToCalendar(Activity activity, int dia, int mes, int año, int hora, int minutos){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, dia);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.YEAR, año);

        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minutos);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.RRULE , "FREQ=DAILY");
        intent.putExtra(CalendarContract.Events.TITLE, "Contratación de Nannys");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Descripción");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,"Calle ....");

        activity.startActivity(intent);
    }
}
