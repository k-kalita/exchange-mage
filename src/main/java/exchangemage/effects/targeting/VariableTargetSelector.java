package exchangemage.effects.targeting;

import exchangemage.base.Game;

public class VariableTargetSelector<T extends Targetable> extends TargetSelector {
    private final Class<T> targetClass;

    private final TargetingMode targetingMode;

    private final boolean reselectAllowed;

    public enum TargetingMode {
        SELECT,
        RANDOM
    }

    public VariableTargetSelector(Class<T> targetClass,
                                  TargetingMode targetingMode,
                                  boolean reselectAllowed) {
        this.targetClass = targetClass;
        this.targetingMode = targetingMode;
        this.reselectAllowed = reselectAllowed;
    }

    @Override
    public boolean chooseTarget() {
        Game.getGame().getScene().getTargetables(targetClass, reselectAllowed);
        return false;
    }
}
