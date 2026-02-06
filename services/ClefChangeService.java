package services;

import java.util.List;

import musicEvent.Note;
import musicEvent.NoteEvent;
import notation.Clef;
import notation.KeySignature;
import notation.Score;
import notation.MidiPitch;
import notation.StaffMapper;

public class ClefChangeService {

	private final Score score;

    public ClefChangeService(Score score) {
        this.score = score;
    }
    
	public void commitClefChange(Clef clef) {
		int staffIndex = clef.getStaffIndex();
		int tick1 = clef.getTick();
		Clef nextClef = score.getNextObjectOfType(staffIndex, tick1, Clef.class);
		List<NoteEvent> list;
		if (nextClef != null) {
			int tick2 = nextClef.getTick();
		list = score.getAllNotesBetween(staffIndex, tick1, tick2);
		}
		else
			list = score.getAllNotesAfter(staffIndex, tick1);
		recalculateMidi(list, clef);
	}
	
	public void commitClefRemove(Clef clef) {
		int staffIndex = clef.getStaffIndex();
		int tick = clef.getTick();
		Clef previousClef = score.getPreviousObjectOfType(staffIndex, tick, Clef.class);
		int tick1 = previousClef.getTick();
		Clef nextClef = score.getNextObjectOfType(staffIndex, tick1, Clef.class);
		int tick2;
		if (nextClef != null) {
			tick2 = nextClef.getTick();
		}
		else {
			Note n = score.getLastObjectOfType(staffIndex, tick1, Note.class);
			tick2 = n.getTick();
		}
		List<NoteEvent> list = score.getAllNotesBetween(staffIndex, tick1, tick2);
		recalculateMidi(list, previousClef);
		// TODO cosa faccio se non c'è una chiave precedente?
		// L'utente può cancellare la prima chiave?
		
	}
	
	private void recalculateMidi(List<NoteEvent> list, Clef clef) {
		for (NoteEvent n : list) {
			int staffIndex = clef.getStaffIndex();
			KeySignature ks = score.getPreviousObjectOfType(staffIndex, n.getTick(), KeySignature.class);
			MidiPitch midi = StaffMapper.staffPositionToMidi(n.getStaffPosition(), clef, ks);
			n.setMidiNumber(midi.getMidiNumber());
			n.setAlteration(midi.getAlteration());
		}
	}
	
}
