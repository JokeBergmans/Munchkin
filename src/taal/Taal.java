package taal;

import java.util.Locale;
import java.util.ResourceBundle;

public class Taal {

    private static ResourceBundle rb;

    /**
     * Wordt opgeroepen in StartUp wanneer een taal gekozen wordt
     * Stelt de huidige taal in volgens de meegegeven resource bundle
     * @param resourceBundle    resource bundle die hoort bij de gewenste taal
     */
    public static void setBundle(ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }

    /**
     * Geeft de vertaling van meegegeven key in de huidige taal terug
     * @param key   key van de gewenste string die in de resource bundle staat
     * @return      string uit de juiste resource bundle
     */
    public static String geefVertaling(String key) {
        return rb.getString(key);
    }

    /**
     * Geeft de huidige taal terug
     * @return      Locale van de huidige taal
     */
    public static Locale getTaal() {
        if (rb != null) {
            return rb.getLocale();
        } else {
            return null;
        }
    }


}
