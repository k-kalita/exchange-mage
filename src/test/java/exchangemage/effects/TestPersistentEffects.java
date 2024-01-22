package exchangemage.effects;

import java.util.List;

import exchangemage.actors.Enemy;
import exchangemage.effects.EffectPlayer.EffectResolutionStage;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.ConditionStatement;
import exchangemage.effects.triggers.ConditionStatement.Operator;
import exchangemage.effects.triggers.EffectTypeTrigger;
import exchangemage.effects.triggers.conditions.NumericValueCondition;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.NonNullCondition;
import exchangemage.effects.triggers.getters.EffectTargetGetter;
import exchangemage.effects.triggers.getters.EffectValueGetter;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.ValueEffect;

public enum TestPersistentEffects {
    WHENEVER_ENEMY_IS_DAMAGED_DEAL_THEM_1_DAMAGE {
        @Override
        public PersistentEffect get() {
            var effectTypeTrigger = new EffectTypeTrigger(
                    new EffectInResolutionGetter(),
                    DamageEffect.class
            );
            var effectTargetCondition = new ConditionalTrigger(
                    new EffectTargetGetter<>(Enemy.class, new EffectInResolutionGetter()),
                    new NonNullCondition()
            );
            var effectValueCondition = new ConditionalTrigger(
                    new EffectValueGetter(ValueEffect.ValueState.MODIFIED),
                    new NumericValueCondition(0,
                                              NumericValueCondition.Operator.GT
                    )
            );
            var conditionStatement = new ConditionStatement(Operator.AND, List.of(
                    effectTypeTrigger, effectTargetCondition, effectValueCondition
            ));

            return new PersistentEffect(
                    "Whenever an enemy is damaged, deal them 1 damage",
                    List.of(TestEffects.DEAL_1_DAMAGE_TO_CURRENTLY_TARGETED_ENEMY.get()),
                    EffectResolutionStage.RESPONSE,
                    conditionStatement
            );
        }
    },
    ;

    public abstract PersistentEffect get();
}