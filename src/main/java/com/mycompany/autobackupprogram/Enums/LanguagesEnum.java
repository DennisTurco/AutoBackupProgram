package com.mycompany.autobackupprogram.Enums;

public enum LanguagesEnum {
    ITA("Italiano", "ita.json"),
    ENG("English", "eng.json");

    private final String languageName;
    private final String fileName;

    LanguagesEnum(String languageName, String fileName) {
        this.languageName = languageName;
        this.fileName = fileName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getFileName() {
        return fileName;
    }
}