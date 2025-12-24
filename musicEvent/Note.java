package musicEvent;

import java.io.Serializable;

import musicInterface.MusicObject;

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
	 * Costruisce una nota di 1/4 (default) in base al numero midi e
	 * all'alterazione. L'alterazione indica solamente se la nota ha un diesis o
	 * un bemolle. Questo vuol dire che la nota (66,1) sarà considerata come il
	 * tasto nero tra fa e sol. Alterazione 1 vuol dire diesis. La nota sarà
	 * dunque un f#.<br>
	 * Al contrario (66,-1) sarà un solb.<br>
	 * (66,0) non esiste.
	 * 
	 * @param midi
	 *            il numero midi della nota
	 * @param alteration
	 *            l'alterazione scelta tra <i>Alteration.SHARP</i> o
	 *            <i>Alteration.FLAT</i>.
	 */
	public Note(int midi, int alteration) {
		this.alteration = alteration;
		this.midiNumber = midi;
		this.duration = 2;
		this.dots = 0;
	}

	/**
	 * Costruisce una nota di valore <i>duration</i> in base al numero midi e
	 * all'alterazione. L'alterazione indica solamente se la nota ha un diesis o
	 * un bemolle. Questo vuol dire che la nota (66,1) sarà considerata come il
	 * tasto nero tra fa e sol. Alterazione 1 vuol dire diesis. La nota sarà
	 * dunque un f#.<br>
	 * Al contrario (66,-1) sarà un solb.<br>
	 * (66,0) non esiste.
	 * 
	 * @param midi
	 *            il numero midi della nota
	 * @param alteration
	 *            l'alterazione scelta tra <i>Alteration.SHARP</i> o
	 *            <i>Alteration.FLAT</i>.
	 * @param duration
	 *            La durata dell'evento calcolata nel seguente modo:<br>
	 *            0 = 4/4<br>
	 *            1 = 2/4<br>
	 *            2 = 1/4<br>
	 *            ecc.
	 */
	public Note(int midi, int alteration, int duration) {
		this.alteration = alteration;
		this.midiNumber = midi;
		this.duration = duration;
		this.dots = 0;
	}

	/**
	 * Costruisce una nota di valore <i>duration</i> in base al numero midi e
	 * all'alterazione. L'alterazione indica solamente se la nota ha un diesis o
	 * un bemolle. Questo vuol dire che la nota (66,1) sarà considerata come il
	 * tasto nero tra fa e sol. Alterazione 1 vuol dire diesis. La nota sarà
	 * dunque un f#.<br>
	 * Al contrario (66,-1) sarà un solb.<br>
	 * (66,0) non esiste.
	 * 
	 * @param midi
	 *            il numero midi della nota
	 * @param alteration
	 *            l'alterazione scelta tra <i>Alteration.SHARP</i> o
	 *            <i>Alteration.FLAT</i>.
	 * @param duration
	 *            La durata dell'evento calcolata nel seguente modo:<br>
	 *            0 = 4/4<br>
	 *            1 = 2/4<br>
	 *            2 = 1/4<br>
	 *            ecc.
	 */
	public Note(int midi, int alteration, int duration, int dots) {
		this.alteration = alteration;
		this.midiNumber = midi;
		this.duration = duration;
		this.dots = dots;
	}

	@Override
	public Note getCopy() {
		return new Note(midiNumber, alteration, duration, dots);
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
		return " [" + midiNumber + "; " + alteration + "]";
	}

}
