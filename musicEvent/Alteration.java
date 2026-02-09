package musicEvent;

public class Alteration {
    public static final int DOUBLE_SHARP = 2;
    public static final int SHARP = 1;
    public static final int NATURAL = 0;
    public static final int FLAT = -1;
    public static final int DOUBLE_FLAT = -2;

    private int value;         // -2, -1, 0, 1, 2
    private boolean courtesy;  // alterazione di cortesia
    private boolean visible;   // se va disegnata

    public Alteration(int value) {
        this.value = value;
        this.courtesy = false;
        this.visible = true;
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public boolean isCourtesy() { return courtesy; }
    public void setCourtesy(boolean courtesy) { this.courtesy = courtesy; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
