package Tests;

import java.util.ArrayList;

import musicEvent.KeyAlteration;
import musicEvent.Modus;
import musicEvent.Note;

public class TestKeyAlteration2 {

	public static void main(String[] args) {
		KeyAlteration ka = new KeyAlteration();
		ArrayList<Note> n = ka.getAlteredNotes(new Note(61,1), Modus.MAJOR_SCALE);

		for (Note nn : n) System.out.println(nn);
	}

}
