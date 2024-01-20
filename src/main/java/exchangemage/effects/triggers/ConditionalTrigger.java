package exchangemage.effects.triggers;

import java.util.Objects;

import exchangemage.actors.Player;
import exchangemage.actors.Actor;
import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.Condition;
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
 * @param <T> the type of subject retrieved by the subject getter and expected by the condition
 * @see SubjectGetter
 * @see Condition
 */
public class ConditionalTrigger<T> implements Trigger {
    /** The {@link SubjectGetter} used to retrieve the subject of the condition. */
    private final SubjectGetter<T> subjectGetter;

    /** The {@link Condition} representing the requirement imposed on the subject. */
    private final Condition<T> condition;

    /**
     * @param subjectGetter the {@link SubjectGetter} used to retrieve the subject of the
     *                      condition
     * @param condition     the {@link Condition} representing the requirement imposed on the
     *                      subject
     * @throws NullPointerException if either the subject getter or the condition is
     *                              <code>null</code>
     */
    public ConditionalTrigger(SubjectGetter<T> subjectGetter,
                              Condition<T> condition) {
        Objects.requireNonNull(subjectGetter, "SubjectGetter cannot be null.");
        Objects.requireNonNull(condition, "Condition cannot be null.");
        this.subjectGetter = subjectGetter;
        this.condition = condition;
    }

    /**
     * @return <code>true</code> if the subject retrieved by the {@link #subjectGetter} fulfills
     * the requirement represented by the {@link #condition}, <code>false</code> otherwise
     */
    @Override
    public boolean isActivated() {
        return this.condition.evaluate(this.subjectGetter.getSubject());
    }
}
