package com.Budgeteer;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//set limit is scrapped at least for now
public class SetLimit extends AppCompatActivity {

    EditText txtLimit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_limit);

        txtLimit = findViewById(R.id.txtlimit);

    }

    public void insert(View view){

        Integer limit = Integer.valueOf(txtLimit.getText().toString());

        Intent intent;
        intent = new Intent(this,MainActivity.class);
        intent.putExtra("limit",limit+"");
        startActivity(intent);
    }

}