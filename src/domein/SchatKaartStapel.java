package domein;

import java.util.*;

public class SchatKaartStapel extends KaartStapel {

    public SchatKaartStapel(KaartRepository kaartRepository) {
        super(kaartRepository);
    }

    public SchatKaartStapel(KaartRepository kaartRepository, List<SchatKaart> schatKaarten) {
        super(kaartRepository);
        Collections.shuffle(schatKaarten);
        stapel.addAll(schatKaarten);
    }

    @Override
    public void startNieuweStapel() {
        List<SchatKaart> kaarten = kaartRepository.geefSchatKaarten();
        Collections.shuffle(kaarten);
        stapel.addAll(kaarten);
    }

}
