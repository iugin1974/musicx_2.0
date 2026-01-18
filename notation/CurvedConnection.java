package notation;

import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public abstract class CurvedConnection extends MusicObject {

    protected final NoteEvent startNote;
    protected final NoteEvent endNote;

    public CurvedConnection(NoteEvent startNote, NoteEvent endNote) {
        this.startNote = startNote;
        this.endNote = endNote;
    }

    public NoteEvent getStart() {
        return startNote;
    }

    public NoteEvent getEnd() {
        return endNote;
    }
    
    public boolean isStart(NoteEvent n) {
        return n == startNote;
    }

    public boolean isEnd(NoteEvent n) {
        return n == endNote;
    }
    
    public NoteEvent getOther(NoteEvent note) {
        if (note == startNote) return endNote;
        if (note == endNote) return startNote;
        return null; 
    }

}
