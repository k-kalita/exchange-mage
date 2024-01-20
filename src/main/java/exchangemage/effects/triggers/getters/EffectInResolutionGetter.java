package exchangemage.effects.triggers.getters;

import exchangemage.base.GameStateLocator;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link SubjectGetter} which returns the {@link Effect} currently in resolution as the subject
 * for its {@link ConditionalTrigger}.
 *
 * @see SubjectGetter
 * @see Effect
 * @see EffectPlayer
 */
public class EffectInResolutionGetter implements SubjectGetter<Effect<?>> {
    /** @return the {@link Effect} currently being resolved by the {@link EffectPlayer} */
    @Override
    public Effect<?> getSubject() {return GameStateLocator.getGameState().getEffectInResolution();}
}
