package Tests;

import java.util.ArrayList;
import java.util.List;

import musicEvent.Note;
import musicEvent.NoteEvent;
import notation.Lyric;
import notation.Score;
import notation.Slur;
import notation.Staff;
import notation.Syllable;

public class CurvedConnectionTest {

	private void go() {
		// Note esistenti
		Note n1, n2, n3, n4, n5, n6;
		n1 = new Note();
		n2 = new Note();
		n3 = new Note();
		n4 = new Note();
		n5 = new Note();
		n6 = new Note();

		n1.setTick(10);
		n2.setTick(50);
		n3.setTick(100);
		n4.setTick(150);
		n5.setTick(200);
		n6.setTick(250);

		n1.setStaffPosition(4);
		n2.setStaffPosition(5); // hai scritto setStaff, meglio usare setStaffPosition per coerenza
		n3.setStaffPosition(6);
		n4.setStaffPosition(3);
		n5.setStaffPosition(4);
		n6.setStaffPosition(5);

		n1.setMidiNumber(100);
		n4.setMidiNumber(90);
		n5.setMidiNumber(95);
		n6.setMidiNumber(105);

		Score score = new Score();
		score.addStaff();

		score.addObject(n1, 0, 1);
		score.addObject(n2, 0, 1);
		score.addObject(n3, 0, 1);
		score.addObject(n4, 0, 1);
		score.addObject(n5, 0, 1);
		score.addObject(n6, 0, 1);

		// Curved connection
		notation.CurvedConnection c = new Slur(n1, n5);
		score.addCurvedConnection(c);

		// Testo delle note
		List<String> syllables = new ArrayList<>();
		syllables.add("1");
		syllables.add("--");
		syllables.add("2");
		syllables.add("3");
		syllables.add("--");
		syllables.add("4");
		syllables.add("5");

		score.addLyrics(syllables, 0, 1, 1);

		// Stampa dei risultati
		System.out.println("Result:");
		System.out.println("Note 1: " + getLyric(n1));
		System.out.println("Note 2: " + getLyric(n2));
		System.out.println("Note 3: " + getLyric(n3));
		System.out.println("Note 4: " + getLyric(n4));
		System.out.println("Note 5: " + getLyric(n5));
		System.out.println("Note 6: " + getLyric(n6));

	}

	private String getLyric(NoteEvent n) {
		Lyric l = n.getLyric(1);
		if (l == null)
			return "";
		String s = l.getSyllable().getText();
		if (s == null)
			return "";
		return s;
	}

	public static void main(String[] args) {
		new CurvedConnectionTest().go();
	}
}