package Tests;

import musicEvent.Intervall;
import musicEvent.NamedNote;

public class TestIntervall3 {

	public static void main(String[] args) {
	new TestIntervall3().los();

	}

	private void los() {
		NamedNote n1 = new NamedNote(3,1);
		for (int i=2; i < 127; i++) {
			for (int j=-2;j<=2;j++) {
		NamedNote n2 = new NamedNote (i,j);
		if (n2.getName()==null) continue;
		Intervall itv = new Intervall();
		int[] hh = itv.calculateIntervall(n1, n2);
		System.out.print(n1.getName() + " - " + new NamedNote(n2).getName()+": ");
		System.out.print(hh[0]+" ");
		printType(hh[1]);
	}
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
				System.out.println("Unknow");
		}
	}	
}
