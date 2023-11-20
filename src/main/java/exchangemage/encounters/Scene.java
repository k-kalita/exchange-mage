package exchangemage.encounters;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import exchangemage.base.Observer;
import exchangemage.base.Observable;
import exchangemage.effects.base.EffectPlayer;
import exchangemage.effects.base.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

public class Scene implements Targetable {
    public List<PersistentEffect> getEffects() {
        return null;
    }

    public static Scene getScene() {
        return null;
    }

    public EffectPlayer getEffectPlayer() {
        return null;
    }

    public Targetable[] getTargetables() {
        return new Targetable[0];
    }

    public Targetable[] getTargetables(Class<?> targetClass) {
        return Arrays.stream(getTargetables())
                .filter(targetClass::isInstance)
                .toArray(Targetable[]::new);
    }

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
    public Set<Observer> getObservers() {
        return null;
    }
}
