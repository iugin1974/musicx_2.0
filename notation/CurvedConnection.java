package notation;

import musicEvent.NoteEvent;

public abstract class CurvedConnection {

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

}
