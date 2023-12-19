package exchangemage.effects.triggers.conditions;

import exchangemage.effects.targeting.Targetable;
import exchangemage.base.GameState;

/**
 * A {@link SubclassGetter} which returns the target of the effect currently in resolution (provided
 * that the current target is an instance of the specified {@link Targetable} subclass).
 *
 * @param <S> subclass of Targetable which can be returned by this getter
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Targetable
 */
public class TargetInResolutionGetter<S extends Targetable> extends SubclassGetter<Targetable, S> {
    /**
     * Creates a new {@link TargetInResolutionGetter} with given {@link Targetable} subclass.
     *
     * @param targetSubclass the class of {@link Targetable} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given target class is <code>null</code>.
     * @see Targetable
     */
    public TargetInResolutionGetter(Class<S> targetSubclass) {
        super(() -> GameState.getEffectInResolution().getTarget(), targetSubclass);
    }
}
