package exchangemage.effects.triggers.getters;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * A {@link SubjectGetter} used as a decorator for another subject getter. It returns the subject
 * only if it is an instance of the specified subclass of the class returned by the decorated
 * getter.
 * <br><br>
 * Subclass getters implement the SubjectGetter interface parameterized with the subclass of the
 * subject class they return to allow the {@link Condition} receiving the subject to access its
 * subclass-specific methods.
 *
 * @param <T> the type of the subject returned by the decorated getter
 * @param <S> subclass of <code>T</code> returned by this getter
 * @see SubjectGetter
 * @see Condition
 * @see ConditionalTrigger
 */
public class SubclassGetter<T, S extends T> implements SubjectGetter<S> {
    /** The superclass getter decorated by this {@link SubclassGetter}. */
    private final SubjectGetter<T> superclassGetter;

    /** The class of subjects returned by this {@link SubclassGetter}. */
    private final Class<S> subclass;

    /**
     * @param superclassGetter the {@link SubjectGetter} used to retrieve the subject to be
     *                         evaluated and potentially returned by this getter.
     * @param subclass         the class of subjects returned by this getter.
     * @throws NullPointerException if the given superclass getter or subclass is <code>null</code>.
     */
    public SubclassGetter(SubjectGetter<T> superclassGetter, Class<S> subclass) {
        Objects.requireNonNull(superclassGetter,
                               "Superclass getter of subclass getter cannot be null.");
        Objects.requireNonNull(subclass, "Subclass of subclass getter cannot be null.");
        this.superclassGetter = superclassGetter;
        this.subclass = subclass;
    }

    /**
     * @return the subject retrieved by the {@link #superclassGetter} cast to the subclass if it is
     * an instance of it, otherwise <code>null</code>.
     */
    @Override
    public S getSubject() {
        T subject = superclassGetter.getSubject();
        if (subject == null)
            throw new RuntimeException("No superclass subject could be retrieved.");
        if (subclass.isInstance(subject))
            return subclass.cast(subject);
        else
            return null;
    }
}
