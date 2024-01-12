package exchangemage.effects.triggers.conditions.getters;

import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * A {@link SubclassGetter} which returns the {@link Effect} provided by a {@link SubjectGetter} as
 * the subject for its {@link Condition} (provided that the current effect is an instance of the
 * specified effect type).
 *
 * @param <S> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Effect
 */
public class EffectSubclassGetter<S extends Effect<?>> extends SubclassGetter<Effect<?>, S> {
    /**
     * Creates a new effect subclass getter with given {@link Effect} subclass and effect getter.
     *
     * @param effectSubclass the class of effect objects returned by this {@link SubjectGetter}
     * @param effectGetter   the subject getter which provides the effect to be returned by this
     *                       getter
     */
    public EffectSubclassGetter(Class<S> effectSubclass,
                                SubjectGetter<? extends Effect<?>> effectGetter) {
        super(effectGetter::getSubject, effectSubclass);
    }
}
