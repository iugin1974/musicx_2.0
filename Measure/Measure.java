package Measure;

import java.io.Serializable;
import java.util.ArrayList;

import musicEvent.MusicEvent;

public class Measure implements Serializable {

	private int numerator = -1;
	private int denominator = -1;
	private float measureArt = -1f;
	private float posInMeasure;
	private int[] n = { 1, 2, 4, 8, 16, 32, 64, 128 };
	private float partial;
	/**
	 * Un arrayList contenente i MusicEvent nella battuta;
	 */
	private ArrayList<MusicEvent> noteInMeasure = new ArrayList<MusicEvent>();
	private int currentMeasure = 1;

	/**
	 * @return true se la battua contiene note, altrimenti false.
	 */
	public boolean hasNote() {
		if (noteInMeasure.size()>0)
			return true;
		return false;
	}

	public ArrayList<MusicEvent> getEvents() {
		return noteInMeasure;
	}

	/**
	 * Controlla che la nota abbia posto nella battuta
	 * 
	 * @param note
	 * @return
	 */
	public boolean hasPlace(MusicEvent note) {
		float dur = note.getRealDuration();
		if (posInMeasure + dur > measureArt)
			return false;
		return true;
	}

	protected Measure(int currentMeasure) {
		this(4, 4, currentMeasure);
	}

	protected Measure(int numerator, int denominator, int currentMeasure) {
		setNumerator(numerator);
		setDenominator(denominator);
		this.currentMeasure = currentMeasure;
		if (currentMeasure == 1) {
			posInMeasure = partial;
		} else {
			posInMeasure = 0f;
		}
	}

	protected Measure(TimeSignature time, int currentMeasure) {
		this(time.getNumerator(), time.getDenominator(), currentMeasure);
	}

	protected Measure(int numerator, int denominator) {
		this(numerator, denominator, 1);
	}

	protected Measure(TimeSignature time) {
		this(time.getNumerator(), time.getDenominator(), 1);
	}

	/**
	 * 
	 * @param note
	 * @return true se la nota è stata aggiunta, altrimenti false (nota troppo
	 *         lunga per lo spazio a disposizione)
	 */
	protected boolean addMusicEvent(MusicEvent note) {
		if (!hasPlace(note))
			return false;
		noteInMeasure.add(note);
		posInMeasure = posInMeasure + note.getRealDuration();
		return true;
	}

	/**
	 * Cambia l'ultimo evento inserito
	 * 
	 * @param me
	 * @return true se il cambiamento ha avuto successo
	 */
	protected boolean changeEvent(MusicEvent me) {
		/*
		 * Tiene una copia in memoria dell'elemento eliminato. Se il nuovo non
		 * avesse posto, l'elemento tolto riverrebbe inserito.
		 */
		MusicEvent m = removeLastEvent();
		if (m == null) {
			System.out.println("Non posso rimuovere l'evento");
			return false;
		}
		boolean hasPlace = hasPlace(me);
		if (hasPlace) {
			addMusicEvent(me);
			return true;
		} else {
			addMusicEvent(m); // ripristina il vecchio evento
			return false;
		}
	}

	/**
	 * Rimuove l'ultimo evento
	 * 
	 * @return l'evento rimosso se l'operazione è stata possibile altrimenti
	 *         null;
	 */
	protected MusicEvent removeLastEvent() {
		if (hasNote()) {
			MusicEvent toRemove = noteInMeasure.get(noteInMeasure.size() - 1);
			noteInMeasure.remove(noteInMeasure.size() - 1);

			posInMeasure = posInMeasure - toRemove.getRealDuration();
			return toRemove;
		}
		return null;
	}

	/**
	 * Ritorna la posizione nella battuta.
	 * 
	 * @return la posizione nella battuta (float) se essa è valida, altrimenti
	 *         -1f.
	 */
	public float getPosInMeasure() {
		return posInMeasure;
	}

	/**
	 * @return il numero della battuta attuale
	 */
	public int getMeasureNumber() {
		return currentMeasure;
	}

	// public void setAktuellerMeasure(int MeasureNumber) {
	// Measure.currentMeasure = MeasureNumber;
	// this.MeasureNumber = MeasureNumber;
	// }

	// ---------------------------
	/**
	 * @return il valore del numeratore della battuta.
	 */
	public int getNumerator() {
		return numerator;
	}

	/**
	 * Setta il valore del numeratore della battuta.
	 * 
	 * @param numerator
	 */
	private void setNumerator(int numerator) {
		if (numerator > 0) {
			this.numerator = numerator;
			setMeasureArt();
		}
	}

	// ---------------------
	/**
	 * @return il valore del denominatore della battuta.
	 */
	public int getDenominator() {
		return denominator;
	}

	/**
	 * Setta il denominatore della battuta
	 * 
	 * @param denominator
	 * @return 0 se il denomitare ha un valore valido, altrimenti -1.
	 */
	private int setDenominator(int denominator) {
		for (int i = 0; i < n.length; i++) {
			if (denominator == n[i]) {
				this.denominator = denominator;
				setMeasureArt();
				return 0;
			}
		}
		return -1;
	}

	// -------------------------
	/**
	 * Ritorna il valore della battuta in float. Il valore è dato dal numerator
	 * / denominator. Es: 4/4 ritorna 1.0f
	 * 
	 * @return il valore della battuta
	 * 
	 */
	public float getMeasureArt() {
		return measureArt;
	}

	/**
	 * Setta il valore della battuta.
	 */
	private void setMeasureArt() {
		if (numerator != -1 && denominator != -1) {
			this.measureArt = (float) numerator / denominator;
		}
	}

	//
	// public void setAufMeasure(MusicEvent musicEvent) {
	// addMusicEvent(musicEvent);
	// }

	/**
	 * Crea un levare nella battuta. Se si inserisce come levare 4. nella
	 * battuta viene inserito un valore invisibile di 2. (ammesso di essere in
	 * 4/4).
	 * 
	 * @param duration
	 * @param dots
	 * @return
	 */
	protected boolean setPartial(int duration, int dots) {
		if (currentMeasure != 1)
			return false;
		float dur = 0;
		dur = (float) (1 / Math.pow(2, duration));
		// i punti
		float dotDuration = 0;
		if (dots != 0) {
			dotDuration = dur / 2;
			for (int i = 1; i < dots; i++) {
				dotDuration = dotDuration + (dotDuration / 2);
			}
		}
		if (getMeasureArt() - (dur + dotDuration) < 0)
			return false;
		partial = getMeasureArt() - (dur + dotDuration);
		posInMeasure = partial;
		return true;
	}

	public boolean isEmpty() {
		if (posInMeasure == 0) {
			return true;
		}
		return false;
	}

	public boolean isFull() {
		if (posInMeasure == measureArt) {
			return true;
		}
		return false;
	}

	public void setCurrentMeasure(int currentMeasure) {
		this.currentMeasure = currentMeasure;
	}

	@Override
	public String toString() {
		return "M[ current="+currentMeasure+", position="+posInMeasure+", partial="+partial+"]";
	}
}
