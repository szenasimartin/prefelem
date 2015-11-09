package com.github.szenasimartin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.szenasimartin.prefelem.PreferenceElement;

import java.util.ArrayList;
import java.util.List;

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
