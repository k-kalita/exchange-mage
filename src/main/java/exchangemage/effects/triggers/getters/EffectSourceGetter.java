package exchangemage.effects.triggers.getters;

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
     * @param effectSourceSubclass the class of {@link EffectSource} which can be returned by this
     *                             getter
     * @param effectGetter         the {@link SubjectGetter} used to retrieve the effect whose
     *                             source is returned by this getter (provided that it is an
     *                             instance of the specified effect source type)
     */
    public EffectSourceGetter(Class<S> effectSourceSubclass,
                              SubjectGetter<? extends Effect<?>> effectGetter) {
        super(() -> effectGetter.getSubject().getSource(), effectSourceSubclass);
    }
}
