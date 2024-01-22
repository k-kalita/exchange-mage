package exchangemage.effects.triggers;

import java.util.List;

import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.triggers.conditions.InstanceCondition;
import exchangemage.effects.triggers.getters.EffectInEvaluationGetter;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.getters.EffectSourceGetter;
import exchangemage.scenes.TurnPlayer;

/**
 * A {@link Trigger} activated when the {@link Effect} currently in resolution represents the start
 * or end (depending on the constructor argument) of the turn of the {@link PersistentEffectsHolder}
 * to which the {@link PersistentEffect} being evaluated is assigned.
 *
 * @see PersistentEffect
 * @see PersistentEffectsHolder
 * @see TurnPlayer
 */
public class HoldersTurnTrigger extends ConditionStatement {
    /**
     * @param turnStart <code>true</code> if the expected notification should signify the start of
     *                  the holder's turn, <code>false</code> if it should signify its end
     */
    public HoldersTurnTrigger(boolean turnStart) {
        super(Operator.AND, List.of(
                getTurnNotificationTrigger(turnStart),
                getHolderComparisonTrigger()
        ));
    }

    /**
     * @param turnStart <code>true</code> if the expected notification should be
     *                  {@link TurnPlayer.TurnPlayerEvent#TURN_STARTED}, <code>false</code> if it
     *                  should be {@link TurnPlayer.TurnPlayerEvent#TURN_ENDED}
     * @return a {@link NotificationTrigger} activated when the {@link NotificationEffect}
     * currently in resolution represents the appropriate {@link TurnPlayer.TurnPlayerEvent}
     */
    private static NotificationTrigger getTurnNotificationTrigger(boolean turnStart) {
        return new NotificationTrigger(turnStart ? TurnPlayer.TurnPlayerEvent.TURN_STARTED
                                                 : TurnPlayer.TurnPlayerEvent.TURN_ENDED);
    }

    /**
     * @return a {@link ConditionalTrigger} which is activated when the {@link Effect} currently in
     * resolution has as its source the same {@link PersistentEffectsHolder} as the effect being
     * evaluated
     */
    private static ConditionalTrigger getHolderComparisonTrigger() {
        var sourceInResolutionGetter = new EffectSourceGetter<>(
                PersistentEffectsHolder.class,
                new EffectInResolutionGetter()
        );
        var sourceInEvaluationGetter = new EffectSourceGetter<>(
                PersistentEffectsHolder.class,
                new EffectInEvaluationGetter()
        );

        return new ConditionalTrigger(sourceInResolutionGetter,
                                      new InstanceCondition(sourceInEvaluationGetter));
    }
}
