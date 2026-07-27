package com.invoice.app;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<Product> products = new ArrayList<>();
    private LinearLayout productsContainer;
    private EditText etInvoiceNumber, etDate, etInvoiceType, etStudentName;
    private EditText etClassNumber, etClassName, etCompany, etDiscount, etTotalWords;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbHelper = new DatabaseHelper(this);
        initViews();
        setupListeners();
        addProductRow();
    }

    private void initViews() {
        productsContainer = findViewById(R.id.productsContainer);
        etInvoiceNumber = findViewById(R.id.etInvoiceNumber);
        etDate = findViewById(R.id.etDate);
        etInvoiceType = findViewById(R.id.etInvoiceType);
        etStudentName = findViewById(R.id.etStudentName);
        etClassNumber = findViewById(R.id.etClassNumber);
        etClassName = findViewById(R.id.etClassName);
        etCompany = findViewById(R.id.etCompany);
        etDiscount = findViewById(R.id.etDiscount);
        etTotalWords = findViewById(R.id.etTotalWords);
        tvTotal = findViewById(R.id.tvTotal);
    }

    private void setupListeners() {
        findViewById(R.id.btnAddProduct).setOnClickListener(v -> addProductRow());
        findViewById(R.id.btnSaveInvoice).setOnClickListener(v -> saveInvoice());
        findViewById(R.id.btnCalculateTotal).setOnClickListener(v -> calculateTotal());
    }

    private void addProductRow() {
        View row = getLayoutInflater().inflate(R.layout.item_product, null);
        productsContainer.addView(row);
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 0; i < productsContainer.getChildCount(); i++) {
            View row = productsContainer.getChildAt(i);
            EditText etPrice = row.findViewById(R.id.etPrice);
            EditText etQty = row.findViewById(R.id.etQty);
            if (etPrice.getText().toString().isEmpty() || etQty.getText().toString().isEmpty()) continue;
            double price = Double.parseDouble(etPrice.getText().toString());
            int qty = Integer.parseInt(etQty.getText().toString());
            total += price * qty;
        }
        tvTotal.setText("الإجمالي: " + String.format("%.2f", total));
    }

    private void saveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(etInvoiceNumber.getText().toString());
        invoice.setDate(etDate.getText().toString());
        invoice.setInvoiceType(etInvoiceType.getText().toString());
        invoice.setStudentName(etStudentName.getText().toString());
        invoice.setClassNumber(etClassNumber.getText().toString());
        invoice.setClassName(etClassName.getText().toString());
        invoice.setCompany(etCompany.getText().toString());
        invoice.setDiscount(Double.parseDouble(etDiscount.getText().toString().isEmpty() ? "0" : etDiscount.getText().toString()));
        invoice.setTotalWords(etTotalWords.getText().toString());
        
        double total = 0;
        for (int i = 0; i < productsContainer.getChildCount(); i++) {
            View row = productsContainer.getChildAt(i);
            EditText etPrice = row.findViewById(R.id.etPrice);
            EditText etQty = row.findViewById(R.id.etQty);
            EditText etUnit = row.findViewById(R.id.etUnit);
            EditText etValue = row.findViewById(R.id.etValue);
            
            if (etPrice.getText().toString().isEmpty()) continue;
            Product product = new Product();
            product.setQty(Integer.parseInt(etQty.getText().toString().isEmpty() ? "1" : etQty.getText().toString()));
            product.setUnit(etUnit.getText().toString());
            product.setValue(etValue.getText().toString());
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));
            product.setTotalPrice(product.getPrice() * product.getQty());
            total += product.getTotalPrice();
            products.add(product);
        }
        
        invoice.setTotal(total - invoice.getDiscount());
        
        long invoiceId = dbHelper.saveInvoice(invoice);
        for (Product product : products) {
            dbHelper.saveProduct(invoiceId, product);
        }
        
        Toast.makeText(this, "تم حفظ الفاتورة بنجاح", Toast.LENGTH_LONG).show();
        finish();
    }
}