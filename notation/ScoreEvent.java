package notation;

import java.util.List;

import musicEvent.MusicEvent;
import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class ScoreEvent {

    public enum Type {
        OBJECT_ADDED,
        OBJECT_REMOVED,
        OBJECT_CHANGED,
        
        STAFF_ADDED,
        STAFF_REMOVED,

        VOICE_ADDED,
        VOICE_REMOVED
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
    public ScoreEvent(Type type) {
    	 this.type = type;
         this.staff = null;
         this.staffIndex = -1;

         this.musicObject = null;
         this.voiceIndex = -1;
    }
    
    /** Eventi che riguardano uno STAFF */
    public ScoreEvent(Type type, Staff staff, int staffIndex) {
        this.type = type;
        this.staff = staff;
        this.staffIndex = staffIndex;

        this.musicObject = null;
        this.voiceIndex = -1;
    }

    /** Eventi che riguardano un MUSIC OBJECT */
    public ScoreEvent(Type type, MusicObject musicObject) {
        this.type = type;
        this.musicObject = musicObject;
        this.staffIndex = musicObject.getStaffIndex();
        this.voiceIndex = musicObject.getVoiceIndex();

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
