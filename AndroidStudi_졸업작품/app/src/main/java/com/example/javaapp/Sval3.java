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

public class Sval3 extends AppCompatActivity {

    private LineChart chart;
    private LineChart chart2;
    private Thread thread;

    private Socket mSocket;
    private float receivedData1;
    private float receivedData2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sval3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("온습도센서값");

        Button humstart = (Button) findViewById(R.id.humstart);
        humstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("tmpstart","tmpstart");
            }
        });

        Button humhome = (Button) findViewById(R.id.humhome);
        humhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sval3.this, onoff.class);
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
        chart2 = (LineChart) findViewById(R.id.chart2);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.invalidate();

        chart2.getAxisRight().setEnabled(false);
        chart2.getLegend().setTextColor(Color.WHITE);
        chart2.animateXY(2000, 2000);
        chart2.invalidate();

        Description d = new Description();
        d.setText("온도");
        d.setTextSize(11);
        d.setTextColor(Color.BLACK);
        chart.setDescription(d);

        Description d2 = new Description();
        d2.setText("습도");
        d2.setTextSize(11);
        d2.setTextColor(Color.BLACK);
        chart2.setDescription(d2);

        LineData data = new LineData();
        chart.setData(data);

        LineData data2 = new LineData();
        chart2.setData(data2);

        feedMultiple();
    }

    Emitter.Listener tmpval = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            try {
                receivedData1 = Float.parseFloat(receivedData.getString("tmpval"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Emitter.Listener humval = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            try {
                receivedData2 = Float.parseFloat(receivedData.getString("humval"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void addEntry() {

        LineData data = chart.getData();
        LineData data2 = chart2.getData();

        {
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 100) + 1500f), 0);
            data.notifyDataChanged();

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(10);
            chart.moveViewToX(data.getEntryCount());
        }
    }

        {
            if (data2 != null) {
                ILineDataSet set = data2.getDataSetByIndex(0);

                if (set == null) {
                    set = createSet();
                    data2.addDataSet(set);
                }

                data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 10) + 1500f), 0);
                data2.notifyDataChanged();

                chart2.notifyDataSetChanged();
                chart2.setVisibleXRangeMaximum(10);
                chart2.moveViewToX(data.getEntryCount());
            }
        }

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "온도");
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

        LineDataSet set = new LineDataSet(null, "습도");
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