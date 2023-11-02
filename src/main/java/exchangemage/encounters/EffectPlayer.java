package exchangemage.encounters;

import java.util.Queue;
import java.util.LinkedList;

import exchangemage.effects.base.Effect;

public class EffectPlayer {
    private Effect currentEffect;
    private final Queue<Effect> effectQueue;

    public EffectPlayer() {
        this.effectQueue = new LinkedList<Effect>();
        this.currentEffect = null;
    }

    public Effect getCurrentEffect() {
        return this.currentEffect;
    }

    public EffectPlayer enqueueEffect(Effect effect) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to enqueue cannot be null.");

        if (effect.canTrigger())
            this.effectQueue.add(effect);

        return this;
    }

    public EffectPlayer playQueue() {
        while (!this.effectQueue.isEmpty()) {
            Effect effect = this.effectQueue.poll();

            if (!effect.isTriggered())
                continue;

            this.currentEffect = effect;
        }

        return this;
    }

    public EffectPlayer playEffect() {
        if (this.currentEffect == null)
            throw new IllegalStateException("No effect to play.");

        return this;
    }
}
