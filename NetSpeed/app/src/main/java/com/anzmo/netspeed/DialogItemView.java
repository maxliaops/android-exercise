package com.anzmo.netspeed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogItemView extends LinearLayout {
    private ImageView iv_dialog;
    private TextView tv_dialog;

    public DialogItemView(Context var1, AttributeSet var2) {
        super(var1, var2);
        View.inflate(var1, R.layout.item_dialog, this);
        this.tv_dialog = (TextView)this.findViewById(R.id.tv_dialog_title);
        this.iv_dialog = (ImageView)this.findViewById(R.id.iv_dialog);
    }

    public void setChoice() {
        this.iv_dialog.setVisibility(0);
    }

    public void setText(String var1) {
        this.tv_dialog.setText(var1);
    }

    public void setUnchoice() {
        this.iv_dialog.setVisibility(4);
    }
}
