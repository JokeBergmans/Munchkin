package domein;

import java.util.Collections;
import java.util.List;

public class KerkerKaartStapel extends KaartStapel {


    public KerkerKaartStapel(KaartRepository kaartRepository) {
        super(kaartRepository);
    }

    public KerkerKaartStapel(KaartRepository kaartRepository, List<KerkerKaart> kerkerKaarten) {
        super(kaartRepository);
        Collections.shuffle(kerkerKaarten);
        stapel.addAll(kerkerKaarten);
    }

    @Override
    public void startNieuweStapel() {
        List<KerkerKaart> kaarten = kaartRepository.geefKerkerKaarten();
        Collections.shuffle(kaarten);
        stapel.addAll(kaarten);
    }


}
