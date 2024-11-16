package com.mycompany.autobackupprogram.Enums;

public enum ThemesEnum {
    LIGHT("Light"),
    DARK("Dark"),
    INTELLIJ("IntelliJ"),
    DRACULA("Darcula"),
    LIGHTMAC("macOS Light"),
    DARKMAC("macOS Dark");

    private final String themeName;

    ThemesEnum(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }
}