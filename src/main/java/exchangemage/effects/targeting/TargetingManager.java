package exchangemage.effects.targeting;

import java.util.Set;
import java.util.HashSet;

import exchangemage.effects.base.Effect;
import exchangemage.effects.base.EffectPlayer;

/**
 * Helper class fot the {@link EffectPlayer} used to manage the process of selecting a target for
 * an {@link Effect}. It is responsible for:
 * <ul>
 *     <li>maintaining a set of forbidden {@link Targetable}s (targetables that cannot be
 *     selected as the target for a given {@link Effect} even if they are returned by that effect's
 *     {@link TargetSelector}'s {@link TargetSelector#getActiveTargetables} method),</li>
 *     <li>calling the {@link TargetSelector#getActiveTargetables} and
 *     {@link TargetSelector#chooseTarget} methods of the active selector to create a set of
 *     active targetables and choose a target from it.</li>
 *     <li>calling the {@link Targetable#notifyObservers} method of targetables when they become
 *     active or inactive.</li>
 * </ul>
 * TargetingManager's methods are called by the {@link EffectPlayer} in the process of enqueuing
 * an effect. If the target cannot be chosen (indicated by the return value of the
 * {@link #chooseTarget} method), the effect is not enqueued.
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
     * The active {@link TargetSelector}.
     */
    private TargetSelector activeTargetSelector;

    /**
     * Sets the given {@link TargetSelector} as the active selector. The selector's
     * {@link TargetSelector#getActiveTargetables} method is called to create a set of active
     * targetables (targetables which can potentially be selected by the active selector) and the
     * {@link Targetable#notifyObservers} method is called on each of them (if the forbidden
     * targetables set is not empty, the active targetables set is filtered
     * to exclude them).
     *
     * @param targetSelector the target selector to set as the active selector.
     *
     * @throws IllegalArgumentException if the given target selector is null.
     * @throws IllegalStateException if there is already an active target selector.
     *
     * @see TargetSelector
     * @see Targetable
     */
    public void setActiveTargetSelector(TargetSelector targetSelector) {
        if (targetSelector == null)
            throw new IllegalArgumentException("Cannot set null target selector.");
        if (this.activeTargetSelector != null)
            throw new IllegalStateException("Cannot set target selector while another is active.");

        this.activeTargetSelector = targetSelector;
        this.activeTargetables.addAll(targetSelector.getActiveTargetables());
        this.activeTargetables.removeAll(forbiddenTargetables);
        this.activeTargetables.forEach(
                targetable -> targetable.notifyObservers(Targetable.TargetableEvent.ACTIVATED)
        );
    }

    /**
     * Calls the {@link TargetSelector#chooseTarget} method of the active {@link TargetSelector}
     * to choose a target from the set of active {@link Targetable}s. Then clears the current
     * selector and the set of active targetables.
     *
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise.
     *
     * @throws IllegalStateException if there is no active target selector.
     *
     * @see TargetSelector
     * @see Targetable
     */
    public boolean chooseTarget() {
        if (this.activeTargetSelector == null)
            throw new IllegalStateException(
                    "Cannot choose target while no target selector is active."
            );

        boolean result = this.activeTargetSelector.chooseTarget(this.activeTargetables);
        clearActiveTargetSelector();
        return result;
    }

    /**
     * Adds the given {@link Targetable} to the set of forbidden targetables. The targetable will
     * be excluded from the set of targetables which can potentially be selected by the active
     * {@link TargetSelector} until the set is cleared.
     *
     * @param targetable the targetable to add to the set of forbidden targetables.
     *
     * @throws IllegalArgumentException if the given targetable is null.
     * @throws IllegalStateException if there is an active target selector.
     *
     * @see Targetable
     * @see TargetSelector
     */
    public void addForbiddenTargetable(Targetable targetable) {
        if (targetable == null)
            throw new IllegalArgumentException(
                    "Cannot add null targetable to forbidden targetables."
            );
        if (this.activeTargetSelector != null)
            throw new IllegalStateException(
                    "Cannot add forbidden targetable while a target selector is active."
            );

        this.forbiddenTargetables.add(targetable);
    }

    /**
     * Clears the set of forbidden {@link Targetable}s.
     *
     * @see Targetable
     */
    public void clearForbiddenTargetables() {
        this.forbiddenTargetables.clear();
    }

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
     * Clears the active {@link TargetSelector} and the set of active {@link Targetable}s.
     *
     * @throws IllegalStateException if there is no active target selector.
     *
     * @see TargetSelector
     * @see Targetable
     */
    private void clearActiveTargetSelector() {
        if (this.activeTargetSelector == null)
            throw new IllegalStateException(
                    "Cannot reset target selector while no target selector is active."
            );

        clearActiveTargetables();
        this.activeTargetSelector = null;
    }
}
