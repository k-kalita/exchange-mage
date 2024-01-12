package exchangemage.actors;

import java.util.Set;
import java.util.HashSet;

import exchangemage.cards.Deck;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

public class Player extends DeckHolderActor {
    public Player(Deck deck, int maxHealth, Set<PersistentEffect> persistentEffects) {
        super(deck, maxHealth, persistentEffects);
    }

    public Player(Deck deck, int maxHealth) {
        super(deck, maxHealth, null);
    }

    @Override
    public Set<Targetable> getTargetables() {
        Set<Targetable> targetables = new HashSet<>(this.getPersistentEffects());
        targetables.addAll(this.getDeck().getTargetables());
        return targetables;
    }

    @Override
    public void takeTurn() {

    }
}
