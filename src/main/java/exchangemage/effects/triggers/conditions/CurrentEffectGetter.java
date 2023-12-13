package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.base.GameState;
import exchangemage.effects.base.Effect;

/**
 * A {@link SubjectGetter} which returns the current {@link Effect} as the subject for its
 * {@link Condition} (provided that the current effect is an instance of the specified effect
 * type).
 *
 * @param <T> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubjectGetter
 * @see Effect
 */
public class CurrentEffectGetter<T extends Effect> implements SubjectGetter<T> {
    /**
     * The class of the {@link Effect} objects returned by this {@link SubjectGetter}.
     */
    private final Class<T> effectClass;

    /**
     * Creates a new {@link CurrentEffectGetter} with given {@link Effect} class.
     *
     * @param effectClass the class of {@link Effect} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given effect class is <code>null</code>.
     * @see Effect
     */
    public CurrentEffectGetter(Class<T> effectClass) {
        Objects.requireNonNull(effectClass, "Effect class cannot be null.");
        this.effectClass = effectClass;
    }

    /**
     * Returns the current {@link Effect} from the {@link GameState} if it is an instance of the
     * effect class specified in this getter.
     *
     * @return the current {@link Effect} or <code>null</code> if the current effect is not an
     * instance of the specified type.
     * @throws IllegalStateException if there is no current effect.
     * @see Effect
     * @see GameState
     */
    @Override
    public T getSubject() {
        Effect currentEffect = GameState.getCurrentEffect();
        if (currentEffect == null)
            throw new IllegalStateException("No current effect.");
        if (!this.effectClass.isInstance(currentEffect))
            return null;
        return this.effectClass.cast(currentEffect);
    }
}
