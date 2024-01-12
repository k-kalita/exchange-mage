package exchangemage.effects;

import exchangemage.actors.Enemy;
import exchangemage.cards.Deck;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.targeting.selectors.VariableTargetSelector;
import exchangemage.effects.targeting.selectors.VariableTargetSelector.TargetingMode;

import java.util.Set;

public enum TestEffects {
    DEAL_1_DAMAGE_TO_RANDOM_ENEMY {
        @Override
        public Effect<?> getEffect() {
            var targetSelector = new VariableTargetSelector<>(Enemy.class, TargetingMode.RANDOM);
            return new DamageEffect<>(1, targetSelector, Effect.ResolutionMode.ENQUEUE);
        }

    },
    ;

    public abstract Effect<?> getEffect();
}