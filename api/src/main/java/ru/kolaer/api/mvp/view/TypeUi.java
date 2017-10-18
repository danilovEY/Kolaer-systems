package ru.kolaer.api.mvp.view;

/**
 * Created by danilovey on 16.10.2017.
 */
public enum TypeUi {
    HIGH("JavaFx"), MEDIUM("Swing"), LOW("Awt");

    private final String nameUiLib;

    TypeUi(String nameUiLib) {
        this.nameUiLib = nameUiLib;
    }

    public String getNameUiLib() {
        return nameUiLib;
    }
}
