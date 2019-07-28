package com.pushpal.talkie.model.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class LanguageResponse {

    @SerializedName("JSON")
    private List<Language> languages;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
