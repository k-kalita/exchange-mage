package exchangemage.effects.triggers;

import java.util.Objects;

import exchangemage.actors.Player;
import exchangemage.actors.Actor;
import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.Condition;
import exchangemage.effects.triggers.conditions.NonNullCondition;
import exchangemage.effects.triggers.getters.SubjectGetter;

/**
 * A {@link Trigger} which activates only if a subject retrieved by a specified
 * {@link SubjectGetter} fulfills a requirement imposed by a given {@link Condition}.
 * <br><br>
 * The specific combination of subject getter and condition can be used to evaluate all kinds of
 * statements about the current game state, such as:
 * <ul>
 *     <li>whether the {@link Player}'s health is below/above/at a certain value</li>
 *     <li>whether an {@link Actor} targeted by the {@link Effect} currently in resolution has a
 *     certain status affecting them</li>
 *     <li>whether the {@link Effect} currently in resolution is of a certain type</li>
 * </ul>
 *
 * @see SubjectGetter
 * @see Condition
 */
public class ConditionalTrigger implements Trigger {
    /** The {@link SubjectGetter} used to retrieve the subject of the condition. */
    private final SubjectGetter<?> subjectGetter;

    /** The {@link Condition} representing the requirement imposed on the subject. */
    private final Condition condition;

    /**
     * @param subjectGetter the {@link SubjectGetter} used to retrieve the subject of the
     *                      condition
     * @param condition     the {@link Condition} representing the requirement imposed on the
     *                      subject, if not provided the trigger will activate if the subject is
     *                      not <code>null</code>
     * @throws NullPointerException if the {@link SubjectGetter} is <code>null</code>
     */
    public ConditionalTrigger(SubjectGetter<?> subjectGetter,
                              Condition condition) {
        Objects.requireNonNull(subjectGetter, "SubjectGetter cannot be null.");
        this.subjectGetter = subjectGetter;
        this.condition     = condition;
    }

    /**
     * @return <code>true</code> if the subject retrieved by the {@link #subjectGetter} fulfills
     * the requirement represented by the {@link #condition} (or if the trigger does not have a
     * condition, if the subject is not <code>null</code>), <code>false</code> otherwise
     */
    @Override
    public boolean isActivated() {
        Object subject = this.subjectGetter.getSubject();

        if (subject == null)
            return false;
        if (this.condition == null)
            return true;

        return this.condition.evaluate(subject);
    }
}
