package com.example.mqtttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IGetMessageCallBack {

    private TextView textView;
    private TextView textView_input;
    private Button button;
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    private Button button_connect;
    private StringBuilder builder;//接收到的消息
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.textView);
        textView_input = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button_connect = findViewById(R.id.button2);
        builder = new StringBuilder();

        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MainActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MQTTService.publish(textView_input.getText().toString().trim());
            }
        });


        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MQTTService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    public void setMessage(String message) {

        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);

        builder.append(message);
        builder.append("\n");
        textView.setText(builder.toString());

    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
