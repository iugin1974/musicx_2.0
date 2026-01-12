package musicEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import musicInterface.MusicObject;
import notation.CurvedConnection;
import notation.Lyric;
import notation.Slur;
import notation.Tie;


/**
 * Una classe rappresentate un evento musicale che può essere o una nota
 * o un accordo.
 * @author eugenio
 *
 */
public abstract class NoteEvent extends MusicEvent {

	protected int midiNumber = -1;
	protected int alteration = 0;
	private Map<Integer, Lyric> lyrics = null;
	private int staffPosition;
	private boolean lyricExtender = false;
	private boolean syllableDivision = false;
	private boolean skipText;
	private List<CurvedConnection> connections = new ArrayList<>();


	protected NoteEvent() {
	}
	/**
	 * @return -1 se il numero MIDI non è stato settato.
	 */
	public int getMidiNumber() {
		return midiNumber;
	}
	
	public void setMidiNumber(int midi) {
		if (midi < 0 || midi > 127) return;
		midiNumber = midi;
	}

	public int getOctave() {
		return midiNumber / 12;
	}

	public void setAlteration(int alteration) {
		this.alteration = alteration;
	}

	public int getAlteration() {
		return alteration;
	}

	public void addFlat() {
		if (alteration <= -2)
			return;
		alteration--;
		midiNumber--;
	}

	public void addSharp() {
		if (alteration >= 2)
			return;
		alteration++;
		midiNumber++;
	}

	
	/**
	 * L'alterazione serve per stabilire se la nota ha un diesis
	 * o un bemolle
	 * @param alt
	 */
	public void alterate(int alt) {
		alteration += alt;
		midiNumber += alt;
	}
	
	public void addLyric(Lyric lyric) {
		if (lyrics == null) lyrics = new HashMap<>();
		lyrics.put(lyric.getStanza(), lyric);
	}
	
	public Lyric getLyric(int stanza) {
		if (lyrics == null) return null;
		return lyrics.get(stanza);
	}
	
	public int getNumberOfStanzas() {
		if (lyrics == null) return 0;
		return lyrics.size();
	}
	
	public boolean hasLyric() {
	    return lyrics != null && !lyrics.isEmpty();
	}

	public void removeLyric() {
		lyrics = null;
	}

	public void removeLyric(int stanza) {
	    if (lyrics != null) {
	        lyrics.remove(stanza);

	        // Se la nota non ha più lyric → mette a null
	        if (lyrics.isEmpty()) {
	            lyrics = null;
	        }
	    }
	}
	
	public void addCurvedConnection(CurvedConnection c) {
		connections.add(c);
	}
	
	public void removeConnection(CurvedConnection c) {
	        connections.remove(c);
	    }
	
	public boolean isTied() {
		for (CurvedConnection c : connections) {
			if (c instanceof Tie) return true;
		}
		return false;
	}
	
	public List<Tie> getTies() {
	    List<Tie> ties = new ArrayList<>();
	    for (CurvedConnection c : connections) {
	        if (c instanceof Tie) {
	            ties.add((Tie) c);
	        }
	    }
	    return ties;
	}
	
	public List<Slur> getSlurs() {
	    List<Slur> slurs = new ArrayList<>();
	    for (CurvedConnection c : connections) {
	        if (c instanceof Slur) {
	            slurs.add((Slur) c);
	        }
	    }
	    return slurs;
	}
	  
//	
//	public void slurStart() {
//		slurStart = true;
//	}
//
//	public void slurEnd() {
//		slurEnd = true;
//	}
//
//	public void slurNone() {
//		slurStart = slurEnd = false;
//	}
//
//	public void tieStart() {
//		tieStart = true;
//	}
//
//	public void tieEnd() {
//		tieEnd = true;
//	}
//
//	public void tieNone() {
//		tieStart = tieEnd = false;
//	}
//
//	public boolean isSlurStart() {
//		return slurStart;
//	}
//
//	public boolean isSlurEnd() {
//		return slurEnd;
//	}
//
//	public boolean isTiedStart() {
//		return tieStart;
//	}
//
//	public boolean isTiedEnd() {
//		return tieEnd;
//	}
//	
//	public void setSlur(Slur slur) {
//		this.slur = slur;
//	}
//
//	public void setTie(Tie tie) {
//		this.tie = tie;
//	}
//
//	public Slur getSlur() {
//		return slur;
//	}
//
//	public Tie getTie() {
//		return tie;
//	}
//	
//	public boolean isCurveStart() {
//		return isTiedStart() || isSlurStart();
//	}
//
//	public boolean isCurveEnd() {
//		return isTiedEnd() || isSlurEnd();
//	}
//	
	public void setStaffPosition(int p) {
		staffPosition = p;
	}
	
	public int getStaffPosition() {
		return staffPosition;
	}
	
	public void setLyricExtender(boolean le) {
	lyricExtender = le;
	}
	
	public void setSyllableDivision(boolean sd) {
		syllableDivision = sd;
	}
	
	public boolean hasLyricExtender() {
		return lyricExtender;
	}
	
	public boolean hasSyllableDivision() {
		return syllableDivision;
	}
	
	public void setSkipText(boolean st) {
		skipText = st;
	}
	
	public boolean isSkipText() {
		return skipText;
	}
}
