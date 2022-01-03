package com.Budgeteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTransaction extends AppCompatActivity {

    TextView txtID;
    EditText txtName,txtNominal;
    RadioGroup txtType;
    RadioButton chooseType;
    TransactionDBFunction transactionDBFunction;
    TransactionAdapter adapter;
    RecyclerView rvTransaction;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        transactionDBFunction = new TransactionDBFunction(this);

        txtID=findViewById(R.id.txtID);
        txtName=findViewById(R.id.txtName);
        txtType=findViewById(R.id.txtType);
        txtNominal=findViewById(R.id.txtNominal);


        Intent intent = getIntent();
        txtID.setText(intent.getStringExtra("id"));
        txtName.setText(intent.getStringExtra("name"));
      // chooseType.setText(intent.getStringExtra("type"));
        txtNominal.setText(intent.getStringExtra("nominal"));

    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public void insert(View view){

        //to receive the insert type if it is update or add
        bundle = getIntent().getExtras();
        String insertType = bundle.getString("insertType");

       chooseType = findViewById(txtType.getCheckedRadioButtonId());

        if(insertType.equals("update")){
            update();
        }else if (insertType.equals("add")){
            add();
        }


    }

    public void add(){
        Transaction transaction = new Transaction();
        transaction.name = txtName.getText().toString();
        transaction.nominal = Integer.parseInt(txtNominal.getText().toString());
        transaction.type = chooseType.getText().toString();
        transaction.date = getDate();
        transactionDBFunction.insertTransaction(transaction);

        txtName.setText("");
        chooseType.setText("");
        txtNominal.setText("");

        Intent intent = getIntent();
        intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void update(){
        Transaction transaction = new Transaction();
        try {
            transaction.id = Long.parseLong(txtID.getText().toString());
       }catch (NumberFormatException e){
            System.out.println("not a long");
        }

        transaction.name = txtName.getText().toString();
        transaction.nominal = Integer.parseInt(txtNominal.getText().toString());
        transaction.type = chooseType.getText().toString();
        transaction.date = getDate();


        transactionDBFunction.updateTransaction(transaction);


        txtName.setText("");
        chooseType.setText("");
        txtNominal.setText("");
        txtID.setText("");


        Intent intent = getIntent();
        intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

