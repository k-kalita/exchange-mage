package exchangemage.effects.base;

public class PersistentEffect extends Effect {
    private Effect[] effects;
    private EffectPlayer.EffectResolutionStage triggerStage;

    public EffectPlayer.EffectResolutionStage getTriggerStage() {
        return triggerStage;
    }

    @Override
    public void execute() {

    }
}
