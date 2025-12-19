package Tests;

import musicEvent.Intervall;
import musicEvent.Modus;
import musicEvent.NamedNote;
import musicEvent.Note;
import musicEvent.Scale;

public class TestIntervall {

	public static void main(String[] args) {
		new TestIntervall().los();

	}

	private void los() {
		Intervall itv = new Intervall();
		int midi = 69;
		Scale s = new Scale(new Note(midi), Modus.MAJOR_SCALE);
		NamedNote.setLanguage("IT");
		NamedNote n1 = new NamedNote(midi, 0);
		for (int i = midi; i <= midi + 12; i++) {
			Note n2 = s.getNote(i);
			int[] intvs = itv.calculateIntervall(n1, n2);

			System.out.print(n1.getName() + " - " + new NamedNote(n2).getName()+": ");
			System.out.print(intvs[0] + " ");
			printType(intvs[1]);
			System.out.println("-----------------------------------------------");
		}
	}
	
		private void printType(int type) {
			switch (type) {
			case Intervall.DIMINISHED:
				System.out.println("dim");
				break;
			case Intervall.MINOR:
				System.out.println("min");
				break;
			case Intervall.PERFECT:
				System.out.println("rein");
				break;
			case Intervall.MAJOR:
				System.out.println("maj");
				break;
			case Intervall.AUGMENTED:
				System.out.println("aug");
				break;
			case Intervall.PLUS_AUGMENTED:
				System.out.println("+aug");
				break;
			case Intervall.PLUS_DIMINISHED:
				System.out.println("+dim");
				break;
				default:
					System.out.println("Undefinied");
			}
		}	
}
