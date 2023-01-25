package com.example.bluetoothconnec;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private Set<BluetoothDevice> devices;
    TextView mstatus, pairedtv;
    Button monbtn;
    Button moffbtn;
    Button mdisco;
    Button mpaired;
    private BluetoothAdapter mblueadapter;
    private Set<BluetoothDevice> pairedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mstatus = findViewById(R.id.status);
        monbtn = findViewById(R.id.onbtn);
        moffbtn = findViewById(R.id.offbtn);
        mdisco = findViewById(R.id.disco);
        mpaired = findViewById(R.id.paired);
        mblueadapter = BluetoothAdapter.getDefaultAdapter();


        if (mblueadapter == null) {
            mstatus.setText("BLUETOOTH NOT AVAILABLE");
        } else {
            mstatus.setText("BLUETOOTH  AVAILABLE");
        }
        monbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblueadapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mblueadapter.isEnabled()) {
                        Intent turnon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            startActivityForResult(turnon, 1);
                            Toast.makeText(getApplicationContext(), "Bluetooth Turned ON", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                                    REQUEST_ENABLE_BT);
                        }
                    }
                }
            }
        });
        moffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mblueadapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        mblueadapter.disable();
                        showToast("TURNING OFF BLUETOOTH");
                        return;
                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                                REQUEST_ENABLE_BT);
                    }

                } else {
                    showToast("BLUETOOTH IS ALREADY OFF");
                }

            }
        });

        mdisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mblueadapter.isEnabled()) {
                    showToast("Making your device discovarable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                }
            }
        });
        mpaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Set<BluetoothDevice> devices = mblueadapter.getBondedDevices();
                    for (BluetoothDevice device : devices) {
                        pairedtv.append("\nDEVICES" + device.getName() + " ," + device);
                    }
                    return;
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                            REQUEST_ENABLE_BT);
                    showToast("TURNING ON BLUETOOTH TO GET PAIRED DEVICES");
                }
            }
        });
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        if(resultcode==RESULT_OK){
            //bluetooth is on
            showToast("Bluetooth is on");
        }
        else{
            //user denied to turn on bluetooth
            showToast("cannot on bluetooth");
        }
        super.onActivityResult(requestcode, resultcode, data);
    }

    private void showToast(String ms) {
        Toast.makeText(this, ms, Toast.LENGTH_SHORT).show();
    }
}
