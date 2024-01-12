package exchangemage.effects.triggers.conditions.getters;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * A {@link SubjectGetter} which returns the {@link Effect} currently in evaluation as the subject
 * for its {@link Condition}.
 *
 * @see SubjectGetter
 * @see Effect
 * @see EffectPlayer
 */
public class EffectInEvaluationGetter implements SubjectGetter<Effect<?>> {
    /**
     * @return the {@link Effect} currently being evaluated by the {@link EffectPlayer}
     */
    @Override
    public Effect<?> getSubject() {return GameState.getEffectInEvaluation();}
}
