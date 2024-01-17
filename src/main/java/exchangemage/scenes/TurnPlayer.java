package exchangemage.scenes;

import exchangemage.base.GameStateLocator;
import exchangemage.base.Notification;
import exchangemage.base.Observable;
import exchangemage.base.Observer;
import exchangemage.actors.Actor;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;

import java.util.Objects;

/**
 * Interface for classes used to manage the turns of {@link Actor}s during the resolution of an
 * {@link Encounter}. The turn player logic decides which actor takes their turn when, and what
 * actions are performed during that turn.
 * <br><br>
 * Sets of turns can be logically grouped into rounds. A round will <i>typically</i> see each
 * actor present in the encounter take their turn once. However, this may not always be the case
 * due to influence of different effects or particular implementations of the turn player logic.
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
        /** Event used to notify {@link Observer}s that a new round has started. */
        ROUND_STARTED,
        /** Event used to notify {@link Observer}s that a round has ended. */
        ROUND_ENDED,
        /** Event used to notify {@link Observer}s that an {@link Actor}'s turn has started. */
        TURN_STARTED,
        /** Event used to notify {@link Observer}s that an {@link Actor}'s turn has ended. */
        TURN_ENDED
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

    // ----------------------------- default notification methods ----------------------------- //

    /**
     * Notifies {@link Observer}s of the current {@link Encounter} of the
     * {@link TurnPlayerEvent#TURN_STARTED} event and plays a {@link NotificationEffect} with the
     * {@link Actor} whose turn has started as the {@link EffectSource}.
     *
     * @param actor the actor whose turn has started
     */
    default void notifyTurnStarted(Actor actor) {
        notifyTurnPlayerEvent(TurnPlayerEvent.TURN_STARTED, actor);
    }

    /**
     * Notifies {@link Observer}s of the current {@link Encounter} of the
     * {@link TurnPlayerEvent#TURN_ENDED} event and plays a {@link NotificationEffect} with the
     * {@link Actor} whose turn has ended as the {@link EffectSource}.
     *
     * @param actor the actor whose turn has ended
     */
    default void notifyTurnEnded(Actor actor) {
        notifyTurnPlayerEvent(TurnPlayerEvent.TURN_ENDED, actor);
    }

    /**
     * Notifies {@link Observer}s of the current {@link Encounter} of the
     * {@link TurnPlayerEvent#ROUND_STARTED} event and plays a {@link NotificationEffect}.
     */
    default void notifyRoundStarted() {
        notifyTurnPlayerEvent(TurnPlayerEvent.ROUND_STARTED,
                              GameStateLocator.getGameState().getScene());
    }

    /**
     * Notifies {@link Observer}s of the current {@link Encounter} of the
     * {@link TurnPlayerEvent#ROUND_ENDED} event and plays a {@link NotificationEffect}.
     */
    default void notifyRoundEnded() {
        notifyTurnPlayerEvent(TurnPlayerEvent.ROUND_ENDED,
                              GameStateLocator.getGameState().getScene());
    }

    /**
     * Notifies {@link Observer}s of the current {@link Encounter} of the given
     * {@link TurnPlayerEvent} and calls on the {@link EffectPlayer} to play a
     * {@link NotificationEffect} with the given event and source.
     *
     * @param event  the event to notify observers of and play a notification effect for
     * @param source the source of the notification effect
     * @throws NullPointerException if the event or source is <code>null</code>
     */
    default void notifyTurnPlayerEvent(TurnPlayerEvent event, EffectSource source) {
        Objects.requireNonNull(event, "Cannot notify observers of null event.");
        Objects.requireNonNull(source, "Notification effect source cannot be null.");
        GameStateLocator.getGameState().getScene().notifyObservers(event);
        GameStateLocator.getGameState().getEffectPlayer().evaluateEffect(
                new NotificationEffect(event, source)
        );
    }
}
