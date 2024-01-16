package exchangemage.effects.triggers.conditions;

import exchangemage.base.Notification;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.triggers.conditions.getters.NotificationGetter;
import exchangemage.effects.triggers.conditions.comparators.ValueComparator;

/**
 * A {@link ComparisonCondition} fulfilled if the {@link Notification} retrieved from the
 * {@link NotificationEffect} currently in resolution is equal to the given target notification.
 *
 * @see Notification
 */
public class NotificationCondition extends ComparisonCondition<Notification> {
    /**
     * @param notification the target {@link Notification} whose equality with the notification
     *                     carried by the {@link NotificationEffect} currently in resolution
     *                     fulfills this condition
     */
    public NotificationCondition(Notification notification) {
        super(new NotificationGetter(), new ValueComparator<>(notification));
    }
}
