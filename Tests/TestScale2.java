package Tests;

import java.util.ArrayList;

import musicEvent.Modus;
import musicEvent.NamedNote;
import musicEvent.Note;
import musicEvent.Scale;

public class TestScale2 {

	public static void main(String[] args) {
		Scale sc = new Scale(new Note(62,0), Modus.MINOR_SCALE);
		ArrayList<Note> ln = sc.getDiatonicScale();
for (Note n : ln) {
	NamedNote nn = new NamedNote(n);
	System.out.println(nn);
}
	}

}
