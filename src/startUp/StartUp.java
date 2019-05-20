package startUp;

import cui.MainMenu;
import taal.Taal;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class StartUp {

    private static MainMenu mm;

    public static void main(String[] args) {
        mm = new MainMenu();
        toonTaalMenu();

    }

    /**
     * Toont het menu om een taal (Nederlands, Engels of Frans) te kiezen
     */
    private static void toonTaalMenu() {
        Scanner sc = new Scanner(System.in);
        ResourceBundle taal = null;
        int keuze = -1;
        while (keuze < 0) {
            System.out.printf("1 - Nederlands %n");
            System.out.printf("2 - English %n");
            System.out.printf("3 - Français %n");
            System.out.print(">");
            try {
                keuze = sc.nextInt();
                if (keuze == 1) {
                    taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("nl", "NL"));
                } else if (keuze == 2) {
                    taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("en", "GB"));
                } else if (keuze == 3) {
                    taal = ResourceBundle.getBundle("resources.ResourceBundle", new Locale("fr", "FR"));
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException ime) {
                keuze = -1;
                System.out.printf("%nKies 1, 2 of 3%nChoose 1, 2 or 3%nChoisissez 1, 2 ou 3%n");
                sc.nextLine();
            }
        }
        Taal.setBundle(taal);
        toonUIMenu();
    }

    /**
     * Toont het menu om een User Interface (Command-Line of Graphical) te kiezen
     */
    private static void toonUIMenu() {
        Scanner sc = new Scanner(System.in);
        int keuze = -1;
        while (keuze < 0) {
            System.out.println("1 - CLI");
            System.out.println("2 - GUI");
            System.out.printf("0 - %s%n", Taal.geefVertaling("stop"));
            System.out.print(">");
            try {
                keuze = sc.nextInt();
                if (keuze == 1) {
                    mm.start();
                } else if (keuze == 2) {
                    StartUpGui.main();
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
