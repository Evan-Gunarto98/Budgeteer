package com.Budgeteer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTransaction;
    TransactionDBFunction transactionDBFunction;
    TransactionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionDBFunction = new TransactionDBFunction(this);

        if(transactionDBFunction.getAllTransaction().size()>0){
            rvTransaction = findViewById(R.id.rvtransaction);
            rvTransaction.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TransactionAdapter(this,transactionDBFunction.getAllTransaction());
            rvTransaction.setAdapter(adapter);
        }



    }

    public void navigation(View view){
        Intent intent;
        intent = new Intent(this,AddTransaction.class);
        intent.putExtra("insertType","add");
        startActivity(intent);
    }
}