package exchangemage.actors;

import java.util.Set;

import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.targeting.Targetable;

public interface Actor extends Targetable, PersistentEffectsHolder {
    Set<Targetable> getTargetables();

    void takeTurn();

    void takeDamage(int damage);

    void heal(int healing);
}
