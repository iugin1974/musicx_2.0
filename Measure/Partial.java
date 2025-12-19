package Measure;

import java.io.Serializable;

public class Partial implements Serializable {

	private int duration;
	private int dots;

	/**
	 * Il levare di una battuta
	 * 
	 * @param duration
	 *            La durata dell'evento calcolata nel seguente modo:<br>
	 *            0 = 4/4<br>
	 *            1 = 2/4<br>
	 *            2 = 1/4<br>
	 *            ecc.
	 * @param dots
	 *            il numero dei punti
	 */
	public Partial(int duration, int dots) {
		this.duration = duration;
		this.dots = dots;
	}

	public int getDuration() {
		return duration;
	}

	public int getDots() {
		return dots;
	}
	
	public float getRealDuration() {
		float dur = 0;
		dur = (float) ((float) 1 / Math.pow(2, duration));
		// i punti
		float dotDuration = 0;
		if (getDots() != 0) {
			dotDuration = (float) dur / 2;
			for (int i = 1; i < dots; i++) {
				dotDuration = dotDuration + (dotDuration / 2);
			}
		}
		return dur + dotDuration;
	}
}
