package Tests;

import musicEvent.ArraysUtils;
import musicEvent.Modus;
import musicEvent.NamedNote;
import musicEvent.Note;
import musicEvent.Scale;

public class TestTransposeNoteForIntervall {

	private Note[] transposeNotes(Note n1, Note n2) {
		Note[] n = new Note[2];
		int alt = n1.getAlteration();
		Note tmp1 = new Note(n1.getMidiNumber()-alt, n1.getAlteration()-alt);
		Note tmp2 = new Note(n2.getMidiNumber()-alt, n2.getAlteration()-alt);
		n[0] = tmp1;
		n[1] = tmp2;
		return n;
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
			if (sc.getNote(midi).getMidiNumber() >= endMidi) break;
			midi += ArraysUtils.getElementAt(tones, posInArray);
			posInArray++;
			intervall++;
		}
		return intervall;
	}
	private void los() {
		Note n1 = new Note (67, 2);
		Note n2 = new Note (67,0);
		NamedNote na1 = new NamedNote(n1);
		NamedNote na2 = new NamedNote(n2);
		System.out.println(na1);
		System.out.println(na2);
		System.out.println("Transpose");
		Note[] nn = transposeNotes(n1, n2);
		NamedNote nn1 = new NamedNote(nn[0]);
		NamedNote nn2 = new NamedNote(nn[1]);
		System.out.println(nn1);
		System.out.println(nn2);
		System.out.println(getIntervall(nn1, nn2));
	}
	public static void main(String[] args) {
		new TestTransposeNoteForIntervall().los();

	}

}
