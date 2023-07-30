package com.example.javaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class onoff extends AppCompatActivity {

    private Socket mSocket;
    private int k=2;
    private int f=0;
    TextView text;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onoff);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
getSupportActionBar().setTitle("리모콘");

        TextView textview = (TextView) findViewById(R.id.textt);

        try {
            mSocket = IO.socket("http://192.168.200.106:3000");
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ToggleButton powerbt = (ToggleButton) findViewById(R.id.powerbt);
        powerbt.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            mSocket.emit("on","on");
                            textview.setText("on");
                        }else{
                            mSocket.emit("off","off");
                            textview.setText("off");
                        }
                    }
                }
        );

        ImageButton co2valbt = (ImageButton) findViewById(R.id.co2valbt);
        co2valbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(onoff.this, Sval.class);
                startActivity(intent);
            }
        });

        ImageButton dustvalbt = (ImageButton) findViewById(R.id.dustvalbt);
        dustvalbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(onoff.this, Sval2.class);
                startActivity(intent);
            }
        });

        ImageButton huvalbt = (ImageButton) findViewById(R.id.huvalbt);
        huvalbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(onoff.this, Sval3.class);
                startActivity(intent);
            }
        });

        ImageButton gasvalbt = (ImageButton) findViewById(R.id.gasvalbt);
        gasvalbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(onoff.this, Sval4.class);
                startActivity(intent);
            }
        });


        Button sensorstart = (Button) findViewById(R.id.sensorbt);
        sensorstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("start", "start");
            }
        });

        Button homebt = (Button) findViewById(R.id.homebt);
        homebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(onoff.this,MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton gang = (ImageButton) findViewById(R.id.gang);
        gang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (k < 3) {
                        k++;
                    } else if (k == 3) {
                        k = 3;
                    }
                }

                {
                    if (k == 3) {
                        textview.setText("강");
                    } else if (k == 2) {
                        textview.setText("중");
                    }
                    else if (k==1){
                        textview.setText("약");
                    }
                }

                mSocket.emit(String.valueOf(k),String.valueOf(k) );
            }
        });

        ImageButton yak = (ImageButton) findViewById(R.id.yak);
        yak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (k > 1) {
                        k--;
                    } else if (k == 1) {
                        k = 1;
                    }
                }

                {
                    if (k == 3) {
                        textview.setText("강");
                    } else if (k == 2) {
                        textview.setText("중");
                    }
                    else if (k==1){
                        textview.setText("약");
                    }
                }

                mSocket.emit(String.valueOf(k),String.valueOf(k));
            }
        });

    };

}