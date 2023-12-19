package exchangemage.effects.triggers.conditions;

import exchangemage.base.GameState;
import exchangemage.effects.base.Effect;

/**
 * A {@link SubclassGetter} which returns the {@link Effect} currently in resolution as the subject
 * for its {@link Condition} (provided that the current effect is an instance of the specified
 * effect type).
 *
 * @param <S> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Effect
 */
public class EffectInResolutionGetter<S extends Effect<?>> extends SubclassGetter<Effect<?>, S> {
    /**
     * Creates a new {@link EffectInResolutionGetter} with given {@link Effect} subclass.
     *
     * @param effectSubclass the class of {@link Effect} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given effect class is <code>null</code>.
     * @see Effect
     */
    public EffectInResolutionGetter(Class<S> effectSubclass) {
        super(GameState::getEffectInResolution, effectSubclass);
    }
}
