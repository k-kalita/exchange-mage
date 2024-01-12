package exchangemage.effects;

import exchangemage.actors.Enemy;
import exchangemage.effects.targeting.selectors.ConstantTargetSelector;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.getters.EffectTargetGetter;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.targeting.selectors.VariableTargetSelector;
import exchangemage.effects.targeting.selectors.VariableTargetSelector.TargetingMode;

public enum TestEffects {
    DEAL_1_DAMAGE_TO_RANDOM_ENEMY {
        @Override
        public Effect<?> getEffect() {
            var targetSelector = new VariableTargetSelector<>(Enemy.class, TargetingMode.RANDOM);
            return new DamageEffect<>(1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }

    },
    DEAL_1_DAMAGE_TO_CURRENTLY_TARGETED_ENEMY {
        @Override
        public Effect<?> getEffect() {
            var targetSelector = new ConstantTargetSelector<>(
                    new EffectTargetGetter<>(Enemy.class, new EffectInResolutionGetter()),
                    Enemy.class
            );
            return new DamageEffect<>(1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }
    }
    ;

    public abstract Effect<?> getEffect();
}