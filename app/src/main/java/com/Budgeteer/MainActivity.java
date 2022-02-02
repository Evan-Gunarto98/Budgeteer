package com.Budgeteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTransaction;
    TransactionDBFunction transactionDBFunction;
    TransactionAdapter adapter;
    TextView balanceView,limitView;
    BottomNavigationView bottomNavigation;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ads
//        HwAds.init(this);
//        BannerView bannerView = findViewById(R.id.hw_banner_view);
//        bannerView.setBannerRefresh(60);
//        AdParam adParam = new AdParam.Builder().build();
//        bannerView.loadAd(adParam);

        //nav
        bottomNavigation = findViewById(R.id.bottomNav);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                switch(item.getItemId())
                {
                    case R.id.history:
                        intent = new Intent(MainActivity.this, History.class);
                        break;

                    case R.id.add:
                        intent = new Intent(MainActivity.this, AddTransaction.class);
                        intent.putExtra("insertType","add");
                        break;

                    case R.id.account:
                        intent = new Intent(MainActivity.this, QuickStartActivity.class);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                startActivity(intent);

                return false;
            }
        });

        bottomNavigation.getMenu().getItem(0).setChecked(false);
        bottomNavigation.getMenu().getItem(1).setChecked(true);

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


    public void account(View view) {
        Intent intent;
        intent = new Intent(this, QuickStartActivity.class);
        startActivity(intent);
    }
}