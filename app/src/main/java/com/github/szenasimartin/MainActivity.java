package com.github.szenasimartin;

import com.szenamartin.prefelem.PreferenceElement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceElement mPreferenceElement=(PreferenceElement)findViewById(R.id.one);
        ArrayList<String> descrs= new ArrayList<>();
        ArrayList<String> values= new ArrayList<>();
        descrs.add("Hungary");
        descrs.add("Germany");
        values.add("HU");
        values.add("asdfasdfasdfasdfasdfasdf");
        mPreferenceElement.create(descrs,values);
    }
}
