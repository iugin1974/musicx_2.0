package musicEvent;

public class Partial extends MusicEvent {


	public String getName() {
		int dur = getDuration();
		int dots = getDots();
		dur = (int) Math.pow(2, dur);
		String dotsAsString = "";
		for (int i=0; i<dots; i++) {
			dotsAsString = dotsAsString+".";
		}
		return dur+dotsAsString;
	}

	@Override
	public Partial getCopy() {
		return null;
	}

}
