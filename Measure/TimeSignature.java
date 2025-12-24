package Measure;

import java.io.Serializable;

import musicInterface.MusicObject;

public class TimeSignature extends MusicObject implements Serializable {

	private int numerator = 4, denominator = 4;

	public TimeSignature(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public void setTimeSignature(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	};

	public float getRealTime() {
		return (float) numerator / denominator;
	}
	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}
	
	@Override
	public String toString() {
		return numerator + "/" + denominator;
	}
	
}
