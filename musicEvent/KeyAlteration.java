package musicEvent;

import java.util.ArrayList;

import Utilities.ArraysUtils;

/**
 * Calcola le alterazioni di una scala maggiore o minore.
 * 
 * @author ebraun
 * 
 */
public class KeyAlteration {

	/*
	 * Due array contenenti le toniche delle tonalità con # e b, sempre partendo
	 * da 0. Es: majorSharpKey: 7=sol, 2=re, 9=la ecc.
	 */
	private int[] majorFlatKey = { 5, 10, 3, 8, 1, 6 }; // tonalità maggiori con
	// b
	private int[] majorSharpKey = { 7, 2, 9, 4, 11, 6, 1 }; // tonalità maggiori
	// con #
	private int[] minorFlatKey = { 2, 7, 0, 5, 10, 3 }; // tonalità minori con
	// b
	private int[] minorSharpKey = { 4, 11, 6, 1, 8, 3, 10 }; // tonalità minori

	// con #

	// /////////////////////
	// PUBLIC //
	// /////////////////////

	/**
	 * Ritorna un <i>ArrayList</i> comprendente i numeri delle note alterate in
	 * una scala. Re maggiore restituirà 6 e 1, cioè f# e c#.
	 * 
	 * @param tonic
	 * @param modus
	 * @return
	 */
	public ArrayList<Integer> getAlteredNoteNumbers(Note tonic, Modus modus) {

		int alterationType = ScaleType.getScaleAlterationType(tonic, modus);
		ArrayList<Integer> notes = new ArrayList<>();
		// traporta il numero MIDI della nota nella prima ottava
		int midi = tonic.getMidiNumber() % 12;
		if (midi == 0 && modus == Modus.MAJOR_SCALE)
			return notes; // do maggiore
		if (midi == 9 && modus == Modus.MINOR_SCALE)
			return notes; // la minore

		if (alterationType == Alteration.FLAT)
			getFlats(notes, midi, modus);
		else if (alterationType == Alteration.SHARP)
			getSharps(notes, midi, modus);

		return notes;
	}

	public ArrayList<Note> getAlteredNotes(Note tonic, Modus modus) {
		ArrayList<Note> notes = new ArrayList<>();
		ArrayList<Integer> numbers = getAlteredNoteNumbers(tonic, modus);
		int alterationType = ScaleType.getScaleAlterationType(tonic, modus);

		for (int i = 0; i < numbers.size(); i++) {
			notes.add(new Note(numbers.get(i), alterationType));
		}
		return notes;
	}

	private void getSharps(ArrayList<Integer> notes, int midi, Modus modus) {
		// individua la posizione della nota nell'array
		int pos = -1;
		if (modus == Modus.MAJOR_SCALE) {
			pos = ArraysUtils.containsElement(majorSharpKey, midi);
		} else if (modus == Modus.MINOR_SCALE) {
			pos = ArraysUtils.containsElement(minorSharpKey, midi);
		}
		int i = 0;
		while (i <= pos) {
			notes.add(majorSharpKey[i] - 1);
			i++;
		}
	}

	private void getFlats(ArrayList<Integer> notes, int midi, Modus modus) {
		// individua la posizione della nota nell'array
		int pos = -1;
		if (modus == Modus.MAJOR_SCALE) {
			pos = ArraysUtils.containsElement(majorFlatKey, midi);
		} else if (modus == Modus.MINOR_SCALE) {
			pos = ArraysUtils.containsElement(minorFlatKey, midi);
		}
		int i = 0;
		while (i <= pos) {
			notes.add(majorFlatKey[(i + 1) % majorFlatKey.length]);
			i++;
		}
	}

}
