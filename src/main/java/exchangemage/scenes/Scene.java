package exchangemage.scenes;

import java.util.HashSet;
import java.util.Set;

import exchangemage.actors.Actor;
import exchangemage.base.Game;
import exchangemage.base.Observer;
import exchangemage.base.Observable;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.targeting.Targetable;

public class Scene implements Targetable, PersistentEffectsHolder, Observable {
    /**
     * {@link EffectPlayer} used by this {@link Scene} to play {@link Effect}s.
     */
    private final EffectPlayer effectPlayer;

    /**
     * Set of {@link Actor}s present in the {@link Scene}.
     */
    private final Set<Actor> actors = new HashSet<>();

    /**
     * Set of {@link Observer}s of the {@link Scene}.
     */
    private final Set<Observer> observers = new HashSet<>();

    public Scene() {
        this.effectPlayer = new EffectPlayer();
        this.actors.add(Game.getGame().getPlayer());
    }

    /**
     * Returns the {@link EffectPlayer} used by this {@link Scene}.
     *
     * @return the current {@link EffectPlayer}
     * @see EffectPlayer
     */
    public EffectPlayer getEffectPlayer() {return this.effectPlayer;}

    public Set<Targetable> getTargetables() {return null;}

    /**
     * Adds an {@link Observer} to this {@link Observable} object. The observer can then be notified
     * of certain events by calling the {@link Observable#notifyObservers} method.
     *
     * @param observer the observer to add
     * @see Observer
     */
    @Override
    public void addObserver(Observer observer) {

    }

    /**
     * Removes an {@link Observer} from this {@link Observable} object. The observer will no longer
     * be notified of any events.
     *
     * @param observer the observer to remove
     * @see Observer
     */
    @Override
    public void removeObserver(Observer observer) {

    }

    /**
     * Returns a set of all {@link Observer}s of this {@link Observable} object.
     *
     * @return a set of all observers of this object
     * @see Observer
     */
    @Override
    public Set<Observer> getObservers() {return null;}

    /**
     * Adds a {@link PersistentEffect} to this {@link PersistentEffectsHolder}.
     *
     * @param effect the {@link PersistentEffect} to add to this {@link PersistentEffectsHolder}.
     * @see PersistentEffect
     */
    @Override
    public void addPersistentEffect(PersistentEffect effect) {

    }

    /**
     * Removes a {@link PersistentEffect} from this {@link PersistentEffectsHolder}.
     *
     * @param effect the {@link PersistentEffect} to remove from this
     *               {@link PersistentEffectsHolder}.
     * @see PersistentEffect
     */
    @Override
    public void removePersistentEffect(PersistentEffect effect) {

    }

    /**
     * Returns the {@link PersistentEffect}s currently assigned to this
     * {@link PersistentEffectsHolder}.
     *
     * @return the set of {@link PersistentEffect}s currently assigned to this
     * {@link PersistentEffectsHolder}.
     * @see PersistentEffect
     */
    @Override
    public Set<PersistentEffect> getPersistentEffects() {return null;}
}
