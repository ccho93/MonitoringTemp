package com.monitoring.charles.monitoringtemp;

import com.monitoring.charles.monitoringtemp.util.SystemUiHider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.DialogPreference;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TempActivity extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener, AdapterView.OnItemClickListener, iSetDataFragment {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    private static final String TAG = "TempActivity";
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int MESSAGE_READ = 0;
    private static final int MESSAGE_READ_PAUSE = 1;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    byte STX = (byte) 0x02;
    byte ETX = (byte) 0x03;
    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;
    private boolean kill = false;
    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */

    private ArrayList<String> parts;
    private ArrayList<String> parts2;
    private ArrayList<String> parts3;
    private ArrayAdapter mArrayAdapter;
    private ArrayList<String> btList;
    private BluetoothAdapter mBA;
    private Dialog listDialog;
    private ConnectedThread connectedThread;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> itemsNav;
    private int requestTemp0Flag;
    private Switch toggle;
    private double finalDTemperature = 0;
    private int checked = 0;
    private boolean checkedPlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        btList = new ArrayList<String>();
        Fragment newGraph = GraphFragment.newInstance();

        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.graph_place, newGraph);
        trans.addToBackStack(null);
        trans.commit();
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                BroadcastReceiver bstate = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                            itemsNav.set(0, "Bluetooth Connected");
                            // mAdapter.notifyDataSetChanged();
                            mAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, itemsNav);
                            mDrawerList.setAdapter(mAdapter);
                        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                            {
                                itemsNav.set(0, "Connect Bluetooth");
                                mAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, itemsNav);
                                mDrawerList.setAdapter(mAdapter);
                            }
                        }
                    }
                };
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
                registerReceiver(bstate, filter);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        addDrawerItems();
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, btList);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.)
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                FragmentManager fm = getFragmentManager();
                SetDataFragment f = SetDataFragment.newInstance();
                f.show(fm,"set data");

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void addDrawerItems() {
        //final String[] items = {"Connect Bluetooth", "Request Temp"};
        itemsNav = new ArrayList<String>();
        itemsNav.add("Connect Bluetooth");
        itemsNav.add("Request Temp");
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsNav);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Connect Bluetooth")) {
                    //connect bluetooth
                    scanButton();
                    mDrawerLayout.closeDrawer(mDrawerList);
                    //  mDrawerList.setItemChecked(position, true);
                } else if (parent.getItemAtPosition(position).equals("Request Temp")) {
                    if (requestTemp0Flag == 0) {
                        requestTemp0();
                        requestTemp0Flag = 1;
                        mDrawerLayout.closeDrawer(mDrawerList);

                    } else if (requestTemp0Flag == 1) {
                        requestTemp0Off();
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
            }
        });
    }

    private void requestTemp0Off() {
        final byte[] buf = new byte[7];
        buf[0] = STX;
        buf[6] = ETX;
        buf[1] = (byte) 'T';
        buf[2] = (byte) 'R';
        buf[3] = (byte) '0'; //0
        buf[4] = (byte) '0'; //1
        buf[5] = (byte) '0'; //1
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Request Close Temperature 0?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectedThread.write(buf);
                listDialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        //alert.setTitle("Choose");
        alert.show();
    }

    /*
        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);

            // Trigger the initial hide() shortly after the activity has been
            // created, to briefly hint to the user that UI controls
            // are available.
            delayedHide(1000);
        }
    */
    public void requestTemp0() {
        final byte[] buf = new byte[7];
        buf[0] = STX;
        buf[6] = ETX;
        buf[1] = (byte) 'T';
        buf[2] = (byte) 'R';
        buf[3] = (byte) '0'; //0
        buf[4] = (byte) '1'; //1
        buf[5] = (byte) '1'; //1
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Request Open Temperature 0?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectedThread.write(buf);
                listDialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        //alert.setTitle("Choose");
        alert.show();

    }

    public void scanButton() {
        //btList = new ArrayList<>();

        //mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,btList);


        mBA = BluetoothAdapter.getDefaultAdapter();
        if (mBA == null) {
            //Does not support bluetooth, which it will never be case
            System.out.println("failed bluetooth");
        }
        // btList = new ArrayList<String>();
        listDialog = new Dialog(this);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.list, null, false);

        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        final ListView listview = (ListView) listDialog.findViewById(R.id.listView);
        listview.setOnItemClickListener(this);

        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ListView listview = new ListView(this);

        listview.setAdapter(mArrayAdapter);
        if (!mBA.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);


        }
        //mBA.getBondedDevices();

        Set<BluetoothDevice> pairedDevices = mBA.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        //System.out.println(mArrayAdapter.getCount());
        //mArrayAdapter.clear();
        mBA.startDiscovery();
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    //System.out.println(device.getName());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        listDialog.setTitle("Bluetooth Device");
        listDialog.show();

    }

    //press start button to read in data and graph.
    public void startGraph(View view) {
        /*
        AssetManager am = this.getAssets();
        InputStream is = am.open("test.txt");

        Log.d(TAG, "Hello im here");
        int data = is.read();
        while(data!= -1){
            System.out.println(data);
          //  Log.d(TAG, String.valueOf(data));
            data = is.read();

        }
        */
        parts = new ArrayList<String>();
        parts2 = new ArrayList<String>();
        parts3 = new ArrayList<String>();
        System.out.println("Hello i am at start");

        //  System.out.println("im inside the thread");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("Test01_0.txt"), "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                //     System.out.println(kill);
                //    System.out.println(line);
                String[] splitParts = line.split("\t");
                parts.add(splitParts[1]);
                parts2.add(splitParts[2]);
                parts3.add(splitParts[3]);
                // parts.add(splitParts[2]);
                // parts.add(splitParts[3]);

                //  System.out.println(parts[1]);
                line = reader.readLine();
                //TimeUnit.SECONDS.sleep(1);
                //handler.postDelayed(this,1000);

            }

        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            handler.postDelayed(runable, 100);


        }
    }

    public void stopGraph(View view) {
        System.out.println("Stoping the graph");
        //  kill = true;
        //  handler.removeCallbacks(runable);
        //System.out.println("length of parts" + parts.size());
        //System.out.println(parts.toString());
        Fragment frg = (Fragment) getFragmentManager().findFragmentById(R.id.graph_place);
        frg.onPause();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose this device ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println(parent.getItemAtPosition(position));
                //System.out.print(parent.getItemAtPosition(position));
                String[] getAddress;
                getAddress = parent.getItemAtPosition(position).toString().split("\\n");
                String address = getAddress[1];
                System.out.println(address);
                BluetoothDevice device = mBA.getRemoteDevice(address);
                ConnectThread connectThread = new ConnectThread(device);
                connectThread.start();
                listDialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Choose");
        alert.show();


    }

    @Override
    public void onDialogPositiveClick(TempData temp) {
        System.out.println(temp.toString());
    }

    @Override
    public void onDialogNegativeClick(TempData temp) {

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            System.out.println("here at connect");
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBA.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception


                mmSocket.connect();


            } catch (IOException connectException) {
                connectException.printStackTrace();

                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                return;
            }
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
        }


        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);


        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        //private Fragment frg;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            int nTemp = 0;

            double dTemperature = 0;

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    System.out.println("Bytes: " + bytes);
                    //System.out.println("Buffer " + buffer.toString());
                    int i = 0;
                    while(i<buffer.length){
                        System.out.print(buffer[i]);
                        i++;
                    }
                    /*
                    if (buffer[1] == 'T' && buffer[2] == 'E') {
                        if (buffer[3] == 0) {
                            if ((buffer[4] >> 7 & 0x01) == 1) {
                                nTemp = ((buffer[4] & 0x7f) << 4 | buffer[5] >> 4);
                                dTemperature = ((double) nTemp - 2048) + (double) ((int) (buffer[5] >> 2 & 0x03)) * 0.25;
                            } else {
                                nTemp = (buffer[4] << 4 | buffer[5] >> 4);
                                dTemperature = (double) nTemp + (double) ((int) (buffer[5] >> 2 & 0x03)) * 0.25;
                            }

                            //System.out.println("TE: " + dTemperature);
                        }
                    }
                    if (buffer[1] == 'T' && buffer[2] == 'I') {
                        if (buffer[3] == 0) {
                            if ((buffer[4] >> 7 & 0x01) == 1) {
                                nTemp = (buffer[4] & 0x7f);
                                dTemperature = ((double) nTemp - 128) + (double) ((int) (buffer[5] >> 4 & 0x03)) * 0.0625;
                            } else {
                                nTemp = (buffer[4]);
                                dTemperature = (double) nTemp + (double) ((int) (buffer[5] >> 4 & 0x03)) * 0.0625;
                            }
                            //System.out.println("TI: " + dTemperature);
                        }
                    }
                    */
                    toggle = (Switch) findViewById(R.id.switch1);
                    finalDTemperature = dTemperature;
                    toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                System.out.println(finalDTemperature);
                                checked = 1;
                            } else {
                                checked = 2;
                            }
                        }
                    });
                    if (checked == 1) {
                        // frg = (Fragment) getFragmentManager().findFragmentById(R.id.graph_place);
                        // frg.onResume();
                        //frg.onPause();
                        //doubleCheck = 1;
                        System.out.println(finalDTemperature);
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget();

                    } else if (checked == 2) {
                        System.out.println("Unchekd");
                        mHandler.obtainMessage(MESSAGE_READ_PAUSE, bytes, -1, buffer)
                                .sendToTarget();
                    }

                    Thread.sleep(1000);

                    //System.out.println(bytes);
                    // Send the obtained bytes to the UI activity

                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public double getTemperature() {
        return finalDTemperature;
    }

    public int getFlag() {
        return checked;
    }

    public boolean getCheckedPlot() {
        return checkedPlot;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    int nTemp;
                    double dTemperature = 0;
                    if (readBuf[1] == 'T' && readBuf[2] == 'E') {
                        if (readBuf[3] == 0) {
                            if ((readBuf[4] >> 7 & 0x01) == 1) {
                                nTemp = ((readBuf[4] & 0x7f) << 4 | readBuf[5] >> 4);
                                dTemperature = ((double) nTemp - 2048) + (double) ((int) (readBuf[5] >> 2 & 0x03)) * 0.25;
                            } else {
                                nTemp = (readBuf[4] << 4 | readBuf[5] >> 4);
                                dTemperature = (double) nTemp + (double) ((int) (readBuf[5] >> 2 & 0x03)) * 0.25;
                            }

                            //System.out.println("TE: " + dTemperature);
                        }
                    }
                    if (readBuf[1] == 'T' && readBuf[2] == 'I') {
                        if (readBuf[3] == 0) {
                            if ((readBuf[4] >> 7 & 0x01) == 1) {
                                nTemp = (readBuf[4] & 0x7f);
                                dTemperature = ((double) nTemp - 128) + (double) ((int) (readBuf[5] >> 4 & 0x03)) * 0.0625;
                            } else {
                                nTemp = (readBuf[4]);
                                dTemperature = (double) nTemp + (double) ((int) (readBuf[5] >> 4 & 0x03)) * 0.0625;
                            }
                            //System.out.println("TI: " + dTemperature);
                        }
                    }
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    finalDTemperature = dTemperature;
                    checkedPlot = true;
                    Fragment frg = (Fragment) getFragmentManager().findFragmentById(R.id.graph_place);
                    frg.onResume();
                    //  System.out.println(dTemperature);
                    break;
                case MESSAGE_READ_PAUSE:
                    readBuf = (byte[]) msg.obj;
                    System.out.println("I am at Pause");
                    // construct a string from the valid bytes in the buffer
                    dTemperature = 0;
                    if (readBuf[1] == 'T' && readBuf[2] == 'E') {
                        if (readBuf[3] == 0) {
                            if ((readBuf[4] >> 7 & 0x01) == 1) {
                                nTemp = ((readBuf[4] & 0x7f) << 4 | readBuf[5] >> 4);
                                dTemperature = ((double) nTemp - 2048) + (double) ((int) (readBuf[5] >> 2 & 0x03)) * 0.25;
                            } else {
                                nTemp = (readBuf[4] << 4 | readBuf[5] >> 4);
                                dTemperature = (double) nTemp + (double) ((int) (readBuf[5] >> 2 & 0x03)) * 0.25;
                            }

                            //System.out.println("TE: " + dTemperature);
                        }
                    }
                    if (readBuf[1] == 'T' && readBuf[2] == 'I') {
                        if (readBuf[3] == 0) {
                            if ((readBuf[4] >> 7 & 0x01) == 1) {
                                nTemp = (readBuf[4] & 0x7f);
                                dTemperature = ((double) nTemp - 128) + (double) ((int) (readBuf[5] >> 4 & 0x03)) * 0.0625;
                            } else {
                                nTemp = (readBuf[4]);
                                dTemperature = (double) nTemp + (double) ((int) (readBuf[5] >> 4 & 0x03)) * 0.0625;
                            }
                            //System.out.println("TI: " + dTemperature);
                        }
                    }
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    finalDTemperature = dTemperature;
                    checkedPlot = false;
                    frg = (Fragment) getFragmentManager().findFragmentById(R.id.graph_place);
                    frg.onResume();
                    break;
            }
        }
    };
}
