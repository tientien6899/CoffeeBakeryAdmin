package com.example.coffeebakeryadmin.ListReport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.coffeebakeryadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MonthReportActivity extends AppCompatActivity {

    ImageView back;
    TextView thangreport;
    Button xem_thang;
    AnyChartView monthreport;
    private DatabaseReference mdata = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report);
        AnhXa();

        thangreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(view.getContext());
                d.setContentView(R.layout.dialog_thongketheongay);
                DatePicker datePicker = d.findViewById(R.id.time_thongkengay);
                Button xacnhan = d.findViewById(R.id.btn_xemthongkengay);
                xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String month = String.valueOf(datePicker.getMonth()+1);
                        if(Integer.parseInt(month) <= 9)
                            month = "0" + month;
                        int year = datePicker.getYear();
                        thangreport.setText(month+"-"+year);
                        d.cancel();
                    }
                });
                d.show();
            }
        });
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data1 = new ArrayList<>();

        xem_thang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdata.child("DonHang").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String ngay = thangreport.getText().toString();
                        int tong = 0;
                        for(DataSnapshot snap : snapshot.getChildren()){
                            String string_ngaydat = snap.child("ngaydat").getValue().toString();
                            if(string_ngaydat.contains(ngay)){
                                String string_tongtien = snap.child("tongtien").getValue().toString();
                                tong += Integer.parseInt(string_tongtien);
                            }
                        }

                        if(tong == 0){
                            Toast.makeText(MonthReportActivity.this, "Doanh thu tháng đấy không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(data1.size() == 1){
                                monthreport.clear();
                                List<DataEntry> data1 = new ArrayList<>();
                                Toast.makeText(MonthReportActivity.this, ngay, Toast.LENGTH_SHORT).show();
                                data1.add(new ValueDataEntry(ngay, tong));
                                Column column = cartesian.column(data1);
                                column.unselect(0);
                                column.tooltip()
                                        .titleFormat("{%X}")
                                        .position(Position.CENTER_BOTTOM)
                                        .anchor(Anchor.CENTER_BOTTOM)
                                        .offsetX(0d)
                                        .offsetY(5d)
                                        .format("{%Value}{groupsSeparator: }");
                                monthreport.setChart(cartesian);
                            }
                            else {
                                data1.add(new ValueDataEntry(ngay, tong));
                                Column column = cartesian.column(data1);
                                column.tooltip()
                                        .titleFormat("{%X}")
                                        .position(Position.CENTER_BOTTOM)
                                        .anchor(Anchor.CENTER_BOTTOM)
                                        .offsetX(0d)
                                        .offsetY(5d)
                                        .format("{%Value}{groupsSeparator: }");
                                monthreport.setChart(cartesian);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

    }

    private void AnhXa() {
        back = (ImageView) findViewById(R.id.btn_BackReportMonth);
        thangreport = (TextView) findViewById(R.id.report_monthreport);
        xem_thang = (Button) findViewById(R.id.report_xemreportthang);
        monthreport = (AnyChartView) findViewById(R.id.any_chart_view_thang);
    }
}