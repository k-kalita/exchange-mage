package exchangemage.effects.targeting.selectors;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import exchangemage.base.GameState;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.conditions.SubjectComparator;

public class VariableTargetSelector<T extends Targetable> extends TargetSelector<T> {
    public enum TargetingMode {
        SELECT,
        RANDOM
    }

    private final SubjectComparator<T> targetFilter;

    private final TargetingMode targetingMode;

    public VariableTargetSelector(Class<T> targetClass,
                                  SubjectComparator<T> targetFilter,
                                  TargetingMode targetingMode) {
        super(targetClass);
        Objects.requireNonNull(targetingMode, "Targeting mode cannot be null.");
        this.targetFilter = targetFilter;
        this.targetingMode = targetingMode;
    }

    public Set<T> getActiveTargetables() {
        Stream<T> activeTargetables = GameState.getScene().getTargetables().stream()
                                               .filter(targetClass::isInstance)
                                               .map(targetClass::cast);

        if (targetFilter != null)
            return activeTargetables.filter(targetFilter::compare).collect(Collectors.toSet());
        return activeTargetables.collect(Collectors.toSet());
    }

    @Override
    public boolean selectTarget(Set<Targetable> activeTargetables) {
        Objects.requireNonNull(activeTargetables, "Active targetables cannot be null.");
        return false;
    }

    @Override
    protected void validateTarget(Targetable target) {

    }
}
