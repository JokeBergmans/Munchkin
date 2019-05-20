package cui;

import domein.DomeinController;
import taal.Taal;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UC8OpslaanSpel {

    private DomeinController dc;

    public UC8OpslaanSpel(DomeinController dc) {
        this.dc = dc;
    }

    /**
     * Als het spel al opgeslagen is, wordt dit meteen overschreven
     * Anders moet een spelnaam worden ingegeven
     */
    public void slaOp() {
        if (dc.isOpgeslagen()) {
            System.out.printf("%s%n", Taal.geefVertaling("startOpslaan"));
            dc.slaSpelOp();
            System.out.printf("%s%n", Taal.geefVertaling("opgeslagen"));
        } else {
            Scanner sc = new Scanner(System.in);
            String naam = "";
            System.out.printf("%s%n", Taal.geefVertaling("naamSpel"));
            System.out.print(">");
            while (naam.isEmpty()) {
                try {
                    naam = sc.nextLine();
                    dc.zetSpelnaam(naam);
                    System.out.printf("%s%n", Taal.geefVertaling("startOpslaan"));
                    dc.slaSpelOp();
                    System.out.printf("%s%n", Taal.geefVertaling("opgeslagen"));
                } catch (InputMismatchException ime) {
                    System.out.printf("%s%n", Taal.geefVertaling("foutSpelnaam"));
                    System.out.print(">");
                    sc.nextLine();
                }
            }

        }
    }
}
