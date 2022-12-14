package com.example.sqlitedatabase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_SKU + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product){
        SQLiteDatabase dB = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_PRODUCTNAME,product.getProductName());
        vals.put(COLUMN_SKU,product.getSku());
        dB.insert(TABLE_PRODUCTS,null,vals);
        dB.close();
    }

    public Product findProduct(String productname){
        SQLiteDatabase dB = this.getReadableDatabase();
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE "+ COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor = dB.rawQuery(query,null);
        Product product = new Product();
        if(cursor.moveToFirst()){
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }else{
            product = null;
        }
        dB.close();
        return product;
    }

    public boolean deleteProduct(String productName){
        SQLiteDatabase dB = this.getWritableDatabase();
        boolean result = false;
        String query = "Select * FROM "+ TABLE_PRODUCTS + " WHERE "+ COLUMN_PRODUCTNAME + " = \"" + productName + "\"";
        Cursor cursor = dB.rawQuery(query,null);
        if(cursor.moveToFirst()){
            String ID = cursor.getString(0);
            dB.delete(TABLE_PRODUCTS,COLUMN_ID+" = \""+ID +"\"",null);
            cursor.close();
            result = true;
        }
        dB.close();
        return result;
    }
}
