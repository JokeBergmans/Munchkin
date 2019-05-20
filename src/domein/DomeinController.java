package domein;

import exceptions.FouteSpelerGegevensException;
import persistentie.PersistentieController;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class DomeinController {

    private Spel spel;
    private PersistentieController pc;
    private KaartRepository kaartRepository;
    private SpelRepository spelRepository;
    private SpelerRepository spelerRepository;

    public DomeinController() {
        pc = new PersistentieController();
        kaartRepository = new KaartRepository(pc);
        spelRepository = new SpelRepository(pc);
        spelerRepository = new SpelerRepository(pc);
    }

    /**
     * Wordt opgeroepen in UC1StartSpel wanneer een aantal spelers is ingegeven
     * Maakt een nieuw spel aan indien het aantal geldig was en stelt dit in als
     * veld
     *
     * @param aantalSpelers ingevoerde aantal
     * @throws InputMismatchException indien ongeldig aantal werd meegegeven
     */
    public void maakSpel(int aantalSpelers) throws InputMismatchException {
        setSpel(new Spel(aantalSpelers, kaartRepository));
    }

    /**
     * Wordt opgeroepen in UC1StartSpel wanneer een naam en geslacht voor een
     * speler zijn ingegeven
     * Vraagt aan spel om een nieuwe speler aan te maken met gegeven naam en geslacht
     * @param naam ingevoerde naam
     * @param geslacht ingevoerd geslacht
     * @throws InputMismatchException indien ongeldige naam of geslacht werd
     * meegegeven
     */
    public void maakSpeler(String naam, char geslacht) throws FouteSpelerGegevensException {
        geslacht = Character.toUpperCase(geslacht);
        Geslacht g;
        if(geslacht == 'M' | geslacht == 'H') {
            g = Geslacht.MAN;
        } else if (geslacht == 'V' | geslacht == 'F') {
            g = Geslacht.VROUW;
        }
        else {
            throw new FouteSpelerGegevensException("foutGeslacht");
        }
        spel.maakSpeler(naam, g);
    }
    /**
     * Maakt een tweedimensionale array aan met voor elke speler zijn gegevens in de
     * volgorde naam - geslacht - niveau - ras
     *
     * @return tweedimensionale array met gegevens
     */
    public String[][] geefOverzichtSpelers() {
        List<Speler> spelers = spel.getSpelers();
        String[][] overzicht = new String[spelers.size()][4];
        int i = 0;
        for (Speler s : spelers) {
            overzicht[i][0] = s.getNaam();
            overzicht[i][1] = s.getGeslacht().name();
            overzicht[i][2] = s.getNiveau() + "";
            overzicht[i][3] = s.getRas().name();
            i++;
        }
        return overzicht;
    }

    private void setSpel(Spel spel) {
        this.spel = spel;
    }

    public void beeindigBeurt() {
        spel.beeindigBeurt();
    }

    public String geefHuidigeSpeler() {
        return spel.geefHuidigeSpeler().getNaam();
    }

    public boolean heeftWinnaar() {
        return (spel.getWinnaar() != null);
    }

    public String geefWinnaar() {
        return spel.getWinnaar().getNaam();
    }

    public boolean isOpgeslagen() {
        return spel.isOpgeslagen();
    }

    /**
     * Vraagt aan repositories om spel met alle spelers en hun kaarten op te slaan
     */
    public void slaSpelOp() {
        int spelId = spelRepository.slaSpelOp(spel);
        for (Speler s : spel.getSpelers()) {
            int spelerId = spelerRepository.slaSpelerOp(s, spelId);
            kaartRepository.slaHandKaartenOp(s.getHandKaarten(), spelerId);
            kaartRepository.slaGedragenKaartenOp(s.getGedragenKaarten(), spelerId);
        }
        kaartRepository.slaGespeeldeKaartenOp(spel.getGespeeldeKaarten(), spelId);
        spelRepository.zetHuidigeSpeler(spel.geefHuidigeSpeler(), spelId);
        spel.setOpgeslagen(true);
    }

    /**
     * Controleert of naam uniek is en geeft deze eventueel door aan spel
     * @param naam                      gegeven spelnaam
     * @throws InputMismatchException   als naam al bestaat
     */
    public void zetSpelnaam(String naam) throws InputMismatchException {
        for (String s : spelRepository.geefSpelnamen()) {
            if (s.toLowerCase().equals(naam.toLowerCase())) {
                throw new InputMismatchException();
            }
        }
        spel.zetSpelnaam(naam);
    }

    /**
     * Geeft een tweedimensionale array terug met voor elk spel uit de spelRepository zijn naam en aantal spelers
     * @return      tweedimensionale array met gegevens
     */
    public String[][] geefSpellen() {
        String[][] spellen = new String[spelRepository.geefSpellen().size()][2];
        int i = 0;
        for (Spel spel : spelRepository.geefSpellen()) {
            spellen[i][0] = spel.getNaam();
            spellen[i][1] = spel.getAantalSpelers() + "";
            i++;
        }
        return spellen;
    }

    /**
     * Vraagt spel met alle spelers en hun kaarten aan repositories
     * Verwijdert spel uit database nadat het geladen is
     * @param spelNaam      gegeven spelNaam
     */
    public void laadSpel(String spelNaam) {
        Spel spel = spelRepository.geefSpellen().get(0);
        for (Spel s1 : spelRepository.geefSpellen()) {
            if (s1.getNaam().equals(spelNaam)) {
                spel = s1;
            }
        }
        int spelId = spelRepository.geefSpelId(spel.getNaam());
        String huidigeSpeler = spelRepository.geefHuidigeSpeler(spelId);
        List<Speler> spelers = spelerRepository.geefSpelers(spelId, spel);
        List<Kaart> spelerKaarten = new ArrayList<>();
        for (Speler s : spelers) {
            List<Kaart> handKaarten = kaartRepository.geefHandKaarten(spelId, s.getNaam());
            s.setHandKaarten(handKaarten);
            spelerKaarten.addAll(handKaarten);
            List<EquipmentKaart> gedragenKaarten = kaartRepository.geefGedragenKaarten(spelId, s.getNaam());
            s.setGedragenKaarten(gedragenKaarten);
            spelerKaarten.addAll(gedragenKaarten);
        }
        spel.setSpelers(spelers);
        spel.setHuidigeSpeler(huidigeSpeler);
        //kaarten
        List<Kaart> gespeeldeKaarten = kaartRepository.geefGespeeldeKaarten(spelId);
        spel.setGespeeldeKaarten(gespeeldeKaarten);
        List<KerkerKaart> kerkerKaarten = kaartRepository.geefKerkerKaarten();
        kerkerKaarten.removeAll(gespeeldeKaarten);
        kerkerKaarten.removeAll(spelerKaarten);
        List<SchatKaart> schatKaarten = kaartRepository.geefSchatKaarten();
        schatKaarten.removeAll(schatKaarten);
        schatKaarten.removeAll(spelerKaarten);
        KerkerKaartStapel kerkerKaartStapel = new KerkerKaartStapel(kaartRepository, kerkerKaarten);
        SchatKaartStapel schatKaartStapel = new SchatKaartStapel(kaartRepository, schatKaarten);
        spel.setKerkerKaartStapel(kerkerKaartStapel);
        spel.setSchatKaartStapel(schatKaartStapel);
        this.spel = spel;
        spelRepository.verwijderSpel(spel);
    }

    public String[][] geefSpelers() {
        return geefOverzichtSpelers();
    }

    /**
     * Maakt een tweedimensionale array aan met voor elke speler zijn gegevens in de
     * volgorde
     * naam - niveau - geslacht - gedragen kaarten
     *
     * @return tweedimensionale array met gegevens
     */
    public String[][] geefSpelOverzicht() {
        List<Speler> spelers = spel.getSpelers();
        String[][] overzicht = new String[spelers.size()][4];
        int i = 0;
        for (Speler s : spelers) {
            overzicht[i][0] = s.getNaam();
            overzicht[i][1] = "" + s.getNiveau();
            overzicht[i][2] = s.getGeslacht().name();
            List<String> kaarten = new ArrayList<>();
            s.getGedragenKaarten().forEach(k -> kaarten.add(k.getNaam()));
            overzicht[i][3] = String.join(", ", kaarten);
            i++;
        }
        return overzicht;
    }

    public String geefBovensteKaart() {
        KerkerKaart bovensteKaart = spel.geefBovensteKaart();
        return bovensteKaart.getNaam();
    }

    public boolean isMonsterKaart() {
        return (spel.geefBovensteKaart() instanceof MonsterKaart);
    }

    public boolean heeftDirectEffect() {
        return (spel.geefBovensteKaart() instanceof CurseKaart);
    }

    public void voerEffectUit() {
        spel.voerEffectUit();
    }

    /**
     * Geeft het resultaat van een effect op de huidige speler terug met zijn gegevens
     * in de volgorde
     * naam - geslacht - niveau - ras
     *
     * @return tweedimensionale array met resultaat
     */
    public String[] geefResultaat() {
        Speler speler = spel.geefHuidigeSpeler();
        String[] details = new String[4];
        details[0] = speler.getNaam();
        details[1] = speler.getGeslacht().name();
        details[2] = speler.getNiveau() + "";
        details[3] = speler.getRas().name();
        return details;
    }

    public void voegKaartBijHand() {
        spel.voegKaartBijHand();
    }

    /**
     * Controleert of de huidige speler meer dan 5 kaarten in zijn hand heeft
     * !! Voorlopig geeft dit altijd false terug omdat het beheren nog niet geïmplementeerd is
     * @return      false - meer dan 5 handkaarten
     *              true  - 5 of minder handkaarten
     */
    public boolean teveelKaarten() {
        //return spel.geefHuidigeSpeler().getHandKaarten().size() > 5;
        //TODO: UC7 beheer kaarten
        return false;
    }

    /**
     * Geeft voor de huidige speler de namen van al zijn handkaarten terug
     * @return      array met namen van handkaarten
     */
    public String[] geefHandKaarten() {
        List<Kaart> handKaarten = spel.geefHuidigeSpeler().getHandKaarten();
        String[] kaarten = new String[handKaarten.size()];
        for(int i = 0; i < kaarten.length; i++) {
            kaarten[i] = handKaarten.get(i).getNaam();
        }
        return kaarten;
    }

    public String geefAfbeeldingBovensteKaart() {
        return spel.geefBovensteKaart().getAfbeeldig();
    }

    /**
     * Geeft voor de huidige speler de padnamen van de afbeeldingen van al zijn handkaarten terug
     * @return      array met padnamen van handkaarten
     */
    public String[] geefAfbeeldingenHandKaarten() {
        List<Kaart> handKaarten = spel.geefHuidigeSpeler().getHandKaarten();
        String[] afbeeldingen = new String[handKaarten.size()];
        for (int i = 0; i <  afbeeldingen.length; i++) {
            afbeeldingen[i] = handKaarten.get(i).getAfbeeldig();
        }
        return afbeeldingen;
    }

    /**
     * Geeft voor de gegeven speler de padnamen van de afbeeldingen van al zijn gedragen kaarten terug
     * @param spelerNaam    gegeven naam van speler
     * @return              array met padnamen van gedragen kaarten
     */
    public String[] geefAfbeeldingenGedragenKaarten(String spelerNaam) {
        Speler speler = spel.geefHuidigeSpeler();
        for(Speler s : spel.getSpelers()) {
            if (s.getNaam().equals(spelerNaam)) {
                speler = s;
            }
        }
        List<EquipmentKaart> gedragenKaarten = speler.getGedragenKaarten();
        String[] afbeeldingen = new String[gedragenKaarten.size()];
        for (int i = 0; i <  afbeeldingen.length; i++) {
            afbeeldingen[i] = gedragenKaarten.get(i).getAfbeeldig();
        }
        return afbeeldingen;
    }
}
