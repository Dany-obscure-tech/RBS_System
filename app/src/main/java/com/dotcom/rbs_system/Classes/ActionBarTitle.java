package com.dotcom.rbs_system.Classes;

import android.widget.TextView;

public class ActionBarTitle {
    private static final ActionBarTitle actionBarTitleObj = new ActionBarTitle();

    TextView textView;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    private ActionBarTitle(){};

    public static ActionBarTitle getInstance(){
        return actionBarTitleObj;
    }
}
