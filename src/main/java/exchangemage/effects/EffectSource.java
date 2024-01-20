package exchangemage.effects;

import exchangemage.actors.Player;
import exchangemage.cards.Card;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * An interface for objects which represent possible sources of an {@link Effect} (e.g. a
 * {@link Card} or a {@link PersistentEffectsHolder}).
 * <br><br>
 * Effect sources may be referenced in the {@link ConditionalTrigger}s of {@link PersistentEffect}
 * (e.g. a persistent effect may be activated only if the source of the triggering effect is the
 * {@link Player}).
 */
public interface EffectSource {}
