package musicEvent;

import java.util.ArrayList;

import Utilities.ArraysUtils;

public class Intervall {

	private int signInt = 1; // il segno dell'intervallo.
	public static final int DIMINISHED = -2, MINOR = -1, PERFECT = 0,
			MAJOR = 1, AUGMENTED = 2, UNDEFINIED = -100,
			PLUS_AUGMENTED = 3, PLUS_DIMINISHED = -3;

	/**
	 * Traspone le note sulla base della prima, facendo 
	 * si che la prima non abbia più alterazioni. Quindi
	 * cis g viene c ges
	 * @param n1
	 * @param n2
	 * @return
	 */
	private Note[] transposeNotes(Note n1, Note n2) {
		Note[] n = new Note[2];
		int alt = n1.getAlteration();
		Note tmp1 = new Note(n1.getMidiNumber()-alt, n1.getAlteration()-alt);
		Note tmp2 = new Note(n2.getMidiNumber()-alt, n2.getAlteration()-alt);
		n[0] = tmp1;
		n[1] = tmp2;
		return n;
	}
	
	/**
	 * Calcola il segno dell'intervallo e se necessario
	 * inverte le note
	 * @param n1
	 * @param n2
	 * @return
	 */
	private Note[] sortNotes(Note note1, Note note2) {
		Note n1, n2;
		if (note1.getMidiNumber() > note2.getMidiNumber()) {
			n1 = note2;
			n2 = note1;
			signInt = -1;
		} else {
			n1 = note1;
			n2 = note2;
			signInt = 1;
		}
		return new Note[]{n1, n2};
	}
	
	private int getIntervall(Note n1, Note n2) {
		int intervall = 1;
		Scale sc = new Scale(n1, Modus.MAJOR_SCALE);
		/*
		 * passa le note a una a una secondo la tabella 2 2 2 1 2 2...
		 * Sono gli intervalli della scala maggiore
		 */
		int[] tones = new int[] { 2, 2, 1, 2, 2, 2, 1 };
		int midi = n1.getMidiNumber();
		int posInArray = 0;
		/* 
		 * endMidi viene calcolato così perchè mi
		 * serve la nota senza alterazione per l'intervallo.
		 * Se avessi c deses, il programma deve calcolcare
		 * l'intervallo c-d (seconda). Quindi deve eliminare
		 * l'alterazione dalla seconda nota
		 */
		int endMidi = n2.getMidiNumber()-n2.getAlteration();
		while (true) {
			if (sc.getNote(midi).getMidiNumber() == endMidi) break; 
			if (sc.getNote(midi).getMidiNumber() > endMidi) {
				intervall--;
				break;
			}
			midi += ArraysUtils.getElementAt(tones, posInArray);
			posInArray++;
			intervall++;
		}
		return intervall;
	}
	/**
	 * Calcola l'intervallo tra <i>note1</i> e <i>note2</i>.
	 * 
	 * @param note1
	 * @param note2
	 * @return un array di int, dove la prima posizione indica
	 * l'intervallo espresso in <i>int</i>, e la
	 *         direzione dell'intervallo, e la seconda posizion
	 *         il tipo di intervallo (maggiore, minore, ecc.)
	 */
	public int[] calculateIntervall(Note note1, Note note2) {
		Note[] notes = sortNotes(note1, note2);
		notes = transposeNotes(notes[0], notes[1]);
		Note n1 = notes[0];
		Note n2 = notes[1];
		int intervall = getIntervall(n1, n2);
		int type = calculateType(n1, n2, intervall);
		return new int[]{intervall * signInt, type};
	}

	private int calculateType(Note n1, Note n2, int intervall) {

//		System.out.print(intervall+" ");
/*
 * Questo è necessario perchè se avessi
 * fatto solamente intervall%7, ogni volta che
 * era 7 veniva cambiato in 0.
 * Avevo quindi sempre 1 2 3 4 5 6 0 | 1 2 3...
 * e io volevo 1 2 3 4 5 6 7 | 1 2 3 ...
 */
		int thisIntervall;
		if (intervall%7 != 0) {
		thisIntervall = intervall%7;
		}
		else {
			thisIntervall = 7;
		}
//		System.out.println(thisIntervall);
		Scale sc = new Scale(n1, Modus.MAJOR_SCALE);
		ArrayList<Note> majorScale =  sc.getDiatonicScale();
		for (Note n : majorScale) {
			
			// controlla se la nota è nella scala maggiore
			if (n.getMidiNumber()%12 == n2.getMidiNumber()%12 &&
					n.getAlteration() == n2.getAlteration()) {
				if (is1457(thisIntervall)) return PERFECT;
				else return MAJOR;
			}
		}
		
			int diff = n2.getMidiNumber()-n1.getMidiNumber();
			diff = diff % 12;
			/*
			 * TODO
			 * un intervallo del tipo d# - gbb
			 * ritorna UNDEFINIED, perchè non 
			 * contemplato nella serie degli if - else.
			 * Non posso automatizzare?
			 */
			if (thisIntervall == 3 && diff == 3) return MINOR;
			if (thisIntervall == 3 && diff == 2) return DIMINISHED;
			if (thisIntervall == 3 && diff == 1) return PLUS_DIMINISHED;
			if (thisIntervall == 3 && diff == 5) return AUGMENTED;
			if (thisIntervall == 3 && diff == 6) return PLUS_AUGMENTED;

			if (thisIntervall == 2 && diff == 1) return MINOR;
			if (thisIntervall == 2 && diff == 0) return DIMINISHED;
			if (thisIntervall == 2 && diff == -1) return PLUS_DIMINISHED;
			if (thisIntervall == 2 && diff == 3) return AUGMENTED;
			if (thisIntervall == 2 && diff == 4) return PLUS_AUGMENTED;
			
			if (thisIntervall == 6 && diff == 8) return MINOR;
			if (thisIntervall == 6 && diff == 7) return DIMINISHED;
			if (thisIntervall == 6 && diff == 6) return PLUS_DIMINISHED;
			if (thisIntervall == 6 && diff == 10) return AUGMENTED;
			if (thisIntervall == 6 && diff == 11) return PLUS_AUGMENTED;
			
			if (thisIntervall == 7 && diff == 10) return MINOR;
			if (thisIntervall == 7 && diff == 9) return DIMINISHED;
			if (thisIntervall == 7 && diff == 8) return PLUS_DIMINISHED;
			if (thisIntervall == 7 && diff == 11) return AUGMENTED;
			if (thisIntervall == 7 && diff == 12) return PLUS_AUGMENTED;
			if (thisIntervall == 7 && diff == 0) return AUGMENTED;
			
			if (thisIntervall == 1 && diff == -1) return DIMINISHED;
			if (thisIntervall == 1 && diff == -2) return PLUS_DIMINISHED;
			if (thisIntervall == 1 && diff == 1) return AUGMENTED;
			if (thisIntervall == 1 && diff == 2) return PLUS_AUGMENTED;
			if (thisIntervall == 1 && diff == 11) return DIMINISHED;
			
			if (thisIntervall == 4 && diff == 4) return DIMINISHED;
			if (thisIntervall == 4 && diff == 3) return PLUS_DIMINISHED;
			if (thisIntervall == 4 && diff == 6) return AUGMENTED;
			if (thisIntervall == 4 && diff == 7) return PLUS_AUGMENTED;
			
			if (thisIntervall == 5 && diff == 6) return DIMINISHED;
			if (thisIntervall == 5 && diff == 5) return PLUS_DIMINISHED;
			if (thisIntervall == 5 && diff == 8) return AUGMENTED;
			if (thisIntervall == 5 && diff == 9) return PLUS_AUGMENTED;
		return UNDEFINIED;
	}

	private boolean is1457(int intervall) {
		return intervall%8 == 1 || intervall%8 ==4 || intervall%8 ==5;
	}
}
