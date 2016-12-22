package com.anzmo.netspeed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetItemView extends LinearLayout {
    private ImageView iv_set;
    private TextView tv_set_desc;
    private TextView tv_set_title;

    public SetItemView(Context var1, AttributeSet var2) {
        super(var1, var2);
        View.inflate(var1, R.layout.item_set, this);
        this.tv_set_title = (TextView)this.findViewById(R.id.tv_set_title);
        this.tv_set_desc = (TextView)this.findViewById(R.id.tv_set_desc);
        this.iv_set = (ImageView)this.findViewById(R.id.iv_set);
    }

    public void setDesc(String var1) {
        this.tv_set_desc.setText(var1);
    }

    public void setImage(int var1) {
        this.iv_set.setImageResource(var1);
    }

    public void setTitle(String var1) {
        this.tv_set_title.setText(var1);
    }
}
