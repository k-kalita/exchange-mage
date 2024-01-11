package exchangemage.effects.triggers.conditions.getters;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * A {@link SubclassGetter} which returns the {@link Effect} currently in evaluation as the subject
 * for its {@link Condition} (provided that the current effect is an instance of the specified
 * effect type).
 *
 * @param <S> the type of {@link Effect} which can be returned by this {@link SubjectGetter}
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Effect
 */
public class EffectInEvaluationGetter<S extends Effect<?>> extends SubclassGetter<Effect<?>, S> {
    /**
     * Creates a new effect-in-evaluation getter with given {@link Effect} subclass.
     *
     * @param effectSubclass the class of {@link Effect} objects returned by this
     *                       {@link SubjectGetter}
     * @throws NullPointerException if the given effect class is <code>null</code>
     * @see EffectPlayer#getEffectInEvaluation()
     */
    public EffectInEvaluationGetter(Class<S> effectSubclass) {
        super(GameState::getEffectInEvaluation, effectSubclass);
    }
}