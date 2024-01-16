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
     * @param targetableSubclass the class of {@link Targetable}s which can be returned by this
     *                           getter
     * @param effectGetter       the {@link SubjectGetter} used to retrieve the effect whose target
     *                           is returned by this getter (provided that it is an instance of the
     *                           specified targetable type)
     */
    public EffectTargetGetter(Class<S> targetableSubclass,
                              SubjectGetter<? extends Effect<?>> effectGetter) {
        super(() -> effectGetter.getSubject().getTarget(), targetableSubclass);

    }
}
