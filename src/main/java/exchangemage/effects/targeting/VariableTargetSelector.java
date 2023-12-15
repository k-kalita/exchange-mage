package exchangemage.effects.targeting;

import exchangemage.base.Game;

import java.util.Set;

public class VariableTargetSelector<T extends Targetable> extends TargetSelector<T> {
    private final Class<T> targetClass;

    private final TargetingMode targetingMode;

    @Override
    public Set<T> getActiveTargetables() {
        return null;
    }

    @Override
    public boolean selectTarget(Set<Targetable> activeTargetables) {
        return false;
    }

    @Override
    protected void validateTarget(Targetable target) {

    }

    public enum TargetingMode {
        SELECT,
        RANDOM
    }

    public VariableTargetSelector(Class<T> targetClass, TargetingMode targetingMode) {
        super(targetClass);
        this.targetClass = targetClass;
        this.targetingMode = targetingMode;
    }
}
