package notation;

import java.util.Arrays;

import musicInterface.MusicObject;

public class Clef extends MusicObject {

    public enum Type {
        TREBLE,
        TREBLE_8,
        BASS,
        ALTO,
        TENOR,
        SOPRANO,
        MEZZO_SOPRANO,
        BARITONE,
        PERCUSSION
    }

    private final Type type;
    private final int midiOffset;       // offset MIDI della nota di riferimento (1. riga in basso del pentagramma)
    private final int[] semitoneMap;    // mappa semitoni per la chiave, se serve

    public Clef(Type type, int midiOffset, int[] semitoneMap) {
        this.type = type;
        this.midiOffset = midiOffset;
        this.semitoneMap = semitoneMap;
    }

    // --- getter ---
    public Type getType() {
        return type;
    }

    public int getMidiOffset() {
        return midiOffset;
    }

    public int[] getSemitoneMap() {
        return semitoneMap;
    }

    // --- equals / hashCode ---
    @Override
    public boolean musicallyEquals(MusicObject obj) {
        if (!(obj instanceof Clef)) return false;
        return this.type == ((Clef) obj).type;
    }

    // --- istanze pronte all'uso ---
    public static Clef treble() {
        return new Clef(Type.TREBLE, 64, SemitoneMap.SEMITONE_MAP_TREBLE);
    }

    public static Clef treble8() {
        return new Clef(Type.TREBLE_8, 52, SemitoneMap.SEMITONE_MAP_TREBLE_8);
    }

    public static Clef bass() {
        return new Clef(Type.BASS, 43, SemitoneMap.SEMITONE_MAP_BASS);
    }

    public static Clef alto() {
        return new Clef(Type.ALTO, 57, SemitoneMap.SEMITONE_MAP_ALTO);
    }

    public static Clef tenor() {
        return new Clef(Type.TENOR, 50, SemitoneMap.SEMITONE_MAP_TENOR);
    }

    public static Clef soprano() {
        return new Clef(Type.SOPRANO, 66, SemitoneMap.SEMITONE_MAP_SOPRANO);
    }

    public static Clef mezzoSoprano() {
        return new Clef(Type.MEZZO_SOPRANO, 62, SemitoneMap.SEMITONE_MAP_MEZZO_SOPRANO);
    }

    public static Clef baritone() {
        return new Clef(Type.BARITONE, 46, SemitoneMap.SEMITONE_MAP_BARITONE);
    }

    public static Clef percussion() {
        return new Clef(Type.PERCUSSION, -1, new int[0]);
    }


}