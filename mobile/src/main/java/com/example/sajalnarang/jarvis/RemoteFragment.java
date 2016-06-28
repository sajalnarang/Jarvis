package com.example.sajalnarang.jarvis;


import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteFragment extends PreferenceFragment {

    ListView remoteList;
    static SeekBar regulator;
    TextView speed;


    public RemoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Jarvis");
    }

    @Override
    public void onStart() {
        super.onStart();

        speed = (TextView) getActivity().findViewById(R.id.speed);
        remoteList = (ListView) getActivity().findViewById(R.id.remote_list);

        ArrayList<Item> remote_arraylist = new ArrayList<>();
        remote_arraylist.add(new Item("Lights", "Off"));
        remote_arraylist.add(new Item("Fans", "Off"));
        remote_arraylist.add(new Item("Latch", "Locked"));
        remote_arraylist.add(new Item("Door", "Closed"));

        MyAdapter adapter = new MyAdapter(RemoteFragment.this.getActivity(), remote_arraylist);
        remoteList.setAdapter(adapter);

        regulator = (SeekBar) getActivity().findViewById(R.id.regulator);
        regulator.setEnabled(false);
        speed.setText("" + regulator.getProgress());
        regulator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed.setText("" + regulator.getProgress());
                sendToArduino(regulator.getProgress() + 3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_remote, container, false);
    }

    public static void lightsOn() {
        //connectedThread.write("1".getBytes());
        sendToArduino(1);
    }

    public static void lightsOff() {
        //connectedThread.write("2".getBytes());
        sendToArduino(2);
    }

    public static void fansOn() {
        regulator.setEnabled(true);
        sendToArduino(regulator.getProgress() + 3);
        //connectedThread.write(Integer.toString(MainActivity.regulator.getProgress()+3).getBytes());
    }

    public static void fansOff() {
        regulator.setEnabled(false);
        sendToArduino(3);
        //connectedThread.write("3".getBytes());
    }

    public static void changeFanSpeed(int speed) {
        sendToArduino(speed);
        //connectedThread.write(Integer.toString(speed).getBytes());
    }

    public static void unlockDoor() {
        sendToArduino(0);
        //connectedThread.write("0".getBytes());
    }

    public static void lockDoor() {
        sendToArduino(9);
        //connectedThread.write("9".getBytes());
    }

    public static void closeDoor() {
        sendToArduino(10);
        //connectedThread.write("".getBytes());
    }

    public static void openDoor() {
        MyAdapter.switch1[2].setChecked(false);
        sendToArduino(11);
        //connectedThread.write("".getBytes());
    }

    private static void sendToArduino(int i) {
        String str = Integer.toString(i);
        (new JSONTask()).execute(str);
    }

}


class JSONTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        final OkHttpClient client = new OkHttpClient();
        String str = params[0];

        Request request = new Request.Builder()
                .url("http://" + Constants.IP_ADDRESS + "/?" + str)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null) {
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    try {
                        System.out.println(response.body().string());
                    } catch (IOException f) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null)
                response.close();
        }
        return null;
    }
}