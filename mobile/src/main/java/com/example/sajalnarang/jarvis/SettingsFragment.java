package com.example.sajalnarang.jarvis;


import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.app_preferences);


        ((MainActivity) getActivity()).setActionBarTitle("Settings");


        findPreference("ip_address").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String stringValue = o.toString();

                Matcher matcher = IP_ADDRESS.matcher(stringValue);
                if (matcher.matches()) {
                    Constants.IP_ADDRESS = stringValue;
                    preference.setSummary(stringValue);
                } else
                    Toast.makeText(SettingsFragment.this.getActivity(), "Invalid IP Address", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findPreference("port").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String stringValue = o.toString();

                if (Integer.parseInt(stringValue) < 10000) {
                    Constants.PORT = stringValue;
                    preference.setSummary(stringValue);
                } else
                    Toast.makeText(SettingsFragment.this.getActivity(), "Invalid Port number", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
