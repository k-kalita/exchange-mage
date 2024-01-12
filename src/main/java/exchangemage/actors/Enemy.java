package exchangemage.actors;

import java.util.Set;
import java.util.HashSet;

import exchangemage.cards.Deck;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

public class Enemy extends DeckHolderActor {
    public Enemy(Deck deck, int maxHealth, Set<PersistentEffect> persistentEffects) {
        super(deck, maxHealth, persistentEffects);
    }

    public Enemy(Deck deck, int maxHealth) {
        super(deck, maxHealth, null);
    }

    @Override
    public Set<Targetable> getTargetables() {return new HashSet<>(this.getPersistentEffects());}

    @Override
    public void takeTurn() {

    }
}
