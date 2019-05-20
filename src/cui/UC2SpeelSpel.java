package cui;

import domein.DomeinController;
import taal.Taal;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UC2SpeelSpel {

    private DomeinController dc;
    private MainMenu mm;
    private UC3SpeelBeurt uc3;
    private UC8OpslaanSpel uc8;

    public UC2SpeelSpel(DomeinController dc, MainMenu mm) {
        this.dc = dc;
        this.mm = mm;
        this.uc3 = new UC3SpeelBeurt(dc);
        this.uc8 = new UC8OpslaanSpel(dc);
    }

    /**
     * Wordt opgeroepen nadat er een spel aangemaakt of geladen is
     * Geeft voor de speler aan beurt een keuzemenu om te spelen, op te slaan of af te sluiten
     * Herhaalt zolang er geen winnaar is
     */
    public void speelSpel() {
        while (!dc.heeftWinnaar()) {
            int keuze = -1;
            while (keuze < 0) {
                String speler = dc.geefHuidigeSpeler();
                System.out.printf("%s: %s%n", Taal.geefVertaling("huidigeSpeler"), speler);
                System.out.printf("1 - %s%n", Taal.geefVertaling("speelBeurt"));
                System.out.printf("2 - %s%n", Taal.geefVertaling("opslaan"));
                System.out.printf("0 - %s%n", Taal.geefVertaling("stopSpel"));
                System.out.print(">");
                Scanner sc = new Scanner(System.in);
                try {
                    keuze = sc.nextInt();
                    if (keuze == 1) {
                        uc3.speelBeurt();
                    } else if (keuze == 2) {
                        uc8.slaOp();
                    } else if (keuze == 0) {
                        if (dc.isOpgeslagen()) {
                            mm.start();
                        } else {
                            opslaan();
                            mm.start();
                        }
                    } else {
                        throw new InputMismatchException();
                    }
                } catch (InputMismatchException ime) {
                    keuze = -1;
                    System.out.printf("%s%n", Taal.geefVertaling("ongeldig"));
                }
            }
        }
        System.out.printf("%s%n", MessageFormat.format(Taal.geefVertaling("gewonnen"), dc.geefWinnaar()));
    }

    /**
     * Wanneer speler wilt stoppen en het spel nog niet is opgeslagen, wordt er een boodschap getoond met de vraag
     * of het spel opgeslagen moet worden
     */
    private void opslaan() {
        Scanner sc = new Scanner(System.in);
        int keuze = -1;
        while (keuze < 0) {
            System.out.printf("%s%n", Taal.geefVertaling("waarschuwingStoppen"));
            System.out.printf("1 - %s%n", Taal.geefVertaling("ja"));
            System.out.printf("2 - %s%n", Taal.geefVertaling("nee"));
            System.out.print(">");
            try {
                keuze = sc.nextInt();
                if (keuze == 1) {
                    uc8.slaOp();
                } else if (keuze == 2) {
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException ime) {
                keuze = -1;
                System.out.printf("%s%n", Taal.geefVertaling("ongeldig"));
            }
        }
    }
}
