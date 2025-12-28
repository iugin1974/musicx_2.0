package musicEvent;

import musicInterface.MusicObject;

/**
 * Una classe astratta che rappresenta un evento musicale, sia esso una nota,
 * una pausa o un accordo.
 */
public abstract class MusicEvent extends MusicObject {
	protected int duration;
	protected int dots;
	
	public abstract MusicEvent getCopy();
	
	protected MusicEvent(int duration, int dots) {
		this.duration = duration;
		this.dots = dots;
	}

	protected MusicEvent() {		
	}
	

	/**
	 * @param duration La durata dell'evento calcolata nel seguente modo:<br>
	 * 0 = 4/4<br>
	 * 1 = 2/4<br>
	 * 2 = 1/4<br>
	 * ecc.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return la durata reale dell'evento in <i>float</i>
	 * comprensiva dei punti.
	 */	
	public float getRealDuration() {
		float dur = 0;
		// if (duration == 0)
		// dur = 1;
		// else
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

	/**
	 * @return la durata dell'evento di base (quindi esclusi
	 * i punti di valore)
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Aggiunge il o i punti di valore all'evento.
	 * @param dots
	 */
	public void setDots(int dots) {
		if (dots < 0)
			dots = 0;
		this.dots = dots;
	}

	/**
	 * Aggiunge un punto di valore all'evento
	 */
	public void addDot() {
		dots++;
		
	}
	
	/**
	 * Rimuove un punto di valore dall'evento
	 */
	public void removeDot() {
		if (dots>0) dots--;
	}

	/**
	 * @return il numero dei punti di valore.
	 */
	public int	getDots() {
		return dots;
	}

}
