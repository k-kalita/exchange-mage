package exchangemage.effects.triggers.conditions;

import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;
import exchangemage.effects.triggers.conditions.getters.EffectInEvaluationGetter;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.comparators.TypeComparator;

/**
 * A {@link Condition} fulfilled if the {@link Effect} returned by the specified
 * {@link SubjectGetter} is an instance of the specified type.
 * <br><br>
 * Standard {@link ComparisonCondition} using the {@link TypeComparator} does not work for
 * parameterized types, so this class is necessary for checking effect types.
 *
 * @see Effect
 */
@SuppressWarnings("rawtypes")
public class EffectTypeCondition implements Condition {
    /** The {@link SubjectGetter} used to retrieve the {@link Effect} to check. */
    private final SubjectGetter<Effect<?>> subjectGetter;

    /** The type against which the retrieved {@link Effect} is checked. */
    private final Class<? extends Effect> type;

    /**
     * @param subjectGetter the {@link SubjectGetter} used to retrieve the {@link Effect} to check
     *                      against the specified type
     * @param type          the type against which the retrieved {@link Effect} is checked
     * @see EffectInEvaluationGetter
     * @see EffectInResolutionGetter
     */
    public EffectTypeCondition(SubjectGetter<Effect<?>> subjectGetter,
                               Class<? extends Effect> type) {
        this.subjectGetter = subjectGetter;
        this.type = type;
    }

    /**
     * @return <code>true</code> if the {@link Effect} returned by the specified
     * {@link SubjectGetter} is an instance of the specified type, <code>false</code> otherwise
     */
    @Override
    public boolean isFulfilled() {return type.isInstance(subjectGetter.getSubject());}
}
