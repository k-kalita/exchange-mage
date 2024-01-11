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
     * Constructs a new {@link NotificationCondition} with the given target {@link Notification}.
     *
     * @param notification the target notification
     */
    public NotificationCondition(Notification notification) {
        super(new NotificationGetter(), new ValueComparator<>(notification));
    }
}
