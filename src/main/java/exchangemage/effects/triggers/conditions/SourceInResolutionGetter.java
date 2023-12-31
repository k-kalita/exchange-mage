package exchangemage.effects.triggers.conditions;

import exchangemage.base.GameState;
import exchangemage.effects.EffectSource;

/**
 * A {@link SubclassGetter} which returns the current {@link EffectSource} as the subject for its
 * {@link Condition} (provided that the current source is an instance of the specified effect source
 * type).
 *
 * @param <S> the type of {@link EffectSource} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see EffectSource
 */
public class SourceInResolutionGetter<S extends EffectSource>
        extends SubclassGetter<EffectSource, S> {
    /**
     * Creates a new {@link SourceInResolutionGetter} with given {@link EffectSource} subclass.
     *
     * @param effectSourceSubclass the class of {@link EffectSource} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given effect source class is <code>null</code>.
     * @see EffectSource
     */
    public SourceInResolutionGetter(Class<S> effectSourceSubclass) {
        super(() -> GameState.getEffectInResolution().getSource(), effectSourceSubclass);
    }
}
