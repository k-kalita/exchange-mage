package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.base.GameState;
import exchangemage.effects.base.Effect;

/**
 * A {@link SubclassGetter} which returns the current {@link Effect} as the subject for its
 * {@link Condition} (provided that the current effect is an instance of the specified effect
 * type).
 *
 * @param <S> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Effect
 */
public class CurrentEffectGetter<S extends Effect> extends SubclassGetter<Effect, S> {
    /**
     * Creates a new {@link CurrentEffectGetter} with given {@link Effect} subclass.
     *
     * @param effectSubclass the class of {@link Effect} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given effect class is <code>null</code>.
     * @see Effect
     */
    public CurrentEffectGetter(Class<S> effectSubclass) {
        super(GameState::getCurrentEffect, effectSubclass);
    }
}
