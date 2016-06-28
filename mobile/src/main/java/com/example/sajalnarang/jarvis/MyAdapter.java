package com.example.sajalnarang.jarvis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    public static TextView[] labelView = new TextView[4];
    public static TextView[] valueView = new TextView[4];
    public static Switch[] switch1 = new Switch[4];

    public MyAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.activity_listview, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.activity_listview, parent, false);

        // 3. Get the two text view from the rowView
        labelView[position] = (TextView) rowView.findViewById(R.id.label);
        valueView[position] = (TextView) rowView.findViewById(R.id.value);
        switch1[position] = (Switch) rowView.findViewById(R.id.test_switch);


        switch1[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Snackbar.make(buttonView,"Clicked at " + (position + 1),Snackbar.LENGTH_SHORT).setAction("Action",null).show();

                if (isChecked) {
                    switch (position) {
                        case 0:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "On"));
                            RemoteFragment.lightsOn();
                            break;
                        case 1:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "On"));
                            RemoteFragment.fansOn();
                            break;
                        case 2:
                            if (switch1[3].isChecked()) {
                                Toast.makeText(context, "Please close the door first", Toast.LENGTH_SHORT).show();
                                switch1[2].setChecked(false);
                            } else {
                                itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Locked"));
                                RemoteFragment.lockDoor();
                            }
                            break;
                        case 3:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Open"));
                            RemoteFragment.openDoor();
                            break;

                    }
                } else {
                    switch (position) {
                        case 0:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Off"));
                            RemoteFragment.lightsOff();
                            break;
                        case 1:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Off"));
                            RemoteFragment.fansOff();
                            break;
                        case 2:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Unlocked"));
                            RemoteFragment.unlockDoor();
                            break;
                        case 3:
                            itemsArrayList.set(position, new Item(itemsArrayList.get(position).getTitle(), "Closed"));
                            RemoteFragment.closeDoor();
                            break;

                    }
                }
                labelView[position].setText(itemsArrayList.get(position).getTitle());
                valueView[position].setText(itemsArrayList.get(position).getDescription());
            }
        });

        // 4. Set the text for textView
        labelView[position].setText(itemsArrayList.get(position).getTitle());
        valueView[position].setText(itemsArrayList.get(position).getDescription());

        // 5. return rowView
        return rowView;
    }
}