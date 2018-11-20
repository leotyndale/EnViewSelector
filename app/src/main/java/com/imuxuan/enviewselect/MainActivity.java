package com.imuxuan.enviewselect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.imuxuan.enviewselector.EnViewSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnViewSelector mEnSelector = findViewById(R.id.en_selector);
        mEnSelector.updateMoodText(getTestData());
    }

    @NonNull
    private ArrayList<Map<String, String>> getTestData() {
        ArrayList<Map<String, String>> testData = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("key", "Test" + mCount);
            testData.add(hashMap);
            mCount++;
        }
        return testData;
    }

}
