package exchangemage.effects;

import java.util.List;

import exchangemage.actors.Enemy;
import exchangemage.effects.EffectPlayer.EffectResolutionStage;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.conditions.ComparisonCondition;
import exchangemage.effects.triggers.conditions.ConditionStatement;
import exchangemage.effects.triggers.conditions.ConditionStatement.Operator;
import exchangemage.effects.triggers.conditions.EffectTypeCondition;
import exchangemage.effects.triggers.conditions.comparators.NumericValueComparator;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.comparators.NonNullComparator;
import exchangemage.effects.triggers.conditions.getters.EffectTargetGetter;
import exchangemage.effects.triggers.conditions.getters.EffectValueGetter;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.ValueEffect;

public enum TestPersistentEffects {
    WHENEVER_ENEMY_IS_DAMAGED_DEAL_THEM_1_DAMAGE {
        @Override
        public PersistentEffect get() {
            var effectTypeCondition = new EffectTypeCondition(
                    new EffectInResolutionGetter(),
                    DamageEffect.class
            );
            var effectTargetCondition = new ComparisonCondition<>(
                    new EffectTargetGetter<>(Enemy.class, new EffectInResolutionGetter()),
                    new NonNullComparator<>()
            );
            var effectValueCondition = new ComparisonCondition<>(
                    new EffectValueGetter(ValueEffect.ValueState.MODIFIED),
                    new NumericValueComparator<>(0,
                                                 NumericValueComparator.Operator.GT
                    )
            );
            var conditionStatement = new ConditionStatement(Operator.AND, List.of(
                    effectTypeCondition, effectTargetCondition, effectValueCondition
            ));

            return new PersistentEffect(
                    "Whenever an enemy is damaged, deal them 1 damage",
                    List.of(TestEffects.DEAL_1_DAMAGE_TO_CURRENTLY_TARGETED_ENEMY.get()),
                    EffectResolutionStage.RESPONSE,
                    new ConditionalTrigger(conditionStatement)
            );
        }
    },
    ;

    public abstract PersistentEffect get();
}