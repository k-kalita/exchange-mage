package exchangemage.effects.triggers;

import exchangemage.effects.deployers.PersistentEffect;

/**
 * A {@link Trigger} which always returns a given value independent of the state of the game.
 * <br><br>
 * Used for utility purposes - e.g. as resolution trigger for {@link PersistentEffect}s which
 * should always be resolved if they have been activated.
 *
 * @see Trigger
 */
public class ConstValueTrigger implements Trigger {
    private final boolean value;

    /**
     * Constructs a {@link ConstValueTrigger} with given value.
     *
     * @param value value of the trigger
     */
    public ConstValueTrigger(boolean value) { this.value = value; }

    /**
     * Returns the value of the {@link ConstValueTrigger}
     *
     * @return the value of the trigger
     */
    @Override
    public boolean isActivated() { return value; }
}
