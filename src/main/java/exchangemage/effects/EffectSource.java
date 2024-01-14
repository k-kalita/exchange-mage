package exchangemage.effects;

import exchangemage.cards.Card;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * An interface for objects which represent possible sources of an {@link Effect} (e.g. a
 * {@link Card} or a {@link PersistentEffectsHolder}).
 * <br><br>
 * Effect sources may be referenced in the {@link Condition}s of {@link PersistentEffect}
 * activation {@link Trigger}s (e.g. a persistent effect may be activated only if the source of
 * the triggering effect is a {@link Card} played by the player).
 */
public interface EffectSource {}
