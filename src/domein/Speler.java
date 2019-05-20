package domein;

import exceptions.FouteSpelerGegevensException;
import util.Validatie;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.IntStream;

public class Speler {

    private Spel spel;
    private String naam;
    private Geslacht geslacht;
    private int niveau;
    private Ras ras;
    private List<Kaart> handKaarten;
    private List<EquipmentKaart> gedragenKaarten;
    private int level;

    /**
     * Constructor voor Speler
     * Geeft alle velden de initiële of gegeven waarde,
     * vraagt aan Spel 2 kaarten van elke stapel en voegt deze toe aan de hand kaarten
     * @param spel                      spel waaraan de speler deelneemt
     * @param naam                      gegeven naam
     * @param geslacht                  gegeven geslacht
     * @throws InputMismatchException   indien naam of geslacht ongeldig is
     */
    public Speler(Spel spel, String naam, Geslacht geslacht) throws FouteSpelerGegevensException {
        this.spel = spel;
        handKaarten = new ArrayList<>();
        setNaam(naam);
        setGeslacht(geslacht);
        setNiveau(1);
        setRas(Ras.MENS);
        IntStream.range(0,2).forEach(e -> {
            handKaarten.add(spel.neemBovensteKerkerKaart());
            handKaarten.add(spel.neemBovensteSchatKaart());
        });
        gedragenKaarten = new ArrayList<>();

    }

    /**
     * Constructor voor Speler
     * Wordt gebruikt als speler uit database gehaald wordt
     * @param spel      spel waaraan speler deelneemt
     * @param naam      gegeven naam
     * @param geslacht  gegeven geslacht
     * @param niveau    gegeven niveau
     * @param ras       gegeven ras
     */
    public Speler(Spel spel, String naam, Geslacht geslacht, int niveau, Ras ras) {
        this.spel = spel;
        this.naam = naam;
        this.geslacht = geslacht;
        this.niveau = niveau;
        this.ras = ras;
    }

    public String getNaam() {
        return naam;
    }

    /**
     * Setter voor naam
     * Controleert of naam tussen de 6 en 12 geldige tekens bevat: letters, "-" en "_"
     * @param naam                      gegeven naam
     * @throws InputMismatchException   indien naam ongeldig is
     */
    private void setNaam(String naam) throws FouteSpelerGegevensException {
        if(Validatie.controleerSpelernaam(naam)) {
            this.naam = naam;
        }
        else {
            throw new FouteSpelerGegevensException("foutNaam");
        }
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    /**
     * Setter voor geslacht
     * Controleert of geslacht een geldig teken is (afhankelijk van de taal):
     * 'M' of 'H' voor mannelijk
     * 'V' of 'F' voor vrouwelijk
     * @param geslacht                  gegeven character
     * @throws InputMismatchException   indien geslacht ongeldig is
     */
    private void setGeslacht(Geslacht geslacht) {
        this.geslacht = geslacht;
    }

    public int getNiveau() {
        return niveau;
    }

    private void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Ras getRas() {
        return ras;
    }

    private void setRas(Ras ras) {
        this.ras = ras;
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public List<Kaart> getHandKaarten() {
        return handKaarten;
    }

    public List<EquipmentKaart> getGedragenKaarten() {
        return gedragenKaarten;
    }

    public void setHandKaarten(List<Kaart> handKaarten) {
        this.handKaarten = handKaarten;
    }

    public void setGedragenKaarten(List<EquipmentKaart> gedragenKaarten) {
        this.gedragenKaarten = gedragenKaarten;
    }

    public void veranderNiveau(int niveaus) {
        niveau += niveaus;
    }

    public void veranderLevel(int levels) {
        level += levels;
    }

    public void voegKaartBijHand(Kaart kaart) {
        handKaarten.add(kaart);
    }
}
