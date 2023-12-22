package exchangemage.effects;

import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.triggers.Trigger;

public class ValueEffect extends Effect {
    public ValueEffect(Trigger trigger, TargetSelector targetSelector, ResolutionMode resolutionMode) {
        super(trigger, targetSelector, resolutionMode);
    }

    @Override
    public void execute() {

    }

    public int getValue() {
        return 0;
    }
}
