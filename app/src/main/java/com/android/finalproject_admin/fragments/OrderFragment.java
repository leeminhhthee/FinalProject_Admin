package com.android.finalproject_admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.finalproject_admin.R;
import com.android.finalproject_admin.adapters.OrderAdapter;
import com.android.finalproject_admin.adapters.ProductAdapter;
import com.android.finalproject_admin.models.OrderInformationModel;
import com.android.finalproject_admin.models.OrderModel;
import com.android.finalproject_admin.models.OrderProductModel;
import com.android.finalproject_admin.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    View root;

    RecyclerView rc_order;
    FirebaseFirestore firestore;
    OrderInformationModel inforModel;
    List<OrderProductModel> productList;

    List<OrderModel> list;
    OrderAdapter orderAdapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_order, container, false);

        firestore = FirebaseFirestore.getInstance();

        inforModel = new OrderInformationModel();
        productList = new ArrayList<>();
        list = new ArrayList<>();

        rc_order = root.findViewById(R.id.rc_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rc_order.setLayoutManager(layoutManager);

//        orderAdapter = new OrderAdapter(getContext(), list);
//        rc_order.setAdapter(orderAdapter);

        //Load data
        firestore.collection("Orders").document("CLHFxlE68eXBkQEmVqafjszCqpz1").collection("Orderdetail")
                .document("q5LzZRh08ZuhwTGwAuIa").collection("information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                inforModel = document.toObject(OrderInformationModel.class);
                                // Lấy thông tin sản phẩm
                                firestore.collection("Orders").document("CLHFxlE68eXBkQEmVqafjszCqpz1").collection("Orderdetail")
                                        .document("q5LzZRh08ZuhwTGwAuIa").collection("products")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        productList.add(document.toObject(OrderProductModel.class));
                                                    }
                                                    // Tạo đối tượng OrderModel và thêm vào danh sách list
                                                    OrderModel orderModel = new OrderModel(inforModel, productList);
                                                    list.add(orderModel);
                                                    // Gán danh sách list cho OrderAdapter và hiển thị lên RecyclerView
                                                    orderAdapter = new OrderAdapter(getContext(), list);
                                                    rc_order.setAdapter(orderAdapter);
                                                } else {
                                                    Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}