package domein;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import exceptions.FouteSpelerGegevensException;
import util.Validatie;

public class Spel {

    private int aantalSpelers;
    private List<Speler> spelers;
    private KerkerKaartStapel kerkerKaartStapel;
    private SchatKaartStapel schatKaartStapel;
    private int huidigeSpeler;
    private Speler winnaar;
    private boolean opgeslagen;
    private String naam;
    private List<Kaart> gespeeldeKaarten;
    private KerkerKaart bovensteKaart;

    /**
     * Constructor voor Spel
     * Geeft alle velden de initiële of gegeven waarde
     * Maakt kaartStapels aan met gegeven KaartRepository
     * @param aantalSpelers             gegeven aantal spelers
     * @param kaartRepository           gegeven KaartRepository
     * @throws InputMismatchException   als aantalSpelers ongeldig is
     */
    public Spel(int aantalSpelers, KaartRepository kaartRepository) throws InputMismatchException {
        setAantalSpelers(aantalSpelers);
        spelers = new ArrayList<>();
        kerkerKaartStapel = new KerkerKaartStapel(kaartRepository);
        schatKaartStapel = new SchatKaartStapel(kaartRepository);
        kerkerKaartStapel.startNieuweStapel();
        schatKaartStapel.startNieuweStapel();
        winnaar = null;
        opgeslagen = false;
        gespeeldeKaarten = new ArrayList<>();
    }

    /**
     * Constructor voor Spel
     * Wordt gebruikt als spel uit database gehaald wordt
     * @param aantalSpelers     gegeven aantal spelers
     * @param naam              gegeven naam
     */
    public Spel(int aantalSpelers, String naam) {
        setAantalSpelers(aantalSpelers);
        setNaam(naam);
        winnaar = null;
        opgeslagen = false;
    }

    /**
     * Setter voor aantalSpelers
     * Controleert of het aantal tussen 3 en 6 ligt
     * @param aantalSpelers             meegegeven aantal
     * @throws InputMismatchException   indien aantal ongeldig is
     */
    private void setAantalSpelers(int aantalSpelers) throws InputMismatchException {
        if(aantalSpelers > 2 && aantalSpelers < 7) {
            this.aantalSpelers = aantalSpelers;
        }
        else {
            throw new InputMismatchException();
        }
    }

    /**
     * Wordt opgeroepen in DomeinController wanneer een nieuwe speler moet worden aangemaakt
     * Controleert of er al een speler met gegeven naam bestaat
     * Maakt nieuwe speler aan en voegt deze toe aan de array met spelers
     * Als elke speler is aangemaakt, wordt bepaalVolgorde() opgeroepen
     * @param naam                      gegeven naam van de speler
     * @param geslacht                  gegeven geslacht van de speler
     * @throws InputMismatchException   indien ongeldige naam of geslacht werd meegegeven
     */
    public void maakSpeler(String naam, Geslacht geslacht) throws FouteSpelerGegevensException {
        for (Speler s : spelers) {
            if (s.getNaam().toLowerCase().equals(naam.toLowerCase())) {
                throw new FouteSpelerGegevensException("spelerBestaatAl");
            }
        }
        Speler speler = new Speler(this, naam, geslacht);
        spelers.add(speler);
        if (spelers.size() == aantalSpelers) {
            bepaalVolgorde();
        }
    }

    public List<Speler> getSpelers() {
        return spelers;
    }

    public Kaart neemBovensteKerkerKaart() {
        return kerkerKaartStapel.neemBovensteKaart();
    }

    public Kaart neemBovensteSchatKaart() {
        return schatKaartStapel.neemBovensteKaart();
    }

    /**
     * Controleert of de huidige speler niveau 10 behaald heeft en past eventueel het veld winnaar aan
     * Past het level van de speler aan en draait de bovenste kaart om voor de volgende beurt
     */
    public void beeindigBeurt() {
        Speler speler = spelers.get(huidigeSpeler);
        if (speler.getNiveau() >= 10) {
            winnaar = speler;
        }
        speler.veranderLevel(-speler.getLevel());
        bepaalVolgendeSpeler();
        setBovensteKaart(neemBovensteKerkerKaart());
    }

    private void setBovensteKaart(Kaart kaart) {
        bovensteKaart = (KerkerKaart) kaart;
    }

    /**
     * Bepaalt de spelervolgorde dmv een domeinregel:
     *      De eerste speler is diegene met de kortste naam,
     *      indien 2 spelers de kortste hebben wordt de eerste bepaald volgens omgekeerde alfabetische volgorde
     *      De overige spelers staan in de volgorde waarin ze ingegeven werden
     * Past het veld spelers aan zodat ze in de juiste volgorde staan
     * Zet huidigeSpeler op 0
     */
    private void bepaalVolgorde() {
        Speler eersteSpeler = spelers.get(0);
        String kortsteNaam = eersteSpeler.getNaam();
        for (int i = 1; i < spelers.size(); i++) {
            Speler speler = spelers.get(i);
            if (speler.getNaam().length() < kortsteNaam.length()) {
                eersteSpeler = speler;
                kortsteNaam = speler.getNaam();
            } else if (speler.getNaam().length() == kortsteNaam.length()) {
                if (speler.getNaam().compareTo(kortsteNaam) > 0) {
                    eersteSpeler = speler;
                    kortsteNaam = speler.getNaam();
                }
            }
        }

        List<Speler> volgorde = new ArrayList<>();
        volgorde.add(eersteSpeler);
        for (Speler s : spelers) {
            if (s != eersteSpeler) {
                volgorde.add(s);
            }
        }
        spelers = volgorde;
        huidigeSpeler = 0;
    }

    /**
     * Past het veld huidigeSpeler aan zodat deze index wijst naar de juiste speler in spelers
     */
    private void bepaalVolgendeSpeler() {
        if (huidigeSpeler == spelers.size() - 1) {
            huidigeSpeler = 0;
        } else {
            huidigeSpeler++;
        }
    }

    public Speler geefHuidigeSpeler() {
        return spelers.get(huidigeSpeler);
    }

    public Speler getWinnaar() {
        return winnaar;
    }
    

    public boolean isOpgeslagen() {
        return opgeslagen;
    }

    public void zetSpelnaam(String naam) throws InputMismatchException {
        setNaam(naam);
    }

    /**
     * Controleert of naam voldoet aan de DR: naam bestaat uit letters en minstens 3 cijfers
     * en is tussen de 6 en 12 tekens lang
     * @param naam                      gegeven naam voor het spel
     * @throws InputMismatchException   indien naam ongeldig is
     */
    private void setNaam(String naam) throws InputMismatchException{
        if(Validatie.controleerSpelnaam(naam)) {
            this.naam = naam;
        }
        else {
            throw new InputMismatchException();
        }
    }

    public String getNaam() {
        return naam;
    }

    public void setOpgeslagen(boolean opgeslagen) {
        this.opgeslagen = opgeslagen;
    }

    public List<Kaart> getGespeeldeKaarten() {
        return gespeeldeKaarten;
    }

    public int getAantalSpelers() {
        return aantalSpelers;
    }

    public void setKerkerKaartStapel(KerkerKaartStapel kerkerKaartStapel) {
        this.kerkerKaartStapel = kerkerKaartStapel;
    }

    public void setSchatKaartStapel(SchatKaartStapel schatKaartStapel) {
        this.schatKaartStapel = schatKaartStapel;
    }

    public void setGespeeldeKaarten(List<Kaart> gespeeldeKaarten) {
        this.gespeeldeKaarten = gespeeldeKaarten;
    }

    public void setSpelers(List<Speler> spelers) {
        this.spelers = spelers;
        bepaalVolgorde();
    }

    public void setHuidigeSpeler(String naam) {
        for (int i = 0; i < spelers.size(); i++) {
            if (spelers.get(i).getNaam().equals(naam)) {
                huidigeSpeler = i;
            }
        }
    }

    public KerkerKaart geefBovensteKaart() {
        if (bovensteKaart == null) {
            setBovensteKaart(neemBovensteKerkerKaart());
        }
        return bovensteKaart;
    }

    /**
     * Voert het effect van de bovenste kaart uit op de huidige speler
     * Voegt daarna deze kaart toe aan de stapel met reeds gespeelde kaarten
     */
    public void voerEffectUit() {
        Effect effect = bovensteKaart.getEffect();
        effect.voerUit(spelers.get(huidigeSpeler));
        gespeeldeKaarten.add(bovensteKaart);
    }

    public void voegKaartBijHand() {
        spelers.get(huidigeSpeler).voegKaartBijHand(bovensteKaart);
    }
}
