package cui;

import domein.DomeinController;
import exceptions.FouteSpelerGegevensException;
import taal.Taal;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UC1StartSpel {

    private DomeinController dc;

    public UC1StartSpel(DomeinController dc) {
        this.dc = dc;
    }

    /**
     * Start een nieuw spel op en vraagt de nodige gegevens:
     * aantal spelers en naam en geslacht voor elke speler, en print daarna een overzicht
     */
    public void nieuwSpel() {
        Scanner sc = new Scanner(System.in);
        int aantal = 0;
        while (aantal == 0) {
            System.out.printf("%s%n", Taal.geefVertaling("aantalSpelers"));
            System.out.print(">");
            try {
                aantal = sc.nextInt();
                dc.maakSpel(aantal);
            } catch (InputMismatchException ime) {
                System.out.printf("%s%n", Taal.geefVertaling("foutAantal"));
                aantal = 0;
                sc.nextLine();
            }
        }
        for (int i = 0; i < aantal; i++) {
            String naam = "";
            char geslacht = ' ';
            System.out.printf("%s%n", MessageFormat.format(Taal.geefVertaling("naamSpeler"), i + 1));
            System.out.print(">");
            while (naam.isEmpty()) {
                    naam = sc.nextLine();
            }
            System.out.printf("%s%n", MessageFormat.format(Taal.geefVertaling("geslachtSpeler"), i + 1));
            System.out.print(">");
            while (geslacht == ' ') {
                try {
                    geslacht = sc.nextLine().charAt(0);
                } catch (IndexOutOfBoundsException ioobe) {
                    System.out.printf("%s%n", Taal.geefVertaling("foutGeslacht"));
                    System.out.print(">");
                }
            }
            try {
                dc.maakSpeler(naam, geslacht);
            } catch (FouteSpelerGegevensException fsge) {
                System.out.printf("%s:%n", Taal.geefVertaling("ongeldig"));
                System.out.printf("%s%n", Taal.geefVertaling(fsge.getMessage()));
                i--;
            }
        }
        printOverzicht(dc.geefOverzichtSpelers());
    }

    /***
     * Print een tabel met alle gegevens van de spelers
     * @param overzicht     tweedimensionale array met per speler alle gegevens in de volgorde
     *                      naam - geslacht - niveau - ras
     */
    private void printOverzicht(String[][] overzicht) {
        int speler = 1;
        // tabel header
        System.out.printf("%-12s|%-12s|%-12s|%-12s|%-12s|%n",
                "",
                Taal.geefVertaling("naam"),
                Taal.geefVertaling("geslacht"),
                Taal.geefVertaling("niveau"),
                Taal.geefVertaling("ras"));
        System.out.println("------------|------------|------------|------------|------------|"); //60 dashes

        //tabel
        for (String[] details : overzicht) {
            System.out.printf("%-12s|%-12s|%-12s|%-12s|%-12s|%n",
                    Taal.geefVertaling("speler") + " " + speler,
                    details[0],
                    Taal.geefVertaling(details[1].toLowerCase()),
                    details[2],
                    Taal.geefVertaling(details[3].toLowerCase()));
            System.out.println("------------|------------|------------|------------|------------|"); //60 dashes
            speler++;
        }
    }
}

