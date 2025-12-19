package Tests;

import musicEvent.Modus;
import musicEvent.NamedNote;
import musicEvent.Note;
import musicEvent.Scale;

public class TestScale {

	private void los() {
		
		System.out.println("Major scales");
		for (int j = 0; j < 12; j++) {
			int midi = j;
			switch (j) {
			case 1:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				break;
			default:
				break;
			}
			NamedNote.setLanguage("IT");
			Scale s = new Scale(new Note(midi), Modus.MAJOR_SCALE);
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(i + "\t");
			}
			System.out.println();
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(new NamedNote(s.getNote(i)).getName() + "\t");
			}
			System.out.println();
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(new NamedNote(s.getNote(i)).getInfo());
			}
			System.out
					.println("\n--------------------------------------------------------------------------------------------");
		}

		
		
		////////////////////////
		System.out.println("\n\n\nMinor scales");
		for (int j = 0; j < 12; j++) {
			int midi = j;
			switch (j) {
			case 1:
			case 4:
			case 6:
			case 11:
				break;

			default:
				break;
			}
			Scale s = new Scale(new Note(midi), Modus.MINOR_SCALE);
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(i + "\t");
			}
			System.out.println();
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(new NamedNote(s.getNote(i)).getName() + "\t");
			}
			System.out.println();
			for (int i = midi; i < midi + 12; i++) {
				System.out.print(new NamedNote(s.getNote(i)).getInfo());
			}
			System.out
					.println("\n--------------------------------------------------------------------------------------------");
		}
	}

	public static void main(String[] args) {
		new TestScale().los();
	}
}
