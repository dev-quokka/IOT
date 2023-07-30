package com.example.javaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Sval extends AppCompatActivity {

    private LineChart chart;
    private Thread thread;

    private Socket mSocket;
    private float receivedData1;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sval);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("가스센서값");


        Button button1 = (Button) findViewById(R.id.button22);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("co2start","co2start");
                mSocket.on("co2val",co2val);
            }
        });

        Button button2 = (Button) findViewById(R.id.button33);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sval.this, onoff.class);
                startActivity(intent);
            }
        });

        try {
            mSocket = IO.socket("http://192.168.200.106:3000");
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        chart = (LineChart) findViewById(R.id.chart);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.invalidate();
        chart.setDescription(null);

        LineData data = new LineData();
        chart.setData(data);

        feedMultiple();

    }

    Emitter.Listener co2val = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            try {
                receivedData1 = Float.parseFloat(receivedData.getString("co2val"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void addEntry() {

        LineData data = chart.getData();

            if (data != null) {
                ILineDataSet set = data.getDataSetByIndex(0);

                if (set == null) {
                    set = createSet();
                    data.addDataSet(set);
                }

                data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 500) + 1500f), 0);
                data.notifyDataChanged();

                chart.notifyDataSetChanged();
                chart.setVisibleXRangeMaximum(10);
                chart.moveViewToX(data.getEntryCount());
            }

    }

    private LineDataSet createSet() {

            LineDataSet set = new LineDataSet(null, "c02val");
            set.setFillAlpha(110);
            set.setFillColor(Color.parseColor("#d7e7fa"));
            set.setColor(Color.parseColor("#0B80C9"));
            set.setCircleColor(Color.parseColor("#FFA1B4DC"));
            set.setCircleColorHole(Color.BLUE);
            set.setValueTextColor(Color.BLACK);
            set.setDrawValues(true);
            set.setLineWidth(2);
            set.setCircleRadius(3);
            set.setDrawCircleHole(true);
            set.setDrawCircles(true);
            set.setValueTextSize(9f);
            set.setDrawFilled(true);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setHighLightColor(Color.rgb(244, 117, 117));
            return set;
    }

    private LineDataSet createSet2() {

        LineDataSet set2 = new LineDataSet(null, "cc2val");
        set2.setFillAlpha(110);
        set2.setFillColor(Color.parseColor("#d7e7fa"));
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        set2.setCircleColorHole(Color.BLUE);
        set2.setValueTextColor(Color.BLACK);
        set2.setDrawValues(true);
        set2.setLineWidth(2);
        set2.setCircleRadius(3);
        set2.setDrawCircleHole(true);
        set2.setDrawCircles(true);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(true);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        return set2;
    }

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }

            }
        });
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null)
            thread.interrupt();
    }

}