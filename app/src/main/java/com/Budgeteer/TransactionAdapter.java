package com.Budgeteer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter  extends RecyclerView.Adapter <TransactionAdapter.ViewHolder> {

    Context context;
    ArrayList<Transaction> listTransaction;

    public TransactionAdapter(Context context, ArrayList<Transaction> listTransaction) {
        this.context = context;
        this.listTransaction = listTransaction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_transaction,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = listTransaction.get(position);
        holder.transactionName.setText(transaction.name);
        holder.transactionNominal.setText(String.valueOf(transaction.nominal));
        holder.transactionType.setText(transaction.type);
        holder.transactionDate.setText(String.valueOf(transaction.date));

    }

    @Override
    public int getItemCount() {
        return listTransaction.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        public TextView transactionName;
        public TextView transactionType;
        public TextView transactionNominal;
        public TextView transactionDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.transactionName = itemView.findViewById(R.id.transactionName);
            this.transactionType = itemView.findViewById(R.id.transactionType);
            this.transactionNominal = itemView.findViewById(R.id.transactionNominal);
            this.transactionDate = itemView.findViewById(R.id.transactionDate);
            itemView.setOnClickListener(this);
//            date = itemView.findViewById(R.id.transactionName);
        }

        @Override
        public void onClick(View v){
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_product,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = getAdapterPosition();
                   Transaction transaction = listTransaction.get(position);

                    TransactionDBFunction transactionDBFunction;
                    Intent intent;


                    switch(item.getItemId()){
                        case R.id.menu_edit:
                            intent = new Intent(context,AddTransaction.class);
                            intent.putExtra("insertType","update");
                            intent.putExtra("id",transaction.id+"");
                            intent.putExtra("name",transaction.name+"");
                            intent.putExtra("type",transaction.type+"");
                            intent.putExtra("nominal",transaction.nominal+"");
                            intent.putExtra("date",transaction.date+"");
                            System.out.println("the id is "+transaction.id+"");
                            context.startActivity(intent);
                            return true;

                        case R.id.menu_delete:
                            intent =  new Intent(context,MainActivity.class);
                            transactionDBFunction= new TransactionDBFunction(context);
                            transactionDBFunction.deleteTransaction(transaction.id);
                            context.startActivity(intent);
                            return true;

                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}
