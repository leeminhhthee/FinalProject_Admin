package com.android.finalproject_admin.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.finalproject_admin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment implements OnChartValueSelectedListener {
    FirebaseFirestore firestore;
    CollectionReference ordersRef;
    List<BarEntry> entries;

    public StatisticsFragment() {
        // Required empty public constructor
    }
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        barChart = view.findViewById(R.id.barChart);

        firestore = FirebaseFirestore.getInstance();
        ordersRef = firestore.collection("Orders");

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Tháng trong Java bắt đầu từ 0

        // Tạo truy vấn để lọc dữ liệu trong tháng hiện tại
        Query query = ordersRef.whereEqualTo("date", getFormattedMonth(currentMonth));

//        entries = new ArrayList<>();
        // Thực hiện truy vấn và tính tổng doanh thu
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long totalRevenue = 0;

                for (DocumentSnapshot document : task.getResult()) {
                    long orderTotal = document.getLong("total");
                    totalRevenue += orderTotal;
                }
                Toast.makeText(getContext(), getFormattedMonth(currentMonth)+"", Toast.LENGTH_SHORT).show();
//                entries.add(new BarEntry(currentMonth, totalRevenue));

            } else {
                // Xử lý khi truy vấn thất bại
            }
        });

        // Lấy dữ liệu thống kê doanh thu (thay bằng phương thức lấy dữ liệu từ Firestore)
        List<BarEntry> entries = getDoanhThuThang();

        // Tạo BarDataSet từ dữ liệu và thiết lập các thuộc tính cho biểu đồ
        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu tháng");
        dataSet.setColor(Color.BLUE);

        // Tạo BarData từ BarDataSet và thiết lập các thuộc tính cho biểu đồ
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        // Đặt dữ liệu vào BarChart
        barChart.setData(barData);

        // Thiết lập người nghe sự kiện cho biểu đồ
        barChart.setOnChartValueSelectedListener(this);

        // Cập nhật biểu đồ
        barChart.invalidate();

        return view;
    }

    // Hàm chuyển đổi tháng thành chuỗi định dạng "tháng MM năm YYYY"
    private String getFormattedMonth(int month) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String monthName = symbols.getMonths()[month - 1];
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format("%s %02d năm %d", monthName, month, year);
    }

    // Phương thức để lấy dữ liệu thống kê doanh thu (ví dụ)
    private List<BarEntry> getDoanhThuThang() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 1000));
        entries.add(new BarEntry(2, 2000));
        entries.add(new BarEntry(3, 3000));
        entries.add(new BarEntry(4, 3000));
        entries.add(new BarEntry(5, 3000));
        entries.add(new BarEntry(6, 3000));
        entries.add(new BarEntry(7, 3000));
        entries.add(new BarEntry(8, 3000));
        entries.add(new BarEntry(9, 3000));
        entries.add(new BarEntry(10, 3000));
        entries.add(new BarEntry(11, 3000));
        entries.add(new BarEntry(12, 3000));

        return entries;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // Xử lý sự kiện khi người dùng chọn một cột trong biểu đồ
        float x = e.getX(); // Giá trị x của cột được chọn
        float y = e.getY(); // Giá trị y của cột được chọn

        // Hiển thị thông báo với giá trị của cột được chọn
        Toast.makeText(getActivity(), "Cột " + x + ": " + y, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected() {
        // Xử lý sự kiện khi không có cột nào được chọn
    }
}