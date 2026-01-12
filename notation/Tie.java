package notation;

import musicEvent.NoteEvent;

public class Tie extends CurvedConnection {

	    private Tie(NoteEvent startNote, NoteEvent endNote) {
	        super(startNote, endNote);
	    }

	    public static Tie createIfValid(Score score, NoteEvent start, NoteEvent end) {
	        if (!isValid(score, start, end)) {
	            return null;
	        }
	        return new Tie(start, end);
	    }

	    public static boolean isValid(Score score, NoteEvent start, NoteEvent end) {
	        Voice v1 = score.getVoiceOf(start);
	        Voice v2 = score.getVoiceOf(end);

	        if (v1 != v2) return false;
	        if (start.getStaffPosition() != end.getStaffPosition()) return false;
	        if (start.getAlteration() != end.getAlteration()) return false;
	        return score.areNotesConsecutive(start, end);
	    }
	}
