package exchangemage.effects.triggers.conditions.getters;

import exchangemage.base.GameState;
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
     * Returns the {@link Notification} carried by the {@link NotificationEffect} currently in
     * resolution or <code>null</code> if no such effect is in resolution.
     *
     * @return current notification or <code>null</code> if none is in resolution
     * @see Notification
     */
    @Override
    public Notification getSubject() {
        var effect = GameState.getEffectInResolution();
        if (effect instanceof NotificationEffect)
            return ((NotificationEffect) effect).getNotification();
        return null;
    }
}
