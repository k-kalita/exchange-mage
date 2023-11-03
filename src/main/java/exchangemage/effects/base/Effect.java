package exchangemage.effects.base;

import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.TargetSelector;

/**
 * Base class for all effects in the game. Effects represent all atomic actions that affect scenes,
 * actors and their decks, as well as alerts used to trigger {@link PersistentEffect}s.
 * <br><br>
 * All effects possess a {@link Trigger} component used to determine whether the effect should be
 * activated and a {@link TargetSelector} component used to determine the target of the effect.
 * <br><br>
 * The most important types of effects (which all other effects extend) are:
 * <ul>
 *     <li>{@link ValueEffect} - used to apply changes which can be defined with a single numeric
 *     value (e.g. dealing damage, healing).</li>
 *     <li>{@link PersistentEffect} - used to represent persistent environmental/actor-specific
 *     effects, storing within them effects which can be activated by external occurrences (e.g.
 *     effects which activate each time an enemy takes damage or when a specific type of card is
 *     played).</li>
 *     <li>{@link AlertEffect}</li>
 *     <li>{@link EffectDeployer}</li>
 * </ul>
 *
 * @see PersistentEffect
 * @see Trigger
 * @see TargetSelector
 */
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
