package domein;

import java.util.Stack;

public abstract class KaartStapel {

    protected Stack<Kaart> stapel;
    protected KaartRepository kaartRepository;

    public KaartStapel(KaartRepository kaartRepository) {
        stapel = new Stack<>();
        this.kaartRepository = kaartRepository;
    }

    public Kaart neemBovensteKaart() {
        return stapel.pop();
    }

    abstract void startNieuweStapel();
}
