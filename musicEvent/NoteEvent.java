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
	protected Alteration alteration = null; // null se la nota non ha alterazione scritta
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

	public void addFlat() {
	    if (alteration.equals(Alteration.NATURAL)) {
	        alteration.setValue(Alteration.FLAT);
	    } else if (alteration.getValue() > Alteration.DOUBLE_FLAT) {
	        alteration.setValue(alteration.getValue() - 1);
	    } else {
	        return; // già DOUBLE_FLAT
	    }
	    midiNumber--;
	}


	public void addSharp() {
	    if (alteration.equals(Alteration.NATURAL)) {
	    	alteration.setValue(Alteration.SHARP);
	    } else if (alteration.getValue() < Alteration.DOUBLE_SHARP) {
	        alteration.setValue(alteration.getValue() + 1);
	    } else {
	        return; // già DOUBLE_SHARP
	    }
	    midiNumber++;
	}

	public void setAlteration(Alteration alt) {
	    this.alteration = alt;
	}

	public Alteration getAlteration() {
	    return alteration;
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
	
	public void removeCurvedConnection(CurvedConnection c) {
	        connections.remove(c);
	    }
	
	public List<CurvedConnection> getCurvedConnections() {
		return connections;
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
	
	public void setStaffPosition(int p) {
		staffPosition = p;
		System.out.println("Staff position: " +p);
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
