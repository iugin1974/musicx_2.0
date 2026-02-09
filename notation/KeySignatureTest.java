package notation;

import java.util.ArrayList;
import java.util.List;

import Measure.Bar;
import musicEvent.Modus;
import musicEvent.Note;
import musicEvent.NoteEvent;

public class KeySignatureTest {
		static Score score = new Score();

	public static void main(String[] args) {
		AccidentalEngine ac = new AccidentalEngine(score);
		score.addListener(ac);
		score.addStaff();
		KeySignature k = new KeySignature(1, 1, Modus.MAJOR_SCALE);
		Clef c = Clef.treble();
		c.setTick(0);
		score.addObject(c, 0, 0);
		score.addObject(k, 0, 1);
		
		msg("INSERIMENTO PRIMA NOTA 60,0 -> sale a 61,1");
		Note n1 = new Note(60,null);
		n1.setStaffPosition(1);
		n1.setTick(2);
		score.addObject(n1, 0, 1);
		printScore(); // atteso 61,1
		
		msg("INSERIMENTO SECONDA NOTA 60,0 -> abbasso di 1/2 tono -> scende a 60,0");
		Note n2 = new Note(60,null);
		n2.setStaffPosition(1);
		n2.setTick(2);
		score.addObject(n2, 0, 1);
		n2.addFlat();
		printScore(); // atteso 60,0
		
		msg("INSERIMENTO SECONDA NOTA 60,0 -> atteso 60,0 -> come nota precedente");
		Note n3 = new Note(60,null);
		n3.setStaffPosition(1);
		n3.setTick(2);
		score.addObject(n3, 0, 1);
		printScore(); // atteso 60,0
		
	}
	
	private static void msg(String s) {
		System.out.println("=== "+s+" ===");
	}
	
	private static void printScore() {
		for (NoteEvent n : score.getStaff(0).getVoice(1).getNotes())
			System.out.print(((Note)n)+"\t");
		System.out.println();
	}
}
