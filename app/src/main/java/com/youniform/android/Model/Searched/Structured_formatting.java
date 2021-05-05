package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Structured_formatting {
    private String main_text;

    private List<Main_text_matched_substrings> main_text_matched_substrings;

    private String secondary_text;

    public String getMain_text() {
        return this.main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }

    public List<Main_text_matched_substrings> getMain_text_matched_substrings() {
        return this.main_text_matched_substrings;
    }

    public void setMain_text_matched_substrings(List<Main_text_matched_substrings> main_text_matched_substrings) {
        this.main_text_matched_substrings = main_text_matched_substrings;
    }

    public String getSecondary_text() {
        return this.secondary_text;
    }

    public void setSecondary_text(String secondary_text) {
        this.secondary_text = secondary_text;
    }
}
