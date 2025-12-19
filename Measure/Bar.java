package Measure;

import musicInterface.MusicObject;

public class Bar implements MusicObject {

    public enum Type {
        NORMAL, DOUBLE, END, BEGIN_REPEAT, END_REPEAT
    }

    private Type type;
    private String bar = " | "; // compatibilità con vecchio codice

    public Bar() {
        this.type = Type.NORMAL;
    }

    public void setEndBar() {
        type = Type.END;
        bar = " \\bar \"|.\" ";
    }

    public void setDoubleBar() {
        type = Type.DOUBLE;
        bar = " \\bar \"||\" ";
    }

    public void setBeginRepeatBar() {
        type = Type.BEGIN_REPEAT;
        bar = " \\bar \"|:\" ";
    }

    public void setEndRepeatBar() {
        type = Type.END_REPEAT;
        bar = " \\bar \":|\" ";
    }

    private void setNormalBar() {
        type = Type.NORMAL;
        bar = " | ";
    }

    public String getBar() {
        return bar;
    }

    // Nuovo metodo per LilyPond moderno
    public String toLilypond() {
        return bar;
    }

    public Type getType() {
        return type;
    }

}
