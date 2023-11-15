package exchangemage.base;

/**
 * An interface for objects that can be observed by {@link Observer}s. Used to decouple the visual
 * representation of the game from the game logic. All elements of the game which possess a visual
 * representation (such as <i>cards</i>, <i>effects</i>, and <i>actors</i>) should implement this
 * as well as their own {@link Observable.Event} enum which defines the element-specific events
 * their observers should be notified of (e.g. a card might define an event for when it is played or
 * discarded).
 *
 * @see Observer
 * @see Observable.Event
 */
public interface Observable {
    /**
     * An interface for events that can be observed by {@link Observer}s. Any class implementing the
     * {@link Observable} interface should define events specific to itself in an enum that
     * implements this interface and use them as arguments when calling the
     * {@link Observable#notifyObservers} method.
     *
     * @see Observable
     * @see Observer
     */
    public interface Event {}

    /**
     * Adds an {@link Observer} to this {@link Observable} object. The observer can then be notified
     * of certain events by calling the {@link Observable#notifyObservers} method.
     *
     * @param observer the observer to add
     *
     * @see Observer
     */
    public void addObserver(Observer observer);

    /**
     * Removes an {@link Observer} from this {@link Observable} object. The observer will no longer
     * be notified of any events.
     *
     * @param observer the observer to remove
     *
     * @see Observer
     */
    public void removeObserver(Observer observer);

    /**
     * Basic validation for an {@link Observer} object. Should be called before adding an
     * observer to this {@link Observable} object.
     *
     * @param observer the observer to validate
     *
     * @throws IllegalArgumentException if the observer is null
     */
    public default void validateObserver(Observer observer) {
        if (observer == null) throw new IllegalArgumentException("Observer cannot be null");
    }

    /**
     * Notifies all {@link Observer}s of this {@link Observable} object of the given event.
     *
     * @param event the event to notify observers of
     *
     * @see Observer
     * @see Observable.Event
     */
    public void notifyObservers(Event event);
}
