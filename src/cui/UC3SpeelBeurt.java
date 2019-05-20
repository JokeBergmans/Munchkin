package cui;

import domein.DomeinController;

import taal.Taal;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UC3SpeelBeurt {

    private DomeinController dc;

    public UC3SpeelBeurt(DomeinController dc) {
        this.dc = dc;
    }

    /**
     * Wordt opgeroepen als een speler kiest om zijn beurt te spelen
     * Geeft de bovenste KerkerKaart weer, en reageert gepast afhankelijk van het type
     * Vraagt daarna of de speler nog kaarten wilt beheren (beheren zelf gebeurt in UC7)
     * Beeïndigt aan het einde de beurt van de speler
     */
    public void speelBeurt() {
        printOverzicht(dc.geefSpelOverzicht());
        System.out.println(Taal.geefVertaling("bovensteKaart") + ": " + dc.geefBovensteKaart());
        Scanner sc = new Scanner(System.in);
        System.out.printf("%s%n", Taal.geefVertaling("bevestig"));
        System.out.print(">");
        while (!sc.nextLine().isEmpty()) {
            System.out.print(">");
        }
        if (dc.isMonsterKaart()) {
            //TODO: UC4 voorbereiden gevecht
        } else if (dc.heeftDirectEffect()) {
            dc.voerEffectUit();
            printResultaat(dc.geefResultaat());
        } else {
            dc.voegKaartBijHand();
        }

        System.out.printf("%s%n", Taal.geefVertaling("handKaarten"));
        System.out.println(String.join(", ", dc.geefHandKaarten()));
        int keuze = -1;
        while(keuze < 0 || dc.teveelKaarten()) {
            //TODO: UC7 beheer kaarten
            if (!dc.teveelKaarten()) {
                System.out.printf("%s%n", Taal.geefVertaling("verderBeheren"));
                System.out.printf("1 - %s%n", Taal.geefVertaling("ja"));
                System.out.printf("2 - %s%n", Taal.geefVertaling("nee"));
                try {
                    keuze = sc.nextInt();
                    if (keuze == 1) {
                        keuze = -1;
                    } else if (keuze != 2) {
                        throw new InputMismatchException();
                    }
                } catch (InputMismatchException ime) {
                    keuze = -1;
                    System.out.printf("%s%n", Taal.geefVertaling("ongeldig"));
                }
            }
        }
        printOverzicht(dc.geefSpelOverzicht());
        dc.beeindigBeurt();
    }

    /**
     * Print het resultaat van een effect op de huidige speler
     * @param details       tweedimensionale array met van de huidige speler alle gegevens in de volgorde
     *                      naam - geslacht - niveau - ras
     */
    private void printResultaat(String[] details) {
        System.out.println(Taal.geefVertaling("resultaat"));
        // tabel header
        System.out.printf("%-12s|%-12s|%-12s|%-12s|%n",
                Taal.geefVertaling("naam"),
                Taal.geefVertaling("geslacht"),
                Taal.geefVertaling("niveau"),
                Taal.geefVertaling("ras"));
        System.out.println("------------|------------|------------|------------|");

        //tabel
        System.out.printf("%-12s|%-12s|%-12s|%-12s|%n",
                details[0],
                Taal.geefVertaling(details[1].toLowerCase()),
                details[2],
                Taal.geefVertaling(details[3].toLowerCase()));
        System.out.println("------------|------------|------------|------------|");
        System.out.println();
    }
    /***
     * Print een tabel met gegevens van de spelers (inclusief zichtbare kaarten)
     * @param overzicht     tweedimensionale array met per speler alle gegevens in de volgorde
     *                      naam - niveau - geslacht - gedragen kaarten
     */
    private void printOverzicht(String[][] overzicht) {
        // tabel header
        System.out.printf("%-12s|%-12s|%-12s|%s%n",
                Taal.geefVertaling("naam"),
                Taal.geefVertaling("niveau"),
                Taal.geefVertaling("geslacht"),
                Taal.geefVertaling("gedragenKaarten"));
        System.out.println("------------|------------|------------|------------------");

        //tabel
        for (String[] details : overzicht) {
            if (details[0].equals(dc.geefHuidigeSpeler())) {
                System.out.printf("%-12s|%-12s|%-12s|%s%n",
                        "*" + details[0],
                        details[1],
                        Taal.geefVertaling(details[2].toLowerCase()),
                        details[3]);
            } else {
                System.out.printf("%-12s|%-12s|%-12s|%s%n",
                        details[0],
                        details[1],
                        Taal.geefVertaling(details[2].toLowerCase()),
                        details[3]);
            }
            System.out.println("------------|------------|------------|------------------");
        }
    }
}