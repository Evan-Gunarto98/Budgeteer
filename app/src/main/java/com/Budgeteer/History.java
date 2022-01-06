package com.Budgeteer;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class History extends AppCompatActivity {

    RecyclerView rvTransactionHistory;
    TransactionDBFunction transactionDBFunction;
    DatePicker datePicker;
    TextView datee;
    TransactionAdapter adapter;
    String date1;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        transactionDBFunction = new TransactionDBFunction(this);

        datePicker =  findViewById(R.id.datePicker);
        datee = findViewById(R.id.datee);
        int day = datePicker.getDayOfMonth();

        DatePicker datePicker =  findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();


        date1 = (datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear());
        showtransaction(calendar);

        //to get date everytime the date is changed
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);


                calendar.set(year, month, dayOfMonth);
                showtransaction(calendar);



            }
        });


    }

    //to show the recyclerview
    public void showtransaction(Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
        date1 = format.format(calendar.getTime());
        datee.setText(date1);

        if(transactionDBFunction.getAllTransaction().size()>0) {
            rvTransactionHistory = findViewById(R.id.rvTransactionHistory);
            rvTransactionHistory.setLayoutManager(new LinearLayoutManager(History.this));
            adapter = new TransactionAdapter(History.this, transactionDBFunction.getAllTransactionbyDate(date1));
            rvTransactionHistory.setAdapter(adapter);
        }
    }



}