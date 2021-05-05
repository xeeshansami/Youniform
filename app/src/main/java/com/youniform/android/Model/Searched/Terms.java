package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

@Keep
public class Terms {
    private int offset;

    private String value;

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
