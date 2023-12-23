package exchangemage.effects.targeting;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.deployers.EffectDeployer;
import exchangemage.effects.targeting.selectors.TargetSelector;

/**
 * An auxiliary class used by the {@link EffectPlayer} to handle the selection of targets for
 * {@link Effect}s during their evaluation (as well as by some of the {@link EffectDeployer}s to
 * select targets for their underlying effects).
 * <br><br>
 * TargetingManager is responsible for:
 * <ul>
 *     <li>
 *         Keeping track of the {@link Targetable}s which cannot be selected as targets for any
 *         effects currently being evaluated (e.g. because they have already been selected by
 *         another constituent of a deployer effect which forbids reselection).
 *     </li>
 *     <li>
 *         Providing TargetSelectors which require player input with appropriate waiting and
 *         notification methods.
 *     </li>
 * </ul>
 *
 * @see TargetSelector
 * @see EffectPlayer
 * @see Targetable
 */
public class TargetingManager {
    /**
     * The set of {@link Targetable}s which cannot be selected as targets for any {@link Effect}s
     * currently being evaluated.
     */
    private final Set<Targetable> forbiddenTargets = new HashSet<>();

    /**
     * The lock used to synchronize the {@link #waitForTarget} and {@link #chooseTarget} methods.
     */
    private final Object lock = new Object();

    /**
     * Selects a target for the given {@link Effect}. If the effect already has a target, this
     * method checks whether the target is forbidden and returns a boolean value accordingly.
     *
     * @param effect the effect to select a target for
     * @return <code>true</code> if the effect already has a non-forbidden target or is able to
     * select one, <code>false</code> otherwise
     * @throws NullPointerException if the effect is <code>null</code>
     * @see Effect
     * @see Targetable
     * @see TargetSelector
     */
    public boolean selectTarget(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect cannot be null.");

        if (effect.hasTarget())
            return !forbiddenTargets.contains(effect.getTarget());

        return effect.selectTarget(forbiddenTargets);
    }

    /**
     * Adds the given {@link Targetable} to the set of {@link #forbiddenTargets}.
     *
     * @param targetable the targetable to add to the set of forbidden targets
     * @throws NullPointerException if the targetable is <code>null</code>
     * @see Targetable
     */
    public void addForbiddenTargetable(Targetable targetable) {
        Objects.requireNonNull(targetable, "Forbidden targetable cannot be null.");
        this.forbiddenTargets.add(targetable);
    }

    /**
     * Clears the set of {@link #forbiddenTargets}.
     *
     * @see Targetable
     */
    public void clearForbiddenTargetables() {this.forbiddenTargets.clear();}

    /**
     * Called by the {@link TargetSelector} of the currently evaluated {@link Effect} to wait for
     * the player to choose a target.
     *
     * @see Targetable
     * @see TargetSelector
     */
    public void waitForTarget() {
        synchronized (this.lock) {
            while (!GameState.getEffectInEvaluation().hasTarget()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Called when the player chooses a target for the currently evaluated {@link Effect} to notify
     * the {@link #waitForTarget()} method.
     *
     * @param target the target chosen by the player
     * @throws NullPointerException if the target is <code>null</code>
     * @see Targetable
     * @see TargetSelector
     */
    public void chooseTarget(Targetable target) {
        Objects.requireNonNull(target, "Target cannot be null.");
        synchronized (this.lock) {
            GameState.getEffectInEvaluation().getTargetSelector().setTarget(target);
            this.lock.notify();
        }
    }
}
