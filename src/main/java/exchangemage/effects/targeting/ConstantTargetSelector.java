package exchangemage.effects.targeting;

import exchangemage.effects.base.Effect;

/**
 * A {@link TargetSelector} which always selects the same target for its {@link Effect}
 * (e.g. always selecting the player as the target of a healing effect).
 *
 * @see TargetSelector
 * @see Effect
 * @see Targetable
 */
public class ConstantTargetSelector extends TargetSelector {
    private final TargetGetter targetGetter;

    /**
     * Creates a new {@link ConstantTargetSelector} with the given {@link TargetGetter}.
     *
     * @param targetGetter implementation of {@link TargetGetter} functional interface used to
     *                     return this {@link TargetSelector}'s target.
     *
     * @see TargetGetter
     * @see TargetSelector
     * @see Targetable
     */
    public ConstantTargetSelector(TargetGetter targetGetter) {
        if (targetGetter == null)
            throw new IllegalArgumentException(
                    "Target getter of constant target selector cannot be null."
            );
        this.targetGetter = targetGetter;
    }

    /**
     * Chooses a target by calling the {@link TargetGetter}'s {@link TargetGetter#getTarget()}
     * method.
     *
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    public void chooseTarget() {
        this.setTarget(this.targetGetter.getTarget());
    }
}
