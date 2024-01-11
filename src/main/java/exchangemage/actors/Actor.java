package exchangemage.actors;

import java.util.Set;

import exchangemage.scenes.Scene;
import exchangemage.scenes.Encounter;
import exchangemage.scenes.TurnPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.HealEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

/**
 * Interface for classes used to represent any friendly or hostile entities which can be present
 * in a {@link Scene}. Actors are {@link Targetable} and can be affected by
 * {@link PersistentEffect}s.
 * <br><br>
 * All actors have certain common qualities:
 * <ul>
 *     <li>
 *         <b>Ability to receive damage</b> - Actors can represent their health in various ways,
 *         but all can end up on the receiving end of a {@link DamageEffect} and should define their
 *         behavior when they do using the {@link #takeDamage} method.
 *         <blockquote>
 *             <i>Specific non-standard reactions to - or ways of - taking damage should
 *             generally be represented by a custom set of {@link PersistentEffect}s held by the
 *             actor. When a damage effect reaches the end of its resolution its final, modified
 *             value will be assumed as representative of the damage taken by the actor by any
 *             persistent effects evaluated at the
 *             {@link EffectPlayer.EffectResolutionStage#RESPONSE} resolution stage.</i>
 *         </blockquote>
 *     </li>
 *     <br>
 *     <li>
 *         <b>Ability to heal</b> - Similarly to receiving damage, actors should define their
 *         behavior when they are healed using the {@link #heal} method.
 *     </li>
 *     <br>
 *     <li>
 *         <b>Ability to take turns</b> - Actors are expected to take turns during the resolution
 *         of an {@link Encounter} and should define their behavior during their turn using the
 *         {@link #takeTurn} method.
 *     </li>
 * </ul>
 */
public interface Actor extends Targetable, PersistentEffectsHolder, EffectSource {
    /**
     * Returns the set of {@link Targetable}s held by this actor (if any).
     *
     * @return the set of targetables held by this actor or an empty set if none are held
     * @see Targetable
     */
    Set<Targetable> getTargetables();

    /**
     * Describes the behavior of this actor during its turn. Called by the {@link Encounter}'s
     * {@link TurnPlayer} when it is this actor's turn to take an action.
     *
     * @see TurnPlayer
     */
    void takeTurn();

    /**
     * Describes the behavior of this actor when receiving damage.
     *
     * @param damage the amount of damage received
     * @see DamageEffect
     */
    void takeDamage(int damage);

    /**
     * Describes the behavior of this actor when healed.
     *
     * @param healing the amount of healing received
     * @see HealEffect
     */
    void heal(int healing);
}
