package exchangemage.scenes;

import exchangemage.base.Notification;
import exchangemage.base.Observable;
import exchangemage.base.Observer;
import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.actors.Enemy;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;

/**
 * Interface for classes used to manage the turns of {@link Actor}s during the resolution of an
 * {@link Encounter}. The turn player logic decides which actor takes their turn when, and what
 * actions are performed during that turn.
 *
 * @see Encounter
 */
public interface TurnPlayer {
    /**
     * An enum defining events specific to {@link TurnPlayer} operations. Can be used to notify
     * {@link Encounter} {@link Observer}s and create {@link NotificationEffect}s used to trigger
     * {@link PersistentEffect}s which activate at the start or end of an {@link Actor}'s turn.
     */
    enum TurnPlayerEvent implements Observable.Event, Notification {
        /**
         * Event used to notify {@link Observer}s that the turn player has started an
         * {@link Actor}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        TURN_STARTED,
        /**
         * Event used to notify {@link Observer}s that the turn player has ended an
         * {@link Actor}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        TURN_ENDED,
        /**
         * Event used to notify {@link Observer}s that the turn player has started the
         * {@link Player}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        PLAYER_TURN_STARTED,
        /**
         * Event used to notify {@link Observer}s that the turn player has ended the
         * {@link Player}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        PLAYER_TURN_ENDED,
        /**
         * Event used to notify {@link Observer}s that the turn player has started an
         * {@link Enemy}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        ENEMY_TURN_STARTED,
        /**
         * Event used to notify {@link Observer}s that the turn player has ended an
         * {@link Enemy}'s turn.
         *
         * @see Observer
         * @see Actor
         */
        ENEMY_TURN_ENDED;
    }

    /**
     * Initializes the turn player with the given {@link Encounter}. This method should be called
     * before the turn player is used to manage the turns of {@link Actor}s in the encounter.
     *
     * @param encounter the encounter to initialize the turn player with
     */
    void init(Encounter encounter);

    /**
     * Starts the turn player. This method should be called after the turn player is initialized.
     *
     * @see TurnPlayer#init
     */
    void start();
}
