package exchangemage.effects.triggers;

import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.TypeCondition;
import exchangemage.effects.triggers.getters.SubjectGetter;
import exchangemage.effects.triggers.getters.EffectInEvaluationGetter;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;

import java.util.Objects;

/**
 * A {@link Trigger} which activates only if the {@link Effect} returned by the specified
 * {@link SubjectGetter} is an instance of the specified type.
 * <br><br>
 * Standard {@link ConditionalTrigger} using the {@link TypeCondition} does not work for
 * parameterized types, so this class is necessary for checking effect types.
 *
 * @see Effect
 */
@SuppressWarnings("rawtypes")
public class EffectTypeTrigger implements Trigger {
    /** The {@link SubjectGetter} used to retrieve the {@link Effect} to check. */
    private final SubjectGetter<Effect<?>> effectGetter;

    /** The type against which the retrieved {@link Effect} is checked. */
    private final Class<? extends Effect> type;

    /**
     * @param effectGetter the {@link SubjectGetter} used to retrieve the {@link Effect} to check
     *                     the type of
     * @param type         the type against which the retrieved {@link Effect} is checked
     * @throws NullPointerException if either the effect getter or the type is <code>null</code>
     * @see EffectInEvaluationGetter
     * @see EffectInResolutionGetter
     */
    public EffectTypeTrigger(SubjectGetter<Effect<?>> effectGetter,
                             Class<? extends Effect> type) {
        Objects.requireNonNull(effectGetter, "Effect getter cannot be null.");
        Objects.requireNonNull(type, "Effect type cannot be null.");
        this.effectGetter = effectGetter;
        this.type = type;
    }

    /**
     * @return <code>true</code> if the {@link Effect} returned by the specified
     * {@link SubjectGetter} is an instance of the specified type, <code>false</code> otherwise
     */
    @Override
    public boolean isActivated() {
        return this.type.isInstance(this.effectGetter.getSubject());
    }
}
