package notation;

import musicEvent.NoteEvent;

public class Tie extends CurvedConnection {

    @Override
    public void assignToNotes(NoteEvent startNote, NoteEvent endNote) {
        this.startNote = startNote;
        this.endNote = endNote;

        startNote.setTie(this);
        endNote.setTie(this);

        startNote.tieStart();
        endNote.tieEnd();

    }
    
    public boolean isValid(Score score) {

        Voice v1 = score.getVoiceOf(startNote);
        Voice v2 = score.getVoiceOf(endNote);

        // stessa voce
        if (v1 != v2)
            return false;

        // stessa altezza grafica (o pitch)
        if (startNote.getMidiNumber() != endNote.getMidiNumber())
            return false;
        // oppure: if (startNote.getPitch() != endNote.getPitch()) return false;

        // devono essere consecutive nella voce
        return score.areNotesConsecutive(startNote, endNote);
    }


    public void detach() {
        startNote.setTie(null);
        endNote.setTie(null);
    }
    
}
