package com.android.finalproject_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finalproject_admin.R;
import com.android.finalproject_admin.models.BrandModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinner;
    FirebaseFirestore firestore;
    TextView proName, proPrice, proDes, proImgUrl;
    AppCompatButton add;

    int loai = 0;
    String brand = "";
    int edit;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edit = getIntent().getIntExtra("isEdit", 0);

        initView();
        ActionToolbar();


        if (edit == 0 ) { //nếu null thì thêm
            flag = false;
        }
        else {
            flag = true;
            toolbar.setTitle("Edit product");
            add.setText("Edit product");
        }

        firestore = FirebaseFirestore.getInstance();


        List<String> stringList = new ArrayList<>();
        stringList.add("Choose brand ...");
        //Get brand
        firestore.collection("brand")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BrandModel brandModel = document.toObject(BrandModel.class);
                                stringList.add(brandModel.getName());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
                brand = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarAdd);
        spinner = findViewById(R.id.spinner_brand);

        proName = findViewById(R.id.addName);
        proPrice = findViewById(R.id.addPrice);
        proDes = findViewById(R.id.addDes);
        proImgUrl = findViewById(R.id.addImg);

        add = findViewById(R.id.btnAddProduct);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }

    public void addProduct(){
        String str_ten = proName.getText().toString().trim();
        String str_gia = proPrice.getText().toString().trim();
        String str_hinhanh = proImgUrl.getText().toString().trim();
        String str_mota = proDes.getText().toString().trim();


        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_hinhanh) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || (loai == 0)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        else {
            // Add a new document with a generated id.
            Map<String, Object> data = new HashMap<>();
            data.put("name", str_ten);
            data.put("price", Integer.parseInt(str_gia));
            data.put("description", str_mota);
            data.put("img_url", str_hinhanh);
            data.put("pro_brand", brand);

            firestore.collection("AllProducts")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(AddProductActivity.this, "Added to database successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}