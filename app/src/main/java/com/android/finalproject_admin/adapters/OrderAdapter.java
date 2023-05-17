package com.android.finalproject_admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.finalproject_admin.R;
import com.android.finalproject_admin.activities.ShowOrderActivity;
import com.android.finalproject_admin.models.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<OrderModel> array;

    public OrderAdapter(Context context, List<OrderModel> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = array.get(position);
        holder.idOrder.setText("Order of #"+order.getName());
        holder.status.setText(order.getStatus()+"");
        holder.created_at.setText("Order at " + order.getDate());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.rc_history_detail.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        linearLayoutManager.setInitialPrefetchItemCount(order.getProducts().size());

        //Adapter detail order
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, order.getProducts());
        holder.rc_history_detail.setLayoutManager(linearLayoutManager);
        holder.rc_history_detail.setAdapter(detailAdapter);
        holder.rc_history_detail.setRecycledViewPool(viewPool);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowOrderActivity.class);
                intent.putExtra("detailed", order);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView idOrder, created_at;
        RecyclerView rc_history_detail;
        Button status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            created_at = itemView.findViewById(R.id.dateOrder);
            status = itemView.findViewById(R.id.status);
            rc_history_detail = itemView.findViewById(R.id.rc_history_detail);
        }
    }
}
