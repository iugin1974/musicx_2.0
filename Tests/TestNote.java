package Tests;

import musicEvent.NamedNote;

public class TestNote {

	private void los() {

		NamedNote.setLanguage("IT");
		for (int i= 60; i<=72; i++) {
			for (int a=-2; a<=2; a++) {
				NamedNote nn = new NamedNote(i, a);
				//nn.setDots(1);
				nn.setDuration(1);
				System.out.print(nn+"\t");
				System.out.print("Duration: "+nn.getDuration()+"\t");
				System.out.print("RealDuration: "+nn.getRealDuration()+"\t");
				System.out.println("Dots: "+nn.getDots());
			}
		}
	}
	
	public static void main(String[] args) {
new TestNote().los();

	}

}
