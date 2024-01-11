package exchangemage.actors;

import java.util.Set;
import java.util.HashSet;

import exchangemage.cards.Deck;
import exchangemage.scenes.Encounter;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

public class Enemy extends DeckHolderActor {
    /**
     * The maximum, starting health value of the enemy.
     */
    private final int maxHealth;

    /**
     * The current health value of the enemy.
     */
    private int currentHealth;

    /**
     * Whether the enemy has been damaged during current {@link Encounter}.
     */
    private boolean damagedThisEncounter = false;

    /**
     * Whether the enemy has been damaged during current turn.
     */
    private boolean damagedThisTurn = false;

    public Enemy(Deck deck, Set<PersistentEffect> persistentEffects, int maxHealth) {
        super(deck);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        if (persistentEffects != null)
            persistentEffects.forEach(this::addPersistentEffect);
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

        if (!this.damagedThisTurn) {
            this.damagedThisTurn = true;
            notifyOfEvent(ActorEvent.FIRST_DAMAGE_THIS_TURN_RECEIVED);
        }

        if (this.currentHealth == 0) {
            die();
            notifyOfEvent(ActorEvent.DEATH);
        }
    }

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

    @Override
    public void die() {

    }
}
