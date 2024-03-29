package exchangemage.effects.triggers;

import exchangemage.base.Notification;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.triggers.getters.NotificationGetter;
import exchangemage.effects.triggers.conditions.ValueCondition;

/**
 * A {@link ConditionalTrigger} fulfilled if the {@link Notification} retrieved from the
 * {@link NotificationEffect} currently in resolution is equal to the given target notification.
 *
 * @see Notification
 */
public class NotificationTrigger extends ConditionalTrigger {
    /**
     * @param notification the target {@link Notification} whose equality with the notification
     *                     carried by the {@link NotificationEffect} currently in resolution
     *                     fulfills this condition
     */
    public NotificationTrigger(Notification notification) {
        super(new NotificationGetter(), new ValueCondition(notification));
    }
}
