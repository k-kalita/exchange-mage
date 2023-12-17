package exchangemage.effects.targeting;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import exchangemage.effects.base.Effect;
import exchangemage.effects.base.EffectPlayer;
import exchangemage.effects.targeting.selectors.TargetSelector;

/**
 * Helper class fot the {@link EffectPlayer} used to manage the process of selecting a target for
 * an {@link Effect}. It is responsible for:
 * <ul>
 *     <li>maintaining a set of forbidden {@link Targetable}s (targetables that cannot be
 *     selected as the target for a given effect even if they are returned by that effect's
 *     {@link TargetSelector}'s {@link TargetSelector#getActiveTargetables} method),</li>
 *     <li>calling the {@link TargetSelector#getActiveTargetables} and
 *     {@link TargetSelector#selectTarget} methods of the active effect's selector to create a set
 *     of active targetables and choose a target from it.</li>
 *     <li>calling the {@link Targetable#notifyObservers} method of targetables when they become
 *     active or inactive.</li>
 * </ul>
 * TargetingManager's methods are called by the {@link EffectPlayer} in the process of evaluating
 * an effect. If the target cannot be chosen (indicated by the return value of the
 * {@link #selectTarget} method), the effect is not resolved.
 *
 * @see Effect
 * @see EffectPlayer
 * @see TargetSelector
 * @see Targetable
 */
public class TargetingManager {
    /**
     * A set of {@link Targetable}s that can potentially be selected as the target by the active
     * {@link TargetSelector}.
     */
    private final Set<Targetable> activeTargetables = new HashSet<>();

    /**
     * A set of {@link Targetable}s that cannot be selected as the target by the active
     * {@link TargetSelector}.
     */
    private final Set<Targetable> forbiddenTargetables = new HashSet<>();

    /**
     * The active {@link Effect}.
     */
    private Effect activeEffect;

    /**
     * Sets the given {@link Effect} as the active effect. The effect's {@link TargetSelector}
     * method {@link TargetSelector#getActiveTargetables} is called to create a set of active
     * targetables (targetables which can potentially be selected by the active selector) and the
     * {@link Targetable#notifyObservers} method is called on each of them (if the forbidden
     * targetables set is not empty, the active targetables set is filtered to exclude them).
     * <br><br>
     * If the effect already has a target the set of active targetables is not created.
     *
     * @param effect the effect to set as the active effect.
     * @return this {@link TargetingManager}
     * @throws NullPointerException  if the given effect is null.
     * @throws IllegalStateException if there is already an active effect.
     * @see Effect
     * @see TargetSelector
     * @see Targetable
     */
    public TargetingManager setActiveEffect(Effect effect) {
        Objects.requireNonNull(effect, "Cannot set null active effect.");

        if (this.activeEffect != null)
            throw new IllegalStateException("Cannot set active effect while another is active.");

        this.activeEffect = effect;

        if (this.activeEffect.hasTarget())
            return this;

        this.activeTargetables.addAll(effect.getTargetSelector().getActiveTargetables());
        this.activeTargetables.removeAll(forbiddenTargetables);
        this.activeTargetables.forEach(
                targetable -> targetable.notifyObservers(Targetable.TargetableEvent.ACTIVATED)
        );

        return this;
    }

    /**
     * Calls the {@link TargetSelector#selectTarget} method of the active {@link Effect}'s
     * {@link TargetSelector} to choose a target from the set of active {@link Targetable}s.
     * Then clears the active effect and the set of active targetables.
     * <br><br>
     * If the effect already has a target it is not chosen again.
     *
     * @return <code>true</code> if a target has been successfully selected (or the effect
     * already had a target), <code>false</code> otherwise.
     * @throws NullPointerException if there is no active effect.
     * @see Effect
     * @see TargetSelector
     * @see Targetable
     */
    public boolean selectTarget() {
        Objects.requireNonNull(this.activeEffect,
                               "Cannot select target while no effect is active.");

        boolean result = this.activeEffect.hasTarget() ||
                this.activeEffect.selectTarget(this.activeTargetables);
        clearActiveEffect();
        return result;
    }

    /**
     * Adds the given {@link Targetable} to the set of forbidden targetables. The targetable will
     * be excluded from the set of targetables which can potentially be selected by the active
     * {@link Effect}'s {@link TargetSelector}.
     *
     * @param targetable the targetable to add to the set of forbidden targetables.
     * @throws NullPointerException  if the given targetable is null.
     * @throws IllegalStateException if there is an active effect.
     * @see Effect
     * @see Targetable
     * @see TargetSelector
     */
    public void addForbiddenTargetable(Targetable targetable) {
        Objects.requireNonNull(targetable,
                               "Cannot add null targetable to forbidden targetables.");
        if (this.activeEffect != null)
            throw new IllegalStateException(
                    "Cannot add forbidden targetable while an effect is active."
            );

        this.forbiddenTargetables.add(targetable);
    }

    /**
     * Clears the set of forbidden {@link Targetable}s.
     *
     * @see Targetable
     */
    public void clearForbiddenTargetables() {this.forbiddenTargetables.clear();}

    /**
     * Clears the set of active {@link Targetable}s and calls the {@link Targetable#notifyObservers}
     * method on each of them to notify of their deactivation.
     *
     * @see Targetable
     */
    private void clearActiveTargetables() {
        this.activeTargetables.forEach(
                targetable -> targetable.notifyObservers(Targetable.TargetableEvent.DEACTIVATED)
        );
        this.activeTargetables.clear();
    }

    /**
     * Clears the active {@link Effect} and the set of active {@link Targetable}s.
     *
     * @throws NullPointerException if there is no active effect.
     * @see Effect
     * @see Targetable
     */
    private void clearActiveEffect() {
        Objects.requireNonNull(this.activeEffect,
                               "Cannot clear active effect while no effect is active.");

        clearActiveTargetables();
        this.activeEffect = null;
    }
}
