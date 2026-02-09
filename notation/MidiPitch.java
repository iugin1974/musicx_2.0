package notation;

import musicEvent.Alteration;

public final class MidiPitch {
    private final int midiNumber;
    private final Alteration alteration;

    public MidiPitch(int midiNumber, Alteration alteration) {
        this.midiNumber = midiNumber;
        this.alteration = alteration;
    }

    public int getMidiNumber() {
        return midiNumber;
    }

    public Alteration getAlteration() {
        return alteration;
    }

    @Override
    public String toString() {
    	return "["+midiNumber+", "+alteration.getValue()+"]";
    }
}
