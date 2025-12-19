package notation;

import java.util.ArrayList;
import java.util.List;

import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class Slur extends CurvedConnection {

    @Override
    public void assignToNotes(NoteEvent startNote, NoteEvent endNote) {
        this.startNote = startNote;
        this.endNote = endNote;

        startNote.setSlur(this);
        endNote.setSlur(this);

        startNote.slurStart();
        endNote.slurEnd();
    }
    
    public List<NoteEvent> getNotesUnderSlur(Score score) {
        List<NoteEvent> notes = new ArrayList<>();

        Voice voice = score.getVoiceOf(startNote);
        if (voice == null) 
            return notes;

        List<MusicObject> objs = voice.getObjects();

        boolean inside = false;

        for (MusicObject obj : objs) {

            if (obj == startNote) {
                inside = true;
                notes.add(startNote);
                continue;
            }

            if (!inside)
                continue;

            if (obj instanceof NoteEvent note) {
                notes.add(note);

                if (note == endNote)
                    break;
            }
        }

        return notes;
    }

}
