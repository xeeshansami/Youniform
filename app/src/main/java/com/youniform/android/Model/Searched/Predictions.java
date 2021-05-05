package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Predictions {
    private String description;

    private String id;

    private List<Matched_substrings> matched_substrings;

    private String place_id;

    private String reference;

    private Structured_formatting structured_formatting;

    private List<Terms> terms;

    private List<String> types;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Matched_substrings> getMatched_substrings() {
        return this.matched_substrings;
    }

    public void setMatched_substrings(List<Matched_substrings> matched_substrings) {
        this.matched_substrings = matched_substrings;
    }

    public String getPlace_id() {
        return this.place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Structured_formatting getStructured_formatting() {
        return this.structured_formatting;
    }

    public void setStructured_formatting(Structured_formatting structured_formatting) {
        this.structured_formatting = structured_formatting;
    }

    public List<Terms> getTerms() {
        return this.terms;
    }

    public void setTerms(List<Terms> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return this.types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
