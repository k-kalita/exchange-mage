package exchangemage.effects.triggers.conditions.getters;

import exchangemage.effects.Effect;
import exchangemage.effects.EffectSource;

/**
 * A {@link SubclassGetter} which returns the source of the effect retrieved by the specified
 * {@link SubjectGetter} (provided that the current source is an instance of the specified effect
 * source type).
 *
 * @param <S> the type of {@link EffectSource} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see EffectSource
 */
public class EffectSourceGetter<S extends EffectSource> extends SubclassGetter<EffectSource, S> {
    /**
     * Creates a new {@link EffectSourceGetter} with given {@link EffectSource} subclass and effect
     * {@link SubjectGetter}.
     *
     * @param effectSourceSubclass the class of effect source objects returned by this subject
     *                             getter
     * @param effectGetter         the subject getter used to retrieve the effect whose source is
     *                             returned by this getter
     */
    public EffectSourceGetter(Class<S> effectSourceSubclass,
                              SubjectGetter<? extends Effect<?>> effectGetter) {
        super(() -> effectGetter.getSubject().getSource(), effectSourceSubclass);
    }
}
