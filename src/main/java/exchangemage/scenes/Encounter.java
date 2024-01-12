package exchangemage.scenes;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import exchangemage.base.Notification;
import exchangemage.base.Observer;
import exchangemage.base.Observable;
import exchangemage.actors.Enemy;
import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.cards.Card;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;

/**
 * A {@link Scene} used to represent an encounter between the {@link Player} and a set of
 * {@link Enemy}s. Encounters use a {@link TurnPlayer} to manage the flow of turns during which
 * {@link Actor}s can perform actions (most commonly playing {@link Card}s).
 *
 * @see Scene
 * @see TurnPlayer
 */
public class Encounter extends Scene {
    /**
     * An enum defining events specific to {@link Encounter} events. Can be used to notify
     * {@link Encounter} {@link Observer}s and create {@link NotificationEffect}s used to trigger
     * {@link PersistentEffect}s which activate at the start or end of an {@link Encounter}.
     *
     * @see Encounter
     * @see NotificationEffect
     * @see TurnPlayer.TurnPlayerEvent
     */
    public enum EncounterEvent implements Observable.Event, Notification {
        /**
         * Event used to notify {@link Observer}s that an {@link Encounter} has started.
         *
         * @see Observer
         * @see Encounter
         */
        ENCOUNTER_START,
        /**
         * Event used to notify {@link Observer}s that an {@link Encounter} has ended.
         *
         * @see Observer
         * @see Encounter
         */
        ENCOUNTER_END
    }

    /**
     * The {@link TurnPlayer} used to manage the flow of turns during the encounter.
     */
    private final TurnPlayer turnPlayer;

    /**
     * Constructs a new encounter with the specified {@link TurnPlayer}, {@link PersistentEffect}s,
     * and enemies.
     *
     * @param turnPlayer           the turn player used to manage the flow of turns during the
     *                             encounter
     * @param environmentalEffects the set of persistent effects active during the encounter
     * @param enemies              the set of enemies present during the encounter
     * @throws NullPointerException if the turn player or enemies are <code>null</code>
     * @see Enemy
     */
    public Encounter(TurnPlayer turnPlayer,
                     Set<PersistentEffect> environmentalEffects,
                     Set<Enemy> enemies) {
        super(environmentalEffects);
        Objects.requireNonNull(turnPlayer, "Turn player cannot be null");
        Objects.requireNonNull(enemies, "Enemies cannot be null");
        this.turnPlayer = turnPlayer;
        this.actors.addAll(enemies);
    }

    /**
     * Constructs a new encounter with the specified {@link TurnPlayer} and enemies.
     *
     * @param turnPlayer the turn player used to manage the flow of turns during the encounter
     * @param enemies    the set of enemies present during the encounter
     * @throws NullPointerException if the turn player or enemies are <code>null</code>
     * @see Enemy
     */
    public Encounter(TurnPlayer turnPlayer,
                     Set<Enemy> enemies) {
        this(turnPlayer, null, enemies);
    }

    /**
     * Starts the encounter by initializing  and starting the {@link #turnPlayer}.
     *
     * @see TurnPlayer
     */
    @Override
    public void start() {
        turnPlayer.init(this);
        turnPlayer.start();
    }

    /**
     * @return the enemies present in the {@link Encounter}
     * @see Enemy
     */
    public Set<Enemy> getEnemies() {
        return actors.stream()
                     .filter(actor -> actor instanceof Enemy)
                     .map(actor -> (Enemy) actor)
                     .collect(Collectors.toSet());
    }

    /**
     * @return <code>true</code> if any enemies are alive, <code>false</code> otherwise
     * @see Enemy
     */
    public boolean enemiesAlive() {
        return getEnemies().stream().anyMatch(actor -> !actor.isDead());
    }
}
