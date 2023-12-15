package exchangemage.effects.triggers.conditions;

import exchangemage.effects.targeting.Targetable;
import exchangemage.base.GameState;

/**
 * A {@link SubclassGetter} which returns the current target as the subject for its
 * {@link Condition} (provided that the current target is an instance of the specified
 * {@link Targetable} subclass).
 *
 * @param <S> subclass of Targetable which can be returned by this getter
 * @see SubclassGetter
 * @see SubjectGetter
 * @see Targetable
 */
public class CurrentTargetGetter<S extends Targetable> extends SubclassGetter<Targetable, S> {
    /**
     * Creates a new {@link CurrentTargetGetter} with given {@link Targetable} subclass.
     *
     * @param targetSubclass the class of {@link Targetable} objects returned by this
     * {@link SubjectGetter}.
     * @throws NullPointerException if the given target class is <code>null</code>.
     * @see Targetable
     */
    public CurrentTargetGetter(Class<S> targetSubclass) {
        super(GameState::getCurrentTarget, targetSubclass);
    }
}
