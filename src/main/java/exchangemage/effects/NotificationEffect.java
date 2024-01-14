package exchangemage.effects;

import exchangemage.base.Notification;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.scenes.Scene;
import exchangemage.effects.targeting.selectors.SceneSelector;

import java.util.Objects;

/**
 * A simple {@link Effect} used to alert {@link PersistentEffect}s in the {@link Scene} of certain
 * events which could potentially trigger them (e.g. <i>the start of a new turn</i> or <i>an
 * enemy's death</i>).
 * <br><br>
 * Classes which are responsible for reporting such events should implement their own
 * {@link Notification}s to represent the events related persistent effects might be interested in.
 *
 * @see Effect
 * @see PersistentEffect
 */
public class NotificationEffect extends Effect<Scene> {
    /** The {@link Notification} representing the event this effect is alerting of. */
    private final Notification notification;

    /**
     * @param notification the {@link Notification} used to represent the event this
     *                     effect is alerting of
     * @param source       {@link EffectSource} which this notification effect pertains to
     * @throws NullPointerException if the notification or source are null
     */
    public NotificationEffect(Notification notification, EffectSource source) {
        super(null,
              () -> true,
              new SceneSelector(),
              ResolutionMode.ENQUEUE_ON_TOP);

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

    /** @return a description generated with the help of the {@link Notification#getText} method */
    @Override
    public String toString() {return "Notifies of: " + this.notification.getText(getSource());}

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

    /** @return a description generated with the help of the {@link Notification#getText} method */
    @Override
    public String getDescription() {return this.toString();}

    /** @return this effect's {@link #notification} */
    public Notification getNotification() {return notification;}
}
