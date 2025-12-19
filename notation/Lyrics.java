package notation;

import java.util.ArrayList;
import java.util.List;


public class Lyrics {
	private List<Lyric> lyrics = new ArrayList<>();

	public void addLyric(Lyric l) {
		lyrics.add(l);
	}

	public List<Lyric> getLyrics() {
		return lyrics;
	}
	
	public void removeLyrics(int staff, int voice, int stanza) {
		for (int i = lyrics.size() - 1; i >= 0; i--) {
			Lyric l = lyrics.get(i);
			if (l.getStaff() == staff && l.getVoice() == voice && l.getStanza() == stanza) {
				lyrics.remove(i);
				l.getParentNote().removeLyric(stanza); // rimuove dalla nota
			}
		}
	}

	public List<Lyric> getLyrics(int staff, int voice, int stanza) {
		List<Lyric> result = new ArrayList<>();
		for (Lyric l : lyrics) {
			if (l.getStaff() == staff && l.getVoice() == voice && l.getStanza() == stanza) {
				result.add(l);
			}
		}
		return result;
	}
}
