package com.Budgeteer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTransaction;
    TransactionDBFunction transactionDBFunction;
    TransactionAdapter adapter;
    TextView balanceView,limitView;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balanceView = findViewById(R.id.balanceView);
       // limitView = findViewById(R.id.limitView);

        transactionDBFunction = new TransactionDBFunction(this);

        //to get budget limit(scrapped)
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            String limit = bundle.getString("limit");
            System.out.println(limit);
            limitView.setText(limit+"");
        }

        //to show recyclerview
        if(transactionDBFunction.getAllTransaction().size()>0){
            rvTransaction = findViewById(R.id.rvtransaction);
            rvTransaction.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TransactionAdapter(this,transactionDBFunction.getAllTransaction());
            rvTransaction.setAdapter(adapter);


            calculateBal();


        }


    }

    //to calculate balance
    public void calculateBal(){
        Integer temp = 0;
        for(Transaction tr : transactionDBFunction.getAllTransaction()){
            if(tr.type.equals("Income")){
                //jika income
                temp += tr.nominal;
            }else if(tr.type.equals("Outcome") && temp>tr.nominal){
                //jika outcome
                temp -=tr.nominal;
            }else{

                // jika outcome melebihi yang ada di balance
                Intent intent;
                intent =  new Intent(this,MainActivity.class);
                transactionDBFunction= new TransactionDBFunction(this);
                transactionDBFunction.deleteTransaction(tr.id);
                Toast.makeText(getBaseContext(), "limit reached", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }

        }
        balanceView.setText(temp+"");
    }


    //to go to addtransaction activity
    public void add(View view){
        Intent intent;
        intent = new Intent(this,AddTransaction.class);
        intent.putExtra("insertType","add");
        startActivity(intent);
    }

    // to go to set limit activity(scrapped for now)
    public void setlimit(View view){
        Intent intent;
        intent = new Intent(this,SetLimit.class);
        startActivity(intent);
    }

    //to go to history activity
    public void history(View view){
        Intent intent;
        intent = new Intent(this,History.class);
        startActivity(intent);
    }

}