package exchangemage.actors;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

import exchangemage.base.Observer;
import exchangemage.cards.Deck;
import exchangemage.cards.Card;
import exchangemage.base.Observable;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;

/**
 * Base abstract class for all {@link Actor}s which hold a {@link Deck} and whose actions revolve
 * around playing {@link Card}s from that deck. The {@link Player} character and all standard
 * {@link Enemy} types are deck holders.
 * <br><br>
 * This base class also provides default implementations of the actor's {@link Observable} and
 * {@link PersistentEffectsHolder} methods.
 *
 * @see Deck
 * @see Actor
 */
public abstract class DeckHolderActor implements Actor {
    /**
     * The {@link Deck} representing the abilities of this actor.
     */
    private final Deck deck;

    /**
     * The set of {@link PersistentEffect}s currently affecting this actor.
     */
    private final Set<PersistentEffect> persistentEffects = new HashSet<>();

    /**
     * The set of {@link Observer}s currently observing this actor.
     */
    private final Set<Observer> observers = new HashSet<>();

    /**
     * Constructs a new deck holder actor with the specified {@link Deck}.
     *
     * @param deck this actor's deck
     * @throws NullPointerException if the deck is <code>null</code>
     */
    public DeckHolderActor(Deck deck) {
        Objects.requireNonNull(deck, "Actor's deck cannot be null.");
        this.deck = deck;
    }

    /**
     * Returns this actor's {@link Deck}
     *
     * @return this actor's deck
     */
    public Deck getDeck() {return this.deck;}

    // -------------------------- persistent effects holder methods --------------------------- //

    @Override
    public void addPersistentEffect(PersistentEffect effect) {
        Objects.requireNonNull(effect, "Cannot add null persistent effect.");
        if (this.persistentEffects.contains(effect))
            throw new IllegalArgumentException("Cannot add duplicate persistent effect.");
        effect.setSource(this);
        this.persistentEffects.add(effect);
    }

    @Override
    public void removePersistentEffect(PersistentEffect effect) {
        Objects.requireNonNull(effect, "Cannot remove null persistent effect.");
        if (!this.persistentEffects.contains(effect))
            throw new IllegalArgumentException("Cannot remove persistent effect not present.");
        this.persistentEffects.remove(effect);
    }

    @Override
    public Set<PersistentEffect> getPersistentEffects() {return this.persistentEffects;}

    // ---------------------------------- observable methods ---------------------------------- //

    @Override
    public Set<Observer> getObservers() {return this.observers;}


    @Override
    public void addObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot add null observer.");
        if (this.observers.contains(observer))
            throw new IllegalArgumentException("Cannot add duplicate observer.");
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot remove null observer.");
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException("Cannot remove observer not present.");
        this.observers.remove(observer);
    }
}
