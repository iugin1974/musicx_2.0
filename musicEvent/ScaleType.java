package musicEvent;

public class ScaleType {

	private ScaleType(){}
	
	/**
	 * 
	 * @param tonic
	 * @param modus
	 * @return 1 per tonalità con diesis, -1 con bemolli o 0 senza
	 * alterazioni
	 */
	public static int getScaleAlterationType(Note tonic, Modus modus) {
		
		int m = tonic.getMidiNumber()%12;
		int a = tonic.getAlteration();
		if (modus == Modus.MAJOR_SCALE) {
			// tutte le tonalità con # e b
			if (a != 0) return a;
			// tutte le tonalità senza alterazioni
			// da qui a = 0
			if (m == 0) return 0;
			if (m == 2 || m == 4 || m == 7 ||
					m == 9 || m ==11) return 1;
			return -1;
		} // end Majorscale
		
		if (modus == Modus.MINOR_SCALE) {
			// tutte le tonalità con # e b
			if (a != 0) return a;
			// tutte le tonalità senza alterazioni
			// da qui a = 0
			if (m == 9) return 0;
			if (m == 4 || m == 11) return 1;
			return -1;
		} // end Minorscale
		return 0;
	}
}
