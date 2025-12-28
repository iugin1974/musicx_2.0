package notation;

import musicInterface.MusicObject;

public class ScoreEvent {

    public enum Type {
        OBJECT_ADDED,
        OBJECT_REMOVED,

        NOTE_ADDED,
        NOTE_REMOVED,

        REST_ADDED,
        REST_REMOVED,

        STAFF_ADDED,
        STAFF_REMOVED,

        VOICE_ADDED,
        VOICE_REMOVED,

        LYRIC_ADDED,
        LYRIC_REMOVED,

        BARLINE_ADDED,
        BARLINE_CHANGED,
        BARLINE_REMOVED,

        CLEF_ADDED,
        CLEF_REMOVED,
        CLEF_CHANGED,

        TIME_SIGNATURE_ADDED,
        TIME_SIGNATURE_REMOVED,
        TIME_SIGNATURE_CHANGED,

        KEY_SIGNATURE_ADDED,
        KEY_SIGNATURE_REMOVED,
        KEY_SIGNATURE_CHANGED
    }

    private final Type type;

    // 🔹 uno solo di questi due è valorizzato
    private final MusicObject musicObject;
    private final Staff staff;

    private final int staffIndex;
    private final int voiceIndex;

    /* =======================
     * COSTRUTTORI
     * ======================= */

    /** Eventi che riguardano uno STAFF */
    public ScoreEvent(Type type, Staff staff, int staffIndex) {
        this.type = type;
        this.staff = staff;
        this.staffIndex = staffIndex;

        this.musicObject = null;
        this.voiceIndex = -1;
    }

    /** Eventi che riguardano un MUSIC OBJECT */
    public ScoreEvent(Type type, MusicObject musicObject,
                      int staffIndex, int voiceIndex) {
        this.type = type;
        this.musicObject = musicObject;
        this.staffIndex = staffIndex;
        this.voiceIndex = voiceIndex;

        this.staff = null;
    }

    /* =======================
     * GETTERS
     * ======================= */

    public Type getType() {
        return type;
    }

    public MusicObject getMusicObject() {
        return musicObject;
    }

    public Staff getStaff() {
        return staff;
    }

    public int getStaffIndex() {
        return staffIndex;
    }

    public int getVoiceIndex() {
        return voiceIndex;
    }

    /* =======================
     * METODI DI UTILITÀ
     * ======================= */

    public boolean isStaffEvent() {
        return staff != null;
    }

    public boolean isMusicObjectEvent() {
        return musicObject != null;
    }
}
