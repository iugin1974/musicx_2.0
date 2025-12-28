package notation;

import java.util.ArrayList;
import java.util.List;

import musicEvent.Note;
import musicEvent.NoteEvent;


public class Lyrics {
	private List<Lyric> lyrics = new ArrayList<>();
	private Score score;
	
	protected Lyrics(Score score) {
		this.score = score;
	}
	
	protected void addLyric(Lyric l) {
		lyrics.add(l);
	}

	protected List<Lyric> getLyrics() {
		return lyrics;
	}
	
	protected void removeLyrics(int staff, int voice, int stanza) {
		for (int i = lyrics.size() - 1; i >= 0; i--) {
			Lyric l = lyrics.get(i);
			if (l.getStaff() == staff && l.getVoice() == voice && l.getStanza() == stanza) {
				lyrics.remove(i);
				l.getParentNote().removeLyric(stanza); // rimuove dalla nota
			}
		}
	}

	protected List<Lyric> getLyrics(int staff, int voice, int stanza) {
		List<Lyric> result = new ArrayList<>();
		for (Lyric l : lyrics) {
			if (l.getStaff() == staff && l.getVoice() == voice && l.getStanza() == stanza) {
				result.add(l);
			}
		}
		return result;
	}
	
	protected void reorganizeLyrics(int staffIndex, int voiceIndex, int stanza) {
		Staff s = score.getStaff(staffIndex);
		Voice v = s.getVoice(voiceIndex);
		List<NoteEvent> notes = v.getNotes();
		List<Lyric> newLyrics = new ArrayList<>();
		for (NoteEvent ne : notes) {
			newLyrics.add(ne.getLyric(stanza));
		}
		removeLyrics(staffIndex, voiceIndex, stanza);
		
	}
}
