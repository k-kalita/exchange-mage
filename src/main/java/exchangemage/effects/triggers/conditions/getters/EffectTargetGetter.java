package exchangemage.effects.triggers.conditions.getters;

import exchangemage.effects.Effect;
import exchangemage.effects.targeting.Targetable;

/**
 * A {@link SubclassGetter} which returns the target of the effect retrieved by the specified
 * {@link SubjectGetter} (provided that the current target is an instance of the specified
 * {@link Targetable} subclass).
 *
 * @param <S> the type of {@link Targetable} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Targetable
 */
public class EffectTargetGetter<S extends Targetable> extends SubclassGetter<Targetable, S> {
    /**
     * Creates a new {@link EffectTargetGetter} with given {@link Targetable} subclass and effect
     * {@link SubjectGetter}.
     *
     * @param targetableSubclass the class of targetable objects returned by this subject getter
     * @param effectGetter       the subject getter used to retrieve the effect whose target is
     *                           returned by this getter
     */
    public EffectTargetGetter(Class<S> targetableSubclass,
                              SubjectGetter<? extends Effect<?>> effectGetter) {
        super(() -> effectGetter.getSubject().getTarget(), targetableSubclass);

    }
}
