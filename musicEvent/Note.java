package musicEvent;

import java.io.Serializable;

import musicInterface.MusicObject;
import notation.CurvedConnection;
import notation.Lyric;

/*
 * L'utente pu?o creare una nota con la frequenza
 * ma non pu?o crearne una col nome.
 * Il nome viene dato dalla tonalità.
 * L'utente può richiedere una nota dalla scala
 * e alterarla. Il nome cambia in base alla scala.
 * L'utente può fare enarmonia. Il nome cambia.
 * L'utente NON PUÒ cambiare il nome direttamente.
 */

/**
 * Una classe rappresentante una nota musicale.
 * 
 * @author eugenio
 * 
 */
public class Note extends NoteEvent implements Comparable<Note>, Serializable {

	private boolean needsAccidental = false;
	public Note() {
		
	}
	/**
	 * Costruisce in base al numero midi e di durata 1/4.
	 * 
	 * @param MIDI
	 *            il numero midi dell'altezza
	 */
	public Note(int midi) {
		this.midiNumber = midi;
		this.duration = 2; // 2 è un quarto
		this.dots = 0;
	}

	/**
	 * Costruisce una nota di 1/4 (default) in base al numero midi e all'alterazione.
	 * L'alterazione indica se la nota ha un diesis o un bemolle. 
	 * (null = nessuna alterazione scritta, deriva da chiave/battuta)
	 *
	 * @param midi numero MIDI della nota
	 * @param alteration alterazione come oggetto Alteration o null
	 */
	public Note(int midi, Alteration alteration) {
	    this.alteration = alteration;
	    this.midiNumber = midi;
	    this.duration = 2;
	    this.dots = 0;
	}

	/**
	 * Costruisce una nota di valore duration in base al numero midi e all'alterazione.
	 *
	 * @param midi numero MIDI della nota
	 * @param alteration alterazione come oggetto Alteration o null
	 * @param duration durata della nota (2 = 1/4)
	 */
	public Note(int midi, Alteration alteration, int duration) {
	    this.alteration = alteration;
	    this.midiNumber = midi;
	    this.duration = duration;
	    this.dots = 0;
	}

	/**
	 * Costruisce una nota con durata e numero di puntini.
	 *
	 * @param midi numero MIDI della nota
	 * @param alteration alterazione come oggetto Alteration o null
	 * @param duration durata
	 * @param dots numero di puntini
	 */
	public Note(int midi, Alteration alteration, int duration, int dots) {
	    this.alteration = alteration;
	    this.midiNumber = midi;
	    this.duration = duration;
	    this.dots = dots;
	}


	@Override
	public Note getCopy() {
	    Alteration altCopy =  new Alteration(alteration.getValue());
	    
	    Note copy = new Note(midiNumber, altCopy, duration, dots);
	    copy.setTick(this.getTick());
	    copy.setStaffPosition(this.getStaffPosition());
	    copy.setLyricExtender(this.hasLyricExtender());
	    copy.setSyllableDivision(this.hasSyllableDivision());
	    copy.setSkipText(this.isSkipText());
	    
	    // copia del flag needsAccidental
	    copy.needsAccidental = this.needsAccidental;
//TODO q	quello qua sotto
//	    // copia lyrics
//	    if (this.hasLyric()) {
//	        for (int stanza = 0; stanza < this.getNumberOfStanzas(); stanza++) {
//	            Lyric lyric = this.getLyric(stanza);
//	            if (lyric != null) copy.addLyric(lyric.copy()); // assume Lyric ha metodo copy()
//	        }
//	    }

//	    // copia curved connections
//	    for (CurvedConnection c : this.getCurvedConnections()) {
//	        copy.addCurvedConnection(c.copy()); // assume CurvedConnection ha metodo copy()
//	    }

	    return copy;
	}


	
	public boolean equalsHeight(Note note) {
		return midiNumber == note.getMidiNumber();
	}

	/**
	 * @param note
	 * @return true se le due note hanno la stessa durata
	 */
	public boolean equalsDuration(NoteEvent note) {
		if (duration == note.getDuration())
			return true;
		return false;
	}

	/**
	 * Compara due note in base alla loro altezza (il numero MIDI).
	 */
	@Override
	public int compareTo(Note n) {
		if (n.getMidiNumber() > this.midiNumber)
			return -1;
		if (n.getMidiNumber() < this.midiNumber)
			return 1;
		return 0;
	}

	/**
	 * @param note
	 * @return <i>true</i> se le due note sono uguali.
	 */
	public boolean equals(Note note) {
		if (alteration != note.getAlteration())
			return false;
		if (duration != note.getDuration())
			return false;
		if (midiNumber != note.getMidiNumber())
			return false;
		return true;
	}

	@Override
	public String toString() {
	    String symbol = "";

	    if (alteration != null) {
	        switch (alteration.getValue()) {
	            case Alteration.DOUBLE_FLAT:  symbol = "♭♭"; break;
	            case Alteration.FLAT:         symbol = "♭";  break;
	            case Alteration.NATURAL:      symbol = "♮";  break;
	            case Alteration.SHARP:        symbol = "♯";  break;
	            case Alteration.DOUBLE_SHARP: symbol = "𝄪";  break;
	            default:                      symbol = "?";  break;
	        }
	    }

	    return " [" + midiNumber + "; " + (alteration != null ? alteration.getValue() : 0) + "]" + symbol;
	}

}
