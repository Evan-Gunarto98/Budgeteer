package com.Budgeteer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class TransactionDBFunction {
    DBHelper dbhelper;

    public TransactionDBFunction(Context context){

        dbhelper = new DBHelper(context,"DB-LYO1",null,1);
    }

    public void insertTransaction (Transaction transaction){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",transaction.name);
        cv.put("type",transaction.type);
        cv.put("nominal",transaction.nominal);
        cv.put("date", String.valueOf(transaction.date));

        db.insert("msproduct",null,cv);
        db.close();
    }

    public void updateTransaction (Transaction transaction){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",transaction.name);
        cv.put("type",transaction.type);
        cv.put("nominal",transaction.nominal);
        cv.put("date", String.valueOf(transaction.date));

        String[] key = {transaction.id+""};
        db.update("msproduct",cv,"id=?",key);
        db.close();
    }

    public void deleteTransaction (long id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();


        String[] key = {id+""};
        db.delete("msproduct","id=?",key);
        db.close();
    }

    //get all transaction data
    public ArrayList<Transaction> getAllTransaction(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        ArrayList<Transaction> listTransaction = new ArrayList<>();

        Cursor cursor  = db.rawQuery("SELECT * FROM msproduct",null);

        Transaction transaction = null;
        while(cursor.moveToNext()){
           transaction = new Transaction();
            transaction.id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            transaction.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            transaction.type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            transaction.nominal = cursor.getInt(cursor.getColumnIndexOrThrow("nominal"));
            transaction.date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            listTransaction.add(transaction);
        }

        return listTransaction;
    }

    //to gate the transaction data based on selected date
    public ArrayList<Transaction> getAllTransactionbyDate(String date1){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        ArrayList<Transaction> listTransaction = new ArrayList<>();

        Transaction transaction = null;
        System.out.println(date1);
        Cursor cursor = db.rawQuery("SELECT * FROM msproduct WHERE date = ? " , new String[]{date1});


        while(cursor.moveToNext()){
            transaction = new Transaction();
            transaction.id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            transaction.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            transaction.type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            transaction.nominal = cursor.getInt(cursor.getColumnIndexOrThrow("nominal"));
            transaction.date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            listTransaction.add(transaction);
        }

        return listTransaction;
    }


}
