package exchangemage.effects.targeting.selectors;

import java.util.Set;

import exchangemage.effects.deployers.EffectDeployer;
import exchangemage.effects.targeting.Targetable;

/**
 * A {@link TargetSelector} which can only acquire targets passively, i.e. it cannot select targets
 * by itself. Instead, it relies on other effects to set its target through the {@link #setTarget}
 * method.
 * <br><br>
 * This target selector is used by effects wrapped by {@link EffectDeployer}s which set targets
 * of the effects they deploy.
 *
 * @param <T> the type of target accepted by this selector
 * @see TargetSelector
 * @see EffectDeployer
 */
public class PassiveTargetSelector<T extends Targetable> extends TargetSelector<T>{
    /**
     * @param targetClass the class of {@link Targetable} objects selected by this selector.
     * @throws NullPointerException if the given target class is <code>null</code>.
     */
    public PassiveTargetSelector(Class<T> targetClass) {
        super(targetClass);
    }

    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        throw new UnsupportedOperationException("Passive target selectors cannot select targets.");
    }

    @Override
    protected void validateTarget(T target) {
        if (!this.targetClass.isInstance(target))
            throw new IllegalArgumentException("Target must be of class " +
                                               this.targetClass.getName() + ".");
    }
}
