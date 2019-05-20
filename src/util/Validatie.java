package util;

public class Validatie {

    /**
     * Controleert spelernaam volgens DR: naam mag alleen letters, "-" en "_" bevatten
     * en moet tussen de 6 en 12 tekens lang zijn
     * @param naam      gegeven spelernaam
     * @return          true als naam aan DR voldoet, anders false
     */
    public static boolean controleerSpelernaam(String naam) {
        return naam.matches("[A-Za-z\\-_]{6,12}");
    }

    /**
     * Controleert spelnaam volgens DR: naam mag alleen letters en minstens 3 cijfers bevatten
     * en moet tussen de 6 en 12 tekens lang zijn
     * @param naam      gegeven spelnaam
     * @return          true als naam aan DR voldoet, anders false
     */
    public static boolean controleerSpelnaam(String naam) {
        return naam.matches("[A-Za-z0-9]{6,12}") && naam.matches(".*[0-9]{3,}.*");
    }
}
