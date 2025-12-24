package notation;

public class ScoreEvent {

    public enum Type {
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
        // aggiungi altri tipi se serve
    }

    private final Type type;
    private final Object source; // oggetto coinvolto
    private final int staffIndex;
    private final int voiceIndex;

    public ScoreEvent(Type type, Object source, int staffIndex, int voiceIndex) {
        this.type = type;
        this.source = source;
        this.staffIndex = staffIndex;
        this.voiceIndex = voiceIndex;
    }

    public Type getType() {
        return type;
    }

    public Object getSource() {
        return source;
    }

    public int getStaffIndex() {
        return staffIndex;
    }

    public int getVoiceIndex() {
        return voiceIndex;
    }
}
