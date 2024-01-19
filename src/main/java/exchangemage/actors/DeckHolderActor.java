package exchangemage.actors;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

import exchangemage.base.Observer;
import exchangemage.cards.Deck;
import exchangemage.cards.Card;
import exchangemage.base.Observable;
import exchangemage.scenes.Encounter;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.HealEffect;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;

/**
 * Base abstract class for all {@link Actor}s which hold a {@link Deck} and whose actions revolve
 * around playing {@link Card}s from that deck. The {@link Player} character and all standard
 * {@link Enemy} types are deck holders.
 * <br><br>
 * This base class also provides default implementations of the actor's {@link Observable} and
 * {@link PersistentEffectsHolder} methods, as well as logic related to the actor's health.
 *
 * @see Deck
 * @see Actor
 */
public abstract class DeckHolderActor implements Actor {
    /** The {@link Deck} representing the abilities of this actor. */
    private final Deck deck;

    /** The set of {@link PersistentEffect}s currently affecting this actor. */
    private final Set<PersistentEffect> persistentEffects = new HashSet<>();

    /** The set of {@link Observer}s currently observing this actor. */
    private final Set<Observer> observers = new HashSet<>();

    /** The maximum, starting health value of the enemy. */
    private final int maxHealth;

    /** The current health value of the enemy. */
    private int currentHealth;

    /** Whether the enemy has been damaged during current {@link Encounter}. */
    private boolean damagedThisEncounter = false;

    /** Whether the enemy has been damaged during current round. */
    private boolean damagedThisRound = false;

    /**
     * @param deck              this actor's {@link Deck}
     * @param maxHealth         this actor's maximum health
     * @param persistentEffects the set of {@link PersistentEffect}s affecting this actor
     * @throws NullPointerException     if the deck is <code>null</code>
     * @throws IllegalArgumentException if the max health is not positive
     */
    public DeckHolderActor(Deck deck, int maxHealth, Set<PersistentEffect> persistentEffects) {
        Objects.requireNonNull(deck, "Actor's deck cannot be null.");
        if (maxHealth <= 0)
            throw new IllegalArgumentException("Actor's max health must be positive.");
        this.deck = deck;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        if (persistentEffects != null)
            persistentEffects.forEach(this::addPersistentEffect);
    }

    /** @return this actor's {@link Deck} */
    public Deck getDeck() {return this.deck;}

    // ------------------------------------ health methods ------------------------------------ //

    /**
     * Sets this actor's {@link #currentHealth} to the specified value.
     * @param currentHealth the new value of this actor's current health
     * @throws IllegalArgumentException if the specified value is negative
     */
    public void setCurrentHealth(int currentHealth) {
        if (currentHealth < 0)
            throw new IllegalArgumentException("Actor's current health cannot be negative.");
        this.currentHealth = currentHealth;
    }

    /**
     * Receives the specified amount of damage and calls on the {@link #notifyOfEvent} method to
     * alert {@link Observer}s and the {@link Encounter} of any relevant {@link ActorEvent}s.
     * <br><br>
     * If as a result of this method health is reduced to zero, the {@link #die} method is called.
     *
     * @param damage the amount of damage received (ignored if negative)
     * @see Actor.ActorEvent
     * @see DamageEffect
     */
    @Override
    public void receiveDamage(int damage) {
        if (damage <= 0)
            return;

        this.currentHealth = Math.max(0, this.currentHealth - damage);
        notifyOfEvent(ActorEvent.DAMAGE_RECEIVED);

        if (!this.damagedThisEncounter) {
            this.damagedThisEncounter = true;
            notifyOfEvent(ActorEvent.FIRST_DAMAGE_THIS_ENCOUNTER_RECEIVED);
        }

        if (!this.damagedThisRound) {
            this.damagedThisRound = true;
            notifyOfEvent(ActorEvent.FIRST_DAMAGE_THIS_TURN_RECEIVED);
        }

        if (this.currentHealth == 0)
            die();
    }

    /**
     * Heals the specified amount of health and calls on the {@link #notifyOfEvent} method to alert
     * {@link Observer}s and the {@link Encounter} of any relevant {@link ActorEvent}s.
     *
     * @param healing the amount of healing received (ignored if negative)
     * @see Actor.ActorEvent
     * @see HealEffect
     */
    @Override
    public void heal(int healing) {
        if (healing <= 0)
            return;

        boolean healed = this.currentHealth < this.maxHealth;
        this.currentHealth = Math.min(this.maxHealth, this.currentHealth + healing);
        notifyOfEvent(ActorEvent.HEALING_RECEIVED);

        if (this.currentHealth == this.maxHealth && healed)
            notifyOfEvent(ActorEvent.MAX_HEALTH_REACHED);
    }

    /**
     * Called when this actor's health is reduced to zero. Notifies {@link Observer}s and the
     * {@link Encounter} of the {@link ActorEvent#DEATH} event.
     */
    @Override
    public void die() {notifyOfEvent(ActorEvent.DEATH);}

    /**
     * @return <code>true</code> if this actor's {@link #currentHealth} is zero,
     * <code>false</code> otherwise
     */
    @Override
    public boolean isDead() {return this.currentHealth == 0;}

    /** @return this actor's {@link #maxHealth} */
    public int getMaxHealth() {return this.maxHealth;}

    /** @return this actor's {@link #currentHealth} */
    public int getCurrentHealth() {return this.currentHealth;}

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
