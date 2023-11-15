package exchangemage.effects.targeting;

import exchangemage.base.Observable;
import exchangemage.base.Observer;
import exchangemage.effects.base.Effect;

/**
 * An interface for all objects that can be targeted by {@link Effect}s. All elements of the game
 * which can become targets of effects (such as <i>cards</i> or <i>actors</i>) should implement
 * this interface.
 * <br><br>
 * This interface extends the {@link Observable} interface and defines a set of
 * {@link TargetableEvent}s so that {@link Observer}s can be notified of events specific to
 * targetable objects.
 *
 * @see Effect
 * @see Observable
 * @see Observer
 */
public interface Targetable extends Observable {
    /**
     * An enum defining events specific to {@link Targetable} objects. Used to notify
     * {@link Observer}s of the objects implementing this interface.
     *
     * @see Observable
     * @see Observer
     */
    enum TargetableEvent implements Observable.Event {
        /**
         * Event used to notify {@link Observer}s that the {@link Targetable} object can potentially
         * be selected for the {@link Effect} whose target is currently being chosen.
         *
         * @see Observer
         * @see Effect
         */
        ACTIVATED,
        /**
         * Event used to notify {@link Observer}s that the {@link Targetable} object can no longer
         * be selected by the {@link Effect} whose target is currently being chosen.
         *
         * @see Observer
         * @see Effect
         */
        DEACTIVATED,
        /**
         * Event used to notify {@link Observer}s that the {@link Targetable} object has been
         * selected for the {@link Effect} whose target is currently being chosen.
         *
         * @see Observer
         * @see Effect
         */
        SELECTED,
        /**
         * Event used to notify {@link Observer}s that the {@link Targetable} object has been
         * deselected as the target for the {@link Effect} whose target is currently being chosen.
         */
        DESELECTED;
    }

    /**
     * Notifies all {@link Observer}s of this {@link Targetable} object that the object has been
     * activated (can be selected for the {@link Effect} whose target is currently being chosen).
     *
     * @see Observer
     * @see Effect
     */
    public default void activate() {
        notifyObservers(TargetableEvent.ACTIVATED);
    }

    /**
     * Notifies all {@link Observer}s of this {@link Targetable} object that the object has been
     * deactivated (can no longer be selected for the {@link Effect} whose target is currently
     * being chosen).
     *
     * @see Observer
     * @see Effect
     */
    public default void deactivate() {
        notifyObservers(TargetableEvent.DEACTIVATED);
    }

    /**
     * Notifies all {@link Observer}s of this {@link Targetable} object that the object has been
     * selected for the {@link Effect} whose target is currently being chosen.
     *
     * @see Observer
     * @see Effect
     */
    public default void select() {
        notifyObservers(TargetableEvent.SELECTED);
    }

    /**
     * Notifies all {@link Observer}s of this {@link Targetable} object that the object has been
     * deselected as the target for the {@link Effect} whose target is currently being chosen.
     *
     * @see Observer
     * @see Effect
     */
    public default void deselect() {
        notifyObservers(TargetableEvent.DESELECTED);
    }
}