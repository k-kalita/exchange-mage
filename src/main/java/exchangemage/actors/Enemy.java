package exchangemage.actors;

import exchangemage.base.Observer;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

import java.util.Set;

public class Enemy implements Actor {
    @Override
    public Set<Targetable> getTargetables() {
        return null;
    }

    @Override
    public void takeTurn() {

    }

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public Set<Observer> getObservers() {
        return null;
    }

    @Override
    public void addPersistentEffect(PersistentEffect effect) {

    }

    @Override
    public void removePersistentEffect(PersistentEffect effect) {

    }

    @Override
    public Set<PersistentEffect> getPersistentEffects() {
        return null;
    }
}
