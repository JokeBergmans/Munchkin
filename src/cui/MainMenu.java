package cui;

import domein.DomeinController;
import taal.Taal;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    private DomeinController dc;
    private UC1StartSpel uc1;
    private UC2SpeelSpel uc2;
    private UC9LadenSpel uc9;

    public MainMenu() {
        dc = new DomeinController();
        uc1 = new UC1StartSpel(dc);
        uc2 = new UC2SpeelSpel(dc, this);
        uc9 = new UC9LadenSpel(dc, this, uc2);
    }

    /**
     * Wordt opgeroepen door StartUp als de CUI gekozen wordt
     */
    public void start() {
        toonMenu();
    }

    /**
     * Toont het menu waarin men kan kiezen om een nieuw spel te starten of een spel te laden
     */
    private void toonMenu() {
        Scanner sc = new Scanner(System.in);
        int keuze = -1;
        while (keuze < 0) {
            System.out.printf("1 - %s%n", Taal.geefVertaling("nieuw"));
            System.out.printf("2 - %s%n", Taal.geefVertaling("laad"));
            System.out.printf("0 - %s%n", Taal.geefVertaling("stop"));
            System.out.print(">");
            try {
                keuze = sc.nextInt();
                if (keuze == 1) {
                    uc1.nieuwSpel();
                    uc2.speelSpel();
                } else if (keuze == 2) {
                    uc9.laadSpel();
                } else if (keuze == 0) {
                    System.exit(0);
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
