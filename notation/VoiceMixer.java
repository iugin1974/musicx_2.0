package notation;

import java.util.ArrayList;
import java.util.List;

import musicInterface.MusicObject;

/**
 * La classe {@code VoiceMixer} si occupa di combinare gli oggetti musicali
 * di uno {@link Staff} in viste separate per ciascuna voce.
 * <p>
 * Per ogni voce "musicale" (escludendo la voce {@code STAFF_WIDE}),
 * vengono restituiti:
 * <ul>
 *   <li>gli oggetti staff-wide (chiavi, armature, metriche, ecc.)</li>
 *   <li>gli oggetti appartenenti alla singola voce</li>
 * </ul>
 * combinati in un'unica lista ordinata in base alla posizione orizzontale
 * (X o tick).
 * <p>
 * Il risultato è pensato per il rendering grafico: ogni lista rappresenta
 * ciò che deve essere disegnato per una specifica voce sullo stesso staff.
 */
public class VoiceMixer {

    /**
     * Riferimento alla partitura che contiene staff, voci e oggetti musicali.
     */
    private final Score score;

    /**
     * Crea un nuovo {@code VoiceMixer} associato a una {@link Score}.
     *
     * @param score la partitura di riferimento
     */
    public VoiceMixer(Score score) {
        this.score = score;
    }

    /**
     * Combina gli oggetti musicali di uno staff identificato dal suo indice.
     *
     * @param staffNumber indice dello staff nella score
     * @return una lista di liste di {@link MusicObject}, una per ciascuna voce
     *         (escludendo la voce staff-wide)
     */
    public List<List<MusicObject>> mixStaff(int staffNumber) {
        Staff staff = score.getStaff(staffNumber);
        return mixStaff(staff);
    }

    /**
     * Combina gli oggetti musicali di uno {@link Staff} per ciascuna voce.
     * <p>
     * Per ogni voce:
     * <ul>
     *   <li>vengono inclusi gli oggetti staff-wide</li>
     *   <li>vengono inclusi gli oggetti specifici della voce</li>
     *   <li>la lista risultante viene ordinata per posizione orizzontale</li>
     * </ul>
     *
     * @param staff lo staff da combinare
     * @return una lista di liste di {@link MusicObject}, una lista per ogni voce
     */
    public List<List<MusicObject>> mixStaff(Staff staff) {

        // Lista risultato: una lista per ogni voce (tranne STAFF_WIDE)
        List<List<MusicObject>> result = new ArrayList<>();

        // Oggetti comuni allo staff (chiave, armatura, metrica, ecc.)
        List<MusicObject> wide = score.getStaffWideObjects(staff);

        // Per ogni voce dello staff
        for (Voice vl : staff.getVoices()) {

            // Salta la voce STAFF_WIDE
            if (vl.getVoiceType() == 0)
                continue;

            // Lista combinata: staff-wide + oggetti della voce
            List<MusicObject> combined = new ArrayList<>();
            combined.addAll(wide);
            combined.addAll(score.getObjects(staff, vl.getVoiceType()));

            // Ordina per posizione orizzontale
            combined.sort(new CompareTick());

            result.add(combined);
        }

        return result;
    }
}
