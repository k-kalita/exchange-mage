package exchangemage.effects.deployers;

import java.util.Set;

import exchangemage.actors.Actor;
import exchangemage.scenes.Scene;
import exchangemage.effects.EffectSource;

/**
 * An interface for all classes which can have {@link PersistentEffect}s assigned to them (e.g.
 * {@link Actor}s and {@link Scene}s).
 * <br><br>
 * Since {@link PersistentEffect}s should have their source set to the persistent effect holder
 * which they are assigned to, this interface extends {@link EffectSource}.
 *
 * @see PersistentEffect
 */
public interface PersistentEffectsHolder extends EffectSource {
    /**
     * Adds a {@link PersistentEffect} to this {@link PersistentEffectsHolder}.
     *
     * @param effect the persistent effect to add to this persistent effect holder.
     * @see PersistentEffect
     */
    void addPersistentEffect(PersistentEffect effect);

    /**
     * Removes a {@link PersistentEffect} from this {@link PersistentEffectsHolder}.
     *
     * @param effect the persistent effect to remove from this persistent effect holder.
     * @see PersistentEffect
     */
    void removePersistentEffect(PersistentEffect effect);

    /**
     * Returns the {@link PersistentEffect}s currently assigned to this
     * {@link PersistentEffectsHolder}.
     *
     * @return the set of persistent effects currently assigned to this persistent effect holder.
     * @see PersistentEffect
     */
    Set<PersistentEffect> getPersistentEffects();
}
