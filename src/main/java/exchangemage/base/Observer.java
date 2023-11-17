package exchangemage.base;

/**
 * An interface for objects which can become observers of {@link Observable} objects. Used to
 * decouple the visual representation of the game from the game logic. All classes used to visually
 * represent elements of the game (such as <i>cards</i>, <i>effects</i>, and <i>actors</i>) should
 * implement this interface.
 * <br><br>
 * Observer objects will be notified by their corresponding {@link Observable} objects of
 * {@link Observable.Event}s and should implement the {@link Observer#update} method to
 * appropriately handle these events.
 *
 * @see Observable
 */
public interface Observer {
    /**
     * Method called by {@link Observable} objects to notify this {@link Observer} of an event.
     *
     * @param publisher the {@link Observable} object that called this method
     * @param event the event to notify this observer of
     *
     * @see Observable
     * @see Observable.Event
     */
    public void update(Observable publisher, Observable.Event event);
}
