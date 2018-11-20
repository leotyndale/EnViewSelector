package com.imuxuan.enviewselector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Yunpeng Li on 2018/11/20.
 */
public class EnViewSelector extends LinearLayout {

    public static final int MOOD_TEXT_TRANSLATE_DURATION = 800;

    private GridView mTextGridView;
    private SimpleAdapter mTextGridViewAdapter;

    private EditText mBlankEditor1;
    private EditText mBlankEditor2;
    private EditText mBlankEditor3;

    private OnClickListener onChangeClickListener;

    public EnViewSelector(Context context) {
        this(context, null);
    }

    public EnViewSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnViewSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.en_diary, this);
        initChangeTextView();
        initEditor();
    }

    public EnViewSelector setOnChangeClickListener(OnClickListener onChangeClickListener) {
        this.onChangeClickListener = onChangeClickListener;
        return this;
    }

    private void initChangeTextView() {
        View changeText = findViewById(R.id.change_text);
        changeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChangeClickListener != null) {
                    onChangeClickListener.onClick(v);
                }
                doAlphaAnim(mTextGridView, false, true);
            }
        });
    }

    private void doAlphaAnim(final View view, final boolean isShow, final boolean isRepeat) {
        AlphaAnimation alphaAnim = getAlphaAnim(isShow, isRepeat);

        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow && !isRepeat) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnim);
    }

    public static AlphaAnimation getAlphaAnim(final boolean isShow, final boolean isRepeat) {
        AlphaAnimation alphaAnim;
        if (isShow) {
            alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        } else {
            alphaAnim = new AlphaAnimation(1.0f, 0.0f);
        }
        //执行三秒
        alphaAnim.setDuration(1000);
        if (isRepeat) {
            alphaAnim.setRepeatCount(1);
            alphaAnim.setRepeatMode(Animation.REVERSE);
            alphaAnim.setDuration(500);
        }
        return alphaAnim;
    }

    private void initEditor() {
        mBlankEditor1 = findViewById(R.id.keyword1);
        mBlankEditor2 = findViewById(R.id.keyword2);
        mBlankEditor3 = findViewById(R.id.keyword3);
    }

    private EditText switchBlankMoodEdit(EditText blankEditor1, EditText blankEditor2, EditText blankEditor3) {
        if (TextUtils.isEmpty(blankEditor1.getText())) {
            return blankEditor1;
        } else if (TextUtils.isEmpty(blankEditor2.getText())) {
            return blankEditor2;
        } else if (TextUtils.isEmpty(blankEditor3.getText())) {
            return blankEditor3;
        } else {
            return null;
        }
    }

    public void updateMoodText(final List<Map<String, String>> moodInfo) {
        mTextGridView = findViewById(R.id.gridView);
        mTextGridViewAdapter = new MoodTextGridViewAdapter(getContext(), moodInfo);
        mTextGridView.setAdapter(mTextGridViewAdapter);
        mTextGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final TextView moodText = view.findViewById(R.id.mood_text);
                        final CharSequence moodTextData = moodText.getText();
                        if (TextUtils.isEmpty(moodTextData)) {
                            return;
                        }
                        final EditText blankEditor;
                        blankEditor = switchBlankMoodEdit(mBlankEditor1, mBlankEditor2, mBlankEditor3);
                        if (blankEditor == null) {
                            return;
                        }
                        initMoodTextFlyAnimator(moodText, blankEditor, new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                blankEditor.setText(moodTextData);

                                if (position < moodInfo.size() && moodInfo.get(position) != null) {
                                    moodInfo.get(position).clear();
                                    mTextGridViewAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }
                }

        );
    }

    private void initMoodTextFlyAnimator(final TextView moodText, final EditText editText, Animation.AnimationListener listener) {
        moodText.bringToFront();

        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f);
        set.addAnimation(scaleAnim);

        final int[] buttonPosition = new int[2];
        moodText.getLocationOnScreen(buttonPosition);
        final int[] editTextPosition = new int[2];
        editText.getLocationOnScreen(editTextPosition);

        TranslateAnimation tranAnim = new TranslateAnimation(
                0, editTextPosition[0] - buttonPosition[0] + editText.getWidth() / 2,
                0, editTextPosition[1] - buttonPosition[1] - editText.getHeight() / 2);
        set.addAnimation(tranAnim);

        set.setAnimationListener(listener);
        set.setDuration(MOOD_TEXT_TRANSLATE_DURATION);
        moodText.bringToFront();
        moodText.startAnimation(set);
    }

}
