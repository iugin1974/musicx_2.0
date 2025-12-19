package musicEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import musicInterface.MusicObject;

public class Chord extends NoteEvent implements Serializable, MusicObject  {
	private ArrayList<Note> notes = new ArrayList<Note>();

	@Override
	public Chord getCopy() {
		Chord copy = new Chord();
		for (int i=0; i<notes.size(); i++) {
			copy.addNote(notes.get(i));
		}
		copy.setDuration(duration);
		copy.setDots(dots);
		
		return copy;
	}
	/**
	 * Setta la durata dell'evento di base (cioè senza i punti di valore)
	 * espressa in <i>int</i>.
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
		for (int i=0; i<size(); i++) {
			getNoteAtPos(i).setDuration(duration);
		}
	}
	
	/**
	 * Aggiunge il o i punti di valore all'evento.
	 * @param dots
	 */
	public void setDots(int dots) {
		if (dots < 0)
			dots = 0;
		this.dots = dots;
		for (int i=0; i<size(); i++) {
			getNoteAtPos(i).setDots(dots);
		}
	}

	/**
	 * Aggiunge un punto di valore all'evento
	 */
	public void addDot() {
		dots++;
		for (int i=0; i<size(); i++) {
			getNoteAtPos(i).setDots(dots);
		}
	}
	
	/**
	 * Rimuove un punto di valore dall'evento
	 */
	public void removeDot() {
		if (dots>0) dots--;
		for (int i=0; i<size(); i++) {
			getNoteAtPos(i).setDots(dots);
		}
	}
	
	public void addNote(Note note) {
		notes.add(note);
		setDuration(note.getDuration());
		setDots(note.getDots());
		sort();
		setMidiNumber(getNoteAtPos(0).getMidiNumber());
	}

	private void sort() {
		Collections.sort(notes);
	}

	public Note getNoteAtPos(int pos) {
		return notes.get(pos);
	}

	public int size() {
		return notes.size();
	}

	public void setMidiNumber(int midiNumber) {
		this.midiNumber = midiNumber;
	}

	public void setAlteration(int alteration) {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).setAlteration(i);
		}
	}

	public void addFlat() {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).addFlat();
		}
	}

	public void addSharp() {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).addSharp();
		}
	}

//	@Override
//	public String getName() {
//		return notes.get(0).getName();
//	}

	public int[] getMidiNumbers() {
		int n[] = new int[size()];
		for (int i=0; i<size(); i++) {
			n[i] = getNoteAtPos(i).getMidiNumber();
		}
		return n;
	}

}
