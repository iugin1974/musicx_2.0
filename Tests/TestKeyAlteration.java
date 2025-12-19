package Tests;

import java.util.ArrayList;

import musicEvent.KeyAlteration;
import musicEvent.Modus;
import musicEvent.Note;

public class TestKeyAlteration {

	private void los() {
		ArrayList<Integer> notes;
		KeyAlteration ka = new KeyAlteration();
		notes = ka.getAlteredNoteNumbers(new Note(11,0), Modus.MAJOR_SCALE);
		System.out.println(notes.size() + " alterazioni.");
		for (int i : notes)
			System.out.print(i + " ");
		System.out.println("\n");

	}

	public static void main(String[] a) {
		TestKeyAlteration ka = new TestKeyAlteration();
		ka.los();
	}
}
