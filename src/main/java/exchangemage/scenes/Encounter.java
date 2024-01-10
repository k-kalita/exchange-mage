package exchangemage.scenes;

import java.util.Objects;
import java.util.Set;

import exchangemage.actors.Enemy;
import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.cards.Card;
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
     * The {@link TurnPlayer} used to manage the flow of turns during the encounter.
     */
    private final TurnPlayer turnPlayer;

    /**
     * Constructs a new encounter with the specified {@link TurnPlayer}, {@link PersistentEffect}s,
     * and {@link Enemy}s.
     *
     * @param turnPlayer           the turn player used to manage the flow of turns during the
     *                             encounter
     * @param environmentalEffects the set of persistent effects active during the encounter
     * @param enemies              the set of enemies present during the encounter
     * @throws NullPointerException if the turn player or enemies are <code>null</code>
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
     * Starts the encounter by initializing  and starting the {@link #turnPlayer}.
     *
     * @see TurnPlayer
     */
    @Override
    public void start() {
        turnPlayer.init(this);
        turnPlayer.start();
    }
}
