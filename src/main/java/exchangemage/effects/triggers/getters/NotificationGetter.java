package exchangemage.effects.triggers.getters;

import exchangemage.base.GameStateLocator;
import exchangemage.base.Notification;
import exchangemage.effects.NotificationEffect;

/**
 * A {@link SubjectGetter} which returns the {@link Notification} carried by the
 * {@link NotificationEffect} currently in resolution.
 *
 * @see Notification
 */
public class NotificationGetter implements SubjectGetter<Notification> {
    /**
     * @return the {@link Notification} carried by the {@link NotificationEffect} currently in
     * resolution or <code>null</code> if no such effect is in resolution.
     */
    @Override
    public Notification getSubject() {
        var effect = GameStateLocator.getGameState().getEffectInResolution();
        if (effect instanceof NotificationEffect)
            return ((NotificationEffect) effect).getNotification();
        return null;
    }
}
