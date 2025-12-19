package notation;

import java.util.Arrays;

import musicInterface.MusicObject;

public class Clef implements MusicObject {

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Clef)) return false;
        Clef other = (Clef) obj;
        return type == other.type &&
               midiOffset == other.midiOffset &&
               Arrays.equals(semitoneMap, other.semitoneMap);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + midiOffset;
        result = 31 * result + Arrays.hashCode(semitoneMap);
        return result;
    }

    // --- istanze pronte all'uso ---
    public static final Clef TREBLE = new Clef(Type.TREBLE, 64, SemitoneMap.SEMITONE_MAP_TREBLE);
    public static final Clef TREBLE_8 = new Clef(Type.TREBLE_8, 52, SemitoneMap.SEMITONE_MAP_TREBLE_8);
    public static final Clef BASS = new Clef(Type.BASS, 43, SemitoneMap.SEMITONE_MAP_BASS);
    public static final Clef ALTO = new Clef(Type.ALTO, 57, SemitoneMap.SEMITONE_MAP_ALTO);       // esempio
    public static final Clef TENOR = new Clef(Type.TENOR, 50, SemitoneMap.SEMITONE_MAP_TENOR);    // esempio
    public static final Clef SOPRANO = new Clef(Type.SOPRANO, 66, SemitoneMap.SEMITONE_MAP_SOPRANO); // esempio
    public static final Clef MEZZO_SOPRANO = new Clef(Type.MEZZO_SOPRANO, 62, SemitoneMap.SEMITONE_MAP_MEZZO_SOPRANO); // esempio
    public static final Clef BARITONE = new Clef(Type.BARITONE, 46, SemitoneMap.SEMITONE_MAP_BARITONE); // esempio
    public static final Clef PERCUSSION = new Clef(Type.PERCUSSION, -1, new int[0]); // non ha note

}