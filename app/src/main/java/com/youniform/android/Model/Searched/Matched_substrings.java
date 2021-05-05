package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

@Keep
public class Matched_substrings {
    private int length;

    private int offset;

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
