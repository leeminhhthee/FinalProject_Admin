package com.android.finalproject_admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.finalproject_admin.R;
import com.android.finalproject_admin.activities.AddProductActivity;
import com.android.finalproject_admin.adapters.ProductAdapter;
import com.android.finalproject_admin.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    View root;
    RecyclerView rc_product;
    //Brand home recyclerview
    ProductAdapter productAdapter;
    List<ProductModel> productModelList;
    ImageView add;

    FirebaseFirestore db;
    FirebaseUser user;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_product, container, false);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        initView();

        //Load data
        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel suggestProductModel = document.toObject(ProductModel.class);
                                productModelList.add(suggestProductModel);
                                productAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return root;
    }

    public void initView(){
        rc_product = root.findViewById(R.id.rc_product);
        rc_product.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productModelList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productModelList);
        rc_product.setAdapter(productAdapter);

        add = root.findViewById(R.id.img_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                // Handle edit action
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.putExtra("isEdit", 1);
                startActivity(intent);
                return true;
            case R.id.menu_delete:
                // Handle delete action
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}