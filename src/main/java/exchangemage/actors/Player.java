package exchangemage.actors;

import java.util.Set;

import exchangemage.cards.Deck;
import exchangemage.effects.targeting.Targetable;

public class Player extends DeckHolderActor {
    public Player(Deck deck) {
        super(deck);
    }

    @Override
    public Set<Targetable> getTargetables() {
        return null;
    }

    @Override
    public void takeTurn() {

    }

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    public void heal(int healing) {

    }
}
