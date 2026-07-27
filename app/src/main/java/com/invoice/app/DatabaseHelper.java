package com.invoice.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "invoice.db";
    private static final int DATABASE_VERSION = 1;

    // جداول
    public static final String TABLE_INVOICES = "invoices";
    public static final String TABLE_PRODUCTS = "products";

    // أعمدة الفاتورة
    public static final String COL_ID = "id";
    public static final String COL_INVOICE_NUMBER = "invoice_number";
    public static final String COL_DATE = "date";
    public static final String COL_INVOICE_TYPE = "invoice_type";
    public static final String COL_STUDENT_NAME = "student_name";
    public static final String COL_CLASS_NUMBER = "class_number";
    public static final String COL_CLASS_NAME = "class_name";
    public static final String COL_COMPANY = "company";
    public static final String COL_DISCOUNT = "discount";
    public static final String COL_TOTAL_WORDS = "total_words";
    public static final String COL_TOTAL = "total";

    // أعمدة المنتجات
    public static final String COL_INVOICE_ID = "invoice_id";
    public static final String COL_QTY = "qty";
    public static final String COL_UNIT = "unit";
    public static final String COL_VALUE = "value";
    public static final String COL_PRICE = "price";
    public static final String COL_TOTAL_PRICE = "total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // إنشاء جدول الفواتير
        String createInvoices = "CREATE TABLE " + TABLE_INVOICES + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INVOICE_NUMBER + " TEXT, "
                + COL_DATE + " TEXT, "
                + COL_INVOICE_TYPE + " TEXT, "
                + COL_STUDENT_NAME + " TEXT, "
                + COL_CLASS_NUMBER + " TEXT, "
                + COL_CLASS_NAME + " TEXT, "
                + COL_COMPANY + " TEXT, "
                + COL_DISCOUNT + " REAL, "
                + COL_TOTAL_WORDS + " TEXT, "
                + COL_TOTAL + " REAL)";
        db.execSQL(createInvoices);

        // إنشاء جدول المنتجات
        String createProducts = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INVOICE_ID + " INTEGER, "
                + COL_QTY + " INTEGER, "
                + COL_UNIT + " TEXT, "
                + COL_VALUE + " TEXT, "
                + COL_PRICE + " REAL, "
                + COL_TOTAL_PRICE + " REAL, "
                + "FOREIGN KEY(" + COL_INVOICE_ID + ") REFERENCES " + TABLE_INVOICES + "(" + COL_ID + "))";
        db.execSQL(createProducts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        onCreate(db);
    }

    // حفظ فاتورة جديدة
    public long saveInvoice(Invoice invoice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(COL_DATE, invoice.getDate());
        values.put(COL_INVOICE_TYPE, invoice.getInvoiceType());
        values.put(COL_STUDENT_NAME, invoice.getStudentName());
        values.put(COL_CLASS_NUMBER, invoice.getClassNumber());
        values.put(COL_CLASS_NAME, invoice.getClassName());
        values.put(COL_COMPANY, invoice.getCompany());
        values.put(COL_DISCOUNT, invoice.getDiscount());
        values.put(COL_TOTAL_WORDS, invoice.getTotalWords());
        values.put(COL_TOTAL, invoice.getTotal());
        return db.insert(TABLE_INVOICES, null, values);
    }

    // حفظ منتج
    public void saveProduct(long invoiceId, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INVOICE_ID, invoiceId);
        values.put(COL_QTY, product.getQty());
        values.put(COL_UNIT, product.getUnit());
        values.put(COL_VALUE, product.getValue());
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_TOTAL_PRICE, product.getTotalPrice());
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // جلب جميع الفواتير
    public Cursor getAllInvoices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_INVOICES, null, null, null, null, null, COL_ID + " DESC");
    }
}