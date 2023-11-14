package exchangemage.encounters;

import exchangemage.effects.base.EffectPlayer;
import exchangemage.effects.base.PersistentEffect;
import exchangemage.effects.targeting.Targetable;

import java.util.Arrays;
import java.util.stream.Stream;

public class Scene {
    public PersistentEffect[] getEffects() {
        return null;
    }

    public EffectPlayer getEffectPlayer() {
        return null;
    }

    public Targetable[] getTargetables() {
        return new Targetable[0];
    }

    public Targetable[] getTargetables(Class<?> targetClass, boolean includeTargeted) {
        Stream<Targetable> result = Arrays.stream(getTargetables()).filter(targetClass::isInstance);

        return includeTargeted ?
                result.toArray(Targetable[]::new) :
                result.filter(targetable -> !targetable.isSelected()).toArray(Targetable[]::new);
    }
}
