package exchangemage.effects.targeting.selectors;

import java.util.Objects;
import java.util.Set;

import exchangemage.effects.Effect;
import exchangemage.effects.deployers.EffectDeployer;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.TargetingManager;

/**
 * An abstract base class for all objects used to select a target for an {@link Effect}. Its two
 * most important methods are:
 * <ul>
 *     <li>
 *         {@link #selectTarget} - called by the {@link TargetingManager} during an effect's
 *         evaluation to select its target if possible.
 *     </li>
 *     <li>
 *         {@link #setTarget} - called by some {@link EffectDeployer}s (and by the targeting manager
 *         when target selection requires player input) to set the target for an effect, provided
 *         that it passes appropriate validation.
 *     </li>
 * </ul>
 *
 * @param <T> the type of {@link Targetable} objects which can be selected by a given target
 * selector
 * @see Effect
 * @see EffectPlayer
 * @see Targetable
 * @see TargetingManager
 */
public abstract class TargetSelector<T extends Targetable> {
    /**
     * The {@link Targetable} object selected by this {@link TargetSelector} as the target for its
     * {@link Effect}.
     */
    protected T target = null;

    /** The class of {@link Targetable} objects selected by this {@link TargetSelector}. */
    protected final Class<T> targetClass;

    /**
     * @param targetClass the class of {@link Targetable} objects selected by this selector.
     * @throws NullPointerException if the given target class is <code>null</code>.
     */
    public TargetSelector(Class<T> targetClass) {
        Objects.requireNonNull(targetClass, "Target class cannot be null.");
        this.targetClass = targetClass;
    }

    /**
     * An exception thrown when an attempt is made to set an invalid target for a
     * {@link TargetSelector}.
     *
     * @see Targetable
     */
    public static class InvalidTargetException extends IllegalArgumentException {
        public InvalidTargetException(String message) {super(message);}
    }

    /**
     * Chooses a target for this selector's {@link Effect} which is not in the given set of
     * forbidden targets (provided by the {@link TargetingManager}). Return value indicates
     * whether it was possible to select a valid target.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise
     * @see Targetable
     * @see TargetingManager
     */
    public abstract boolean selectTarget(Set<Targetable> forbiddenTargets);

    /**
     * Validates given {@link Targetable} object as a target for this {@link TargetSelector}.
     * Called to ensure validity when the {@link #setTarget} method is used instead of the standard
     * target selection process.
     * <br><br>
     * Concrete implementations of the TargetSelector base class should override this method to
     * provide their own validation logic.
     *
     * @param target the target to validate.
     * @throws InvalidTargetException if the given target is null.
     */
    protected abstract void validateTarget(T target);

    /** @return <code>true</code> if a target has been selected, <code>false</code> otherwise. */
    public boolean hasTarget() {return target != null;}

    /**
     * Sets this {@link TargetSelector}'s target to the given {@link Targetable} object if it
     * is valid (ensured by the {@link #validateTarget} method).
     *
     * @param target the target to set.
     * @see Targetable
     */
    public void setTarget(Targetable target) {
        if (target == null)
            throw new InvalidTargetException("Target cannot be null.");
        if (!targetClass.isInstance(target))
            throw new InvalidTargetException(
                    "Target must be of type " + targetClass.getName() + "."
            );
        validateTarget(targetClass.cast(target));
        this.target = targetClass.cast(target);
    }

    /**
     * @return the target selected by this {@link TargetSelector}.
     * @throws IllegalStateException if no target has been selected.
     * @see Targetable
     */
    public T getTarget() {
        if (!this.hasTarget())
            throw new IllegalStateException("No target has been selected.");
        return this.target;
    }

    /** Clears this selector's {@link #target}. */
    public void clearTarget() {this.target = null;}
}
