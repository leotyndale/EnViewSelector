package com.imuxuan.enviewselector;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by tyndale on 2018/1/17.
 */

public class MoodTextGridViewAdapter extends SimpleAdapter {

    public MoodTextGridViewAdapter(Context context, List<? extends Map<String, ?>> data) {
        super(context, data, R.layout.en_diary_mood_grid_item, new String[]{"key"}, new int[]{R.id.mood_text});
    }
}
