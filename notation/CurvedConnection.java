package notation;

import musicEvent.NoteEvent;

public abstract class CurvedConnection {

    protected NoteEvent startNote;
    protected NoteEvent endNote;

    public abstract void assignToNotes(NoteEvent startNote, NoteEvent endNote);

    // -----------------------------
    //     GETTER PER LE NOTE
    // -----------------------------
    public NoteEvent getStartNote() {
        return startNote;
    }

    public NoteEvent getEndNote() {
        return endNote;
    }

    public void removeFromNotes() {
        if (this instanceof Slur) {
            if (startNote != null) {
                startNote.setSlur(null);
                startNote.slurNone();
            }
            if (endNote != null) {
                endNote.setSlur(null);
                endNote.slurNone();
            }
        } else if (this instanceof Tie) {
            if (startNote != null) {
                startNote.setTie(null);
                startNote.tieNone();
            }
            if (endNote != null) {
                endNote.setTie(null);
                endNote.tieNone();
            }
        }
    }
}
