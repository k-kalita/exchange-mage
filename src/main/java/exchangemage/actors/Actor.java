package exchangemage.actors;

import java.util.Objects;
import java.util.Set;

import exchangemage.base.GameState;
import exchangemage.base.Notification;
import exchangemage.base.Observable;
import exchangemage.base.Observer;
import exchangemage.scenes.Scene;
import exchangemage.scenes.Encounter;
import exchangemage.scenes.TurnPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.NotificationEffect;
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
 *         behavior when they do using the {@link #receiveDamage} method.
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
 *     <br>
 *     <li>
 *         <b>Ability to die</b> - Actors should define their behavior when their health is
 *         depleted using the {@link #die} method.
 *     </li>
 * </ul>
 */
public interface Actor extends Targetable, PersistentEffectsHolder, EffectSource {
    /**
     * An enum defining events specific to {@link Actor} operations. Can be used to notify
     * its {@link Observer}s and create {@link NotificationEffect}s used to trigger
     * {@link PersistentEffect}s which activate in response to events such as the actor's death
     * or being healed/damaged.
     *
     * @see NotificationEffect
     * @see Observable
     */
    enum ActorEvent implements Observable.Event, Notification {
        /**
         * Event used to notify {@link Observer}s that an {@link Actor} has died.
         */
        DEATH,
        /**
         * Event used to notify {@link Observer}s that an {@link Actor} has received damage.
         */
        DAMAGE_RECEIVED,
        /**
         * Event used to notify {@link Observer}s that an {@link Actor} has received damage for the
         * first time during current {@link Encounter}.
         */
        FIRST_DAMAGE_THIS_ENCOUNTER_RECEIVED,
        /**
         * Event used to notify {@link Observer}s that an {@link Actor} has received damage for the
         * first time during current turn.
         */
        FIRST_DAMAGE_THIS_TURN_RECEIVED,
        /**
         * Event used to notify {@link Observer}s that an {@link Actor} has been healed.
         */
        HEALING_RECEIVED,
        /**
         * Event used to notify {@link Observer}s that an {@link Actor}'s health has been restored
         * to its maximum value.
         */
        MAX_HEALTH_REACHED;
    }

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
    void receiveDamage(int damage);

    /**
     * Describes the behavior of this actor when healed.
     *
     * @param healing the amount of healing received
     * @see HealEffect
     */
    void heal(int healing);

    /**
     * Describes the behavior of this actor when its health is depleted.
     */
    void die();

    /**
     * Notifies this actor's {@link Observer}s of the specified {@link ActorEvent} and calls
     * on the {@link EffectPlayer} to play a {@link NotificationEffect} for the event.
     *
     * @param event the event to notify the observers and play an effect for
     * @see Observer
     * @see NotificationEffect
     * @throws NullPointerException if the event is <code>null</code>
     */
    default void notifyOfEvent(ActorEvent event) {
        Objects.requireNonNull(event, "Cannot notify of null event.");
        notifyObservers(event);
        GameState.getEffectPlayer().evaluateEffect(new NotificationEffect(event, this));
    }
}
