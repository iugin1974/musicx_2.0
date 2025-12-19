package Tests;

import musicEvent.Intervall;
import musicEvent.NamedNote;

public class TestIntervall2 {

	public static void main(String[] args) {
		new TestIntervall2().los();

	}

	private void los() {
		Intervall itv = new Intervall();
		NamedNote.setLanguage("IT");
		NamedNote n1 = new NamedNote(65, 0);
		NamedNote n2 = new NamedNote(71, 0);
		
		
			int[] intvs = itv.calculateIntervall(n1, n2);

			System.out.print(n1.getName() + " - " + new NamedNote(n2).getName()+": ");
			System.out.print(intvs[0]);
			printType(intvs[1]);
	
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
			}
		}	
}
