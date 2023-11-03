package exchangemage.effects.base;

import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.TargetSelector;

public abstract class Effect {
    private EffectSource source;
    private Trigger trigger;
    private TargetSelector targetSelector;

    public boolean canActivate() {
        return this.trigger.canActivate();
    }

    public boolean isActivated() {
        return this.trigger.isActivated();
    }

    public abstract void execute();

    public EffectSource getSource() {
        return this.source;
    }

    public void chooseTarget() {
        this.targetSelector.chooseTarget();
    }

    public Effect setSource(EffectSource source) {
        this.source = source;
        return this;
    }

    public Trigger getTrigger() {
        return this.trigger;
    }

    public Effect setTrigger(Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    public TargetSelector getTargetSelector() {
        return this.targetSelector;
    }

    public Effect setTargetSelector(TargetSelector targetSelector) {
        this.targetSelector = targetSelector;
        return this;
    }
}
