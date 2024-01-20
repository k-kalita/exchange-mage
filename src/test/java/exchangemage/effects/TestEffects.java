package exchangemage.effects;

import exchangemage.actors.Enemy;
import exchangemage.effects.targeting.selectors.ConstantTargetSelector;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.getters.EffectSourceGetter;
import exchangemage.effects.triggers.getters.EffectTargetGetter;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.targeting.selectors.VariableTargetSelector;
import exchangemage.effects.targeting.selectors.VariableTargetSelector.TargetingMode;

public enum TestEffects {
    DEAL_1_DAMAGE_TO_RANDOM_ENEMY {
        @Override
        public Effect<?> get() {
            var targetSelector = new VariableTargetSelector<>(Enemy.class, TargetingMode.RANDOM);
            return new DamageEffect<>("Deal 1 damage to a random enemy",
                                      1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }

    },
    DEAL_1_DAMAGE_TO_CURRENTLY_TARGETED_ENEMY {
        @Override
        public Effect<?> get() {
            var targetSelector = new ConstantTargetSelector<>(
                    new EffectTargetGetter<>(Enemy.class, new EffectInResolutionGetter()),
                    Enemy.class
            );
            return new DamageEffect<>("Deal 1 damage to the currently targeted enemy",
                                      1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }
    },
    DEAL_1_DAMAGE_TO_SOURCE_ENEMY {
        @Override
        public Effect<?> get() {
            var targetSelector = new ConstantTargetSelector<>(
                    new EffectSourceGetter<>(Enemy.class, new EffectInResolutionGetter()),
                    Enemy.class
            );
            return new DamageEffect<>("Deal 1 damage to the source enemy",
                                      1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }
    };

    public abstract Effect<?> get();
}