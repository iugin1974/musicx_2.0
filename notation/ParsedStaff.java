package notation;

import java.util.List;

import musicInterface.MusicObject;

/**
 * {@code ParsedStaff} rappresenta uno staff già elaborato e pronto
 * per il rendering o per ulteriori fasi di layout.
 * <p>
 * Contiene:
 * <ul>
 *   <li>il riferimento allo {@link Staff} di origine</li>
 *   <li>le liste di {@link MusicObject} per ciascuna voce, già combinate
 *       con gli oggetti staff-wide e ordinate temporalmente</li>
 * </ul>
 * <p>
 * Ogni elemento della lista {@code voices} rappresenta una voce distinta
 * dello staff, così come risultante dal parsing (ad esempio tramite
 * {@link VoiceMixer}).
 */
public class ParsedStaff {

    /**
     * Liste di oggetti musicali per ciascuna voce dello staff.
     * <p>
     * Ogni lista contiene sia gli oggetti staff-wide sia quelli
     * specifici della voce, combinati e ordinati.
     */
    public final List<List<MusicObject>> voices;

    /**
     * Lo staff di riferimento.
     */
    public final Staff staff;

    /**
     * Crea un nuovo {@code ParsedStaff}.
     *
     * @param staff  lo staff di riferimento
     * @param voices le voci elaborate e combinate
     */
    public ParsedStaff(Staff staff, List<List<MusicObject>> voices) {
        this.staff = staff;
        this.voices = voices;
    }

    /**
     * Restituisce lo staff associato.
     *
     * @return lo {@link Staff} di riferimento
     */
    public Staff getStaff() {
        return staff;
    }

    /**
     * Indica se la prima voce dello staff inizia con una chiave.
     * <p>
     * Questo metodo è utile per determinare se è necessario
     * disegnare una chiave all'inizio dello staff.
     *
     * @return {@code true} se il primo oggetto della prima voce è una {@link Clef},
     *         {@code false} altrimenti
     */
    public boolean startsWithClef() {
        if (voices.get(0).isEmpty())
            return false;
        return voices.get(0).get(0) instanceof Clef;
    }
}
