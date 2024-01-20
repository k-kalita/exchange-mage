package exchangemage.effects.triggers.getters;

import exchangemage.effects.Effect;
import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link SubclassGetter} which returns the {@link Effect} provided by a {@link SubjectGetter} as
 * the subject for its {@link ConditionalTrigger} (provided that the current effect is an
 * instance of the specified effect type).
 *
 * @param <S> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Effect
 */
@SuppressWarnings("rawtypes")
public class EffectSubclassGetter<S extends Effect> extends SubclassGetter<Effect, S> {
    /**
     * @param effectSubclass the class of {@link Effect}s which can be returned by this getter
     * @param effectGetter   the {@link SubjectGetter} used to retrieve the effect which is returned
     *                       by this getter (provided that it is an instance of the specified effect
     *                       type)
     */
    public EffectSubclassGetter(Class<S> effectSubclass,
                                SubjectGetter<? extends Effect> effectGetter) {
        super(effectGetter::getSubject, effectSubclass);
    }
}
