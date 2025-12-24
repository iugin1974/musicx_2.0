package musicEvent;

import java.io.Serializable;

import musicInterface.MusicObject;

public class Rest extends MusicEvent implements Serializable  {

	public Rest(int duration, int dots) {
		super(duration, dots);
	}

	@Override
	public Rest getCopy() {
		return new Rest(this.duration, this.dots);
	}
	
	@Override
	public String toString() {
		return " R[" + duration + "; " + dots + "]";
	}
	
}
