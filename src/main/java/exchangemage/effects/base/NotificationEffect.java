package exchangemage.effects.base;

import exchangemage.base.Notification;
import exchangemage.base.Observable;
import exchangemage.encounters.Scene;
import exchangemage.effects.triggers.ConstValueTrigger;
import exchangemage.effects.targeting.SceneSelector;

import java.util.Objects;

/**
 * A simple {@link Effect} used to alert {@link PersistentEffect}s in the {@link Scene} of certain
 * events which could potentially activate them (e.g. <i> the start of a new turn</i> or <i>an
 * enemy's death</i>).
 * <br><br>
 * {@link EffectSource}s which are responsible for reporting such events should implement their own
 * {@link Notification}s to represent the events related persistent effects might be interested in.
 *
 * @see Effect
 * @see PersistentEffect
 */
public class NotificationEffect extends Effect {
    /**
     * The {@link Notification} used to represent the event this {@link NotificationEffect} is
     * alerting of.
     */
    private final Notification notification;

    /**
     * Creates a new {@link NotificationEffect} with given {@link Notification} and {@link
     * EffectSource}. The source should be the object which is responsible for reporting the event
     * this {@link Effect} is alerting of.
     *
     * @param notification the notification used to represent the event this effect is alerting of
     * @param source       object reporting the event
     * @throws NullPointerException if the notification or source are null
     * @see Notification
     * @see EffectSource
     * @see Effect
     */
    public NotificationEffect(Notification notification, EffectSource source) {
        super(
                new ConstValueTrigger(true),
                new ConstValueTrigger(true),
                new SceneSelector(),
                ResolutionMode.IMMEDIATE
        );

        Objects.requireNonNull(
                notification,
                "Cannot create notification effect with null notification."
        );
        Objects.requireNonNull(
                source,
                "Cannot create notification effect with null source."
        );

        this.notification = notification;
        setSource(source);
    }

    /**
     * This method does not have any effect.
     * <br><br>
     * {@link NotificationEffect}s are used solely to alert {@link PersistentEffect}s in the
     * {@link Scene} of certain events which could potentially activate them.
     *
     * @see PersistentEffect
     */
    @Override
    public void execute() {}

    /**
     * Returns the {@link Notification} used to represent the event this {@link NotificationEffect}
     * is alerting of.
     *
     * @return this effect's notification
     * @see Notification
     */
    public Notification getNotification() {return notification;}
}
