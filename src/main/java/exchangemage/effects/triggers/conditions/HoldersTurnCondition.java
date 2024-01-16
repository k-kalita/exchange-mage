package exchangemage.effects.triggers.conditions;

import java.util.List;

import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.triggers.conditions.comparators.InstanceComparator;
import exchangemage.effects.triggers.conditions.getters.EffectInEvaluationGetter;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.getters.EffectSourceGetter;
import exchangemage.scenes.TurnPlayer;

/**
 * A {@link Condition} fulfilled when the {@link Effect} currently in resolution represents start or
 * end (depending on the constructor argument) of the turn of the {@link PersistentEffectsHolder} to
 * which the {@link PersistentEffect} being evaluated is assigned.
 *
 * @see PersistentEffect
 * @see PersistentEffectsHolder
 * @see TurnPlayer
 */
public class HoldersTurnCondition extends ConditionStatement {
    /**
     * @param turnStart <code>true</code> if the expected notification should signify the start of
     *                  the holder's turn, <code>false</code> if it should signify its end
     */
    public HoldersTurnCondition(boolean turnStart) {
        super(Operator.AND, List.of(
                getTurnNotificationCondition(turnStart),
                getHolderComparisonCondition()
        ));
    }

    /**
     * @param turnStart <code>true</code> if the expected notification should be
     *                  {@link TurnPlayer.TurnPlayerEvent#TURN_STARTED}, <code>false</code> if it
     *                  should be {@link TurnPlayer.TurnPlayerEvent#TURN_ENDED}
     * @return a {@link NotificationCondition} fulfilled when the {@link NotificationEffect}
     * currently in resolution represents the appropriate {@link TurnPlayer.TurnPlayerEvent}
     */
    private static NotificationCondition getTurnNotificationCondition(boolean turnStart) {
        return new NotificationCondition(turnStart ? TurnPlayer.TurnPlayerEvent.TURN_STARTED
                                                   : TurnPlayer.TurnPlayerEvent.TURN_ENDED);
    }

    /**
     * @return a {@link ComparisonCondition} which is fulfilled when the {@link Effect} currently in
     * resolution has as its source the same {@link PersistentEffectsHolder} as the effect being
     * evaluated
     */
    private static ComparisonCondition<?> getHolderComparisonCondition() {
        var sourceInResolutionGetter = new EffectSourceGetter<>(
                PersistentEffectsHolder.class,
                new EffectInResolutionGetter()
        );
        var sourceInEvaluationGetter = new EffectSourceGetter<>(
                PersistentEffectsHolder.class,
                new EffectInEvaluationGetter()
        );

        return new ComparisonCondition<>(sourceInResolutionGetter,
                                         new InstanceComparator<>(sourceInEvaluationGetter));
    }
}
