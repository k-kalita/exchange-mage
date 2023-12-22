package exchangemage.effects;

import exchangemage.cards.Card;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * An interface for objects which represent possible sources of an {@link Effect} (e.g. a
 * {@link Card} or a {@link PersistentEffect}). Effect sources may be referenced in the
 * {@link Condition}s of persistent effects' activation {@link Trigger}s (e.g. a persistent effect
 * may be activated only if the source of the triggering effect is a {@link Card} played by the
 * player).
 *
 * @see Effect
 * @see PersistentEffect
 * @see Trigger
 */
public interface EffectSource {}
