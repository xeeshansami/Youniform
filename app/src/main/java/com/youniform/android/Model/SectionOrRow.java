package com.youniform.android.Model;

public class SectionOrRow {

    private String row;
    private String section;
    private int section_id;
    private String row_name;
    private int row_id;
    private boolean isRow;


    public static SectionOrRow createRow(String row_name, int row_id) {
        SectionOrRow ret = new SectionOrRow();
        ret.row_name = row_name;
        ret.row_id = row_id;
        ret.isRow = true;
        return ret;
    }

    public static SectionOrRow createSection(String section, int section_id) {
        SectionOrRow ret = new SectionOrRow();
        ret.section = section;
        ret.section_id = section_id;
        ret.isRow = false;
        return ret;
    }

    public int getSection_id() {
        return section_id;
    }

    public String getRow_name() {
        return row_name;
    }

    public int getRow_id() {
        return row_id;
    }

    public String getRow() {
        return row;
    }

    public String getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }
}
