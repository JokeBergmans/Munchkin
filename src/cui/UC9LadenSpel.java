package cui;

import domein.DomeinController;
import domein.Spel;
import taal.Taal;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UC9LadenSpel {

    private DomeinController dc;
    private MainMenu mm;
    private UC2SpeelSpel uc2;

    public UC9LadenSpel(DomeinController dc, MainMenu mm, UC2SpeelSpel uc2) {
        this.dc = dc;
        this.mm = mm;
        this.uc2 = uc2;
    }

    /**
     * Geeft lijst terug van opgeslagen spellen, of gepaste boodschap indien er geen zijn
     * Start gewenste spel als er geldige keuze werd gemaakt
     */
    public void laadSpel() {
        String[][] spellen = dc.geefSpellen();
        if (spellen.length == 0) {
            System.out.printf("%s%n", Taal.geefVertaling("geenSpellen"));
            mm.start();
        }
        System.out.printf("%s%n", Taal.geefVertaling("kiesSpel"));
        int i = 1;
        for (String[] s : spellen) {
            System.out.printf("%d - %-12s %s%n", i, s[0], s[1] + " " + Taal.geefVertaling("speler").toLowerCase() + "s");
            i++;
        }
        System.out.printf("0 - %s%n", Taal.geefVertaling("stop"));
        System.out.print(">");
        Scanner sc = new Scanner(System.in);
        int keuze = -1;
        while (keuze < 0) {
            try {
                keuze = sc.nextInt();
                if (keuze == 0) {
                    System.exit(0);
                } else if (keuze < 0 || keuze > spellen.length) {
                    throw new InputMismatchException();
                } else {
                    System.out.printf("%s%n", Taal.geefVertaling("startLaden"));
                    dc.laadSpel(spellen[(keuze - 1)][0]);
                    uc2.speelSpel();
                }
            } catch (InputMismatchException ime) {
                System.out.printf("%s%n", Taal.geefVertaling("ongeldig"));
            }
        }

    }
}
