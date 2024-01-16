package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.actors.Player;
import exchangemage.actors.Actor;
import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;
import exchangemage.effects.triggers.conditions.comparators.SubjectComparator;

/**
 * A {@link Condition} which compares a subject retrieved from the current game state by a
 * {@link SubjectGetter} against a given value/object/type using a {@link SubjectComparator}. The
 * subject getter and comparator provided to the constructor decide exactly which object is to be
 * tested and how will the comparison be performed.
 * <br><br>
 * Can be used to evaluate all kinds of statements about the current game state, such as:
 * <ul>
 *     <li>whether the {@link Player}'s health is below/above/at a certain value</li>
 *     <li>whether an {@link Actor} targeted by the current {@link Effect} has a certain status
 *     affecting them</li>
 *     <li>whether the current {@link Effect} is of a certain type</li>
 * </ul>
 *
 * @param <T> the type of the subject retrieved by the subject getter and compared by the subject
 * comparator
 * @see Condition
 * @see SubjectGetter
 * @see SubjectComparator
 */
public class ComparisonCondition<T> implements Condition {
    /**
     * The {@link SubjectGetter} used to retrieve the subject of the comparison from the current
     * game state.
     */
    private final SubjectGetter<T> subjectGetter;

    /**
     * The {@link SubjectComparator} used to compare the subject retrieved by the subject getter.
     */
    private final SubjectComparator<T> subjectComparator;

    /**
     * @param subjectGetter the {@link SubjectGetter} used to retrieve the subject of the comparison
     * @param subjectComparator the {@link SubjectComparator} used to compare the subject retrieved
     *                          by the subject getter
     */
    public ComparisonCondition(SubjectGetter<T> subjectGetter,
                               SubjectComparator<T> subjectComparator) {
        Objects.requireNonNull(subjectGetter, "SubjectGetter cannot be null.");
        Objects.requireNonNull(subjectComparator, "SubjectComparator cannot be null.");
        this.subjectGetter = subjectGetter;
        this.subjectComparator = subjectComparator;
    }

    /**
     * Evaluates whether the subject retrieved by the {@link SubjectGetter} fulfills the condition
     * imposed by the {@link SubjectComparator}.
     *
     * @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise
     */
    @Override
    public boolean isFulfilled() {
        return this.subjectComparator.compare(this.subjectGetter.getSubject());
    }
}
