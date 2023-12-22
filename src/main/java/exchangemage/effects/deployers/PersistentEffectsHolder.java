package exchangemage.effects.deployers;

import exchangemage.actors.Actor;
import exchangemage.scenes.Scene;

import java.util.Set;

/**
 * An interface for all classes which can have {@link PersistentEffect}s assigned to them (e.g.
 * {@link Actor}s and {@link Scene}s).
 *
 * @see PersistentEffect
 */
public interface PersistentEffectsHolder {
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
