package exchangemage.effects.targeting.selectors;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.effects.triggers.conditions.SubjectComparator;

public class VariableTargetSelector<T extends Targetable> extends TargetSelector<T> {
    /**
     * An enum used to specify the mode of target selection used by {@link VariableTargetSelector}s.
     * Depending on the mode, the selector will either wait for the player to select a target or
     * select a random target. Targets are chosen from the set of active targetables provided by
     * the {@link #getActiveTargetables} method.
     *
     * @see VariableTargetSelector
     * @see Targetable
     * @see TargetingManager
     */
    public enum TargetingMode {
        /**
         * This selection mode relies on player input. The selector will wait for the player to
         * choose a target from the set of active targetables unless the set is empty.
         *
         * @see VariableTargetSelector
         * @see Targetable
         * @see TargetingManager
         */
        SELECT {
            /**
             * Waits for the player to select a target from the set of active targetables. If the
             * set is empty, returns <code>false</code> without waiting.
             *
             * @param activeTargetables the set of active {@link Targetable}s to choose the target
             *                          from
             * @param selector          the {@link VariableTargetSelector} to set the target for
             * @return <code>true</code> if the target selection was successful, <code>false</code>
             *         otherwise
             * @param <T> the type of the {@link Targetable} objects selected by the target selector
             * @throws RuntimeException if the target selection process ended without a target being
             *                          selected
             * @see VariableTargetSelector
             * @see TargetingManager
             */
            @Override
            public <T extends Targetable> boolean selectTarget(
                    Set<T> activeTargetables,
                    VariableTargetSelector<T> selector
            ) {
                if (activeTargetables.isEmpty())
                    return false;

                GameState.getTargetingManager().waitForTarget();

                if (!selector.hasTarget())
                    throw new RuntimeException("Wait for target ended without a target being" +
                                               " selected.");

                return true;
            }
        },
        RANDOM {
            /**
             * Selects a random target from the set of active targetables. If the set is empty,
             * returns <code>false</code>.
             *
             * @param activeTargetables the set of active {@link Targetable}s to choose the target
             *                          from
             * @param selector          the {@link VariableTargetSelector} to set the target for
             * @return <code>true</code> if the target selection was successful, <code>false</code>
             *         otherwise
             * @param <T> the type of the {@link Targetable} objects selected by the target selector
             * @throws RuntimeException if the target selection process ended without a target being
             *                          selected
             * @see VariableTargetSelector
             */
            @Override
            public <T extends Targetable> boolean selectTarget(
                    Set<T> activeTargetables,
                    VariableTargetSelector<T> selector
            ) {
                if (activeTargetables.isEmpty())
                    return false;

                int randomIndex = new Random().nextInt(activeTargetables.size());
                selector.setTarget(activeTargetables.stream().toList().get(randomIndex));

                if (!selector.hasTarget())
                    throw new RuntimeException("Random target selection ended without a target" +
                                               " being selected.");

                return true;
            }
        };

        /**
         * Selects a target from the given set of active targetables and sets it for the given
         * {@link VariableTargetSelector}. The logic of the selection process depends on the
         * implementation of this method by the concrete targeting mode.
         *
         * @param activeTargetables the set of active {@link Targetable}s to choose the target from
         * @param selector          the {@link VariableTargetSelector} to set the target for
         * @param <T>               the type of the {@link Targetable} objects selected by the target selector
         * @return <code>true</code> if the target selection was successful, <code>false</code>
         * otherwise
         * @see VariableTargetSelector
         * @see Targetable
         */
        public abstract <T extends Targetable> boolean selectTarget(
                Set<T> activeTargetables,
                VariableTargetSelector<T> selector
        );
    }

    /**
     * A {@link SubjectComparator} used to filter the set of active targetables. Only
     * {@link Targetable}s which match the requirements of the comparator will be considered for
     * selection.
     */
    private final SubjectComparator<T> targetFilter;

    /**
     * The {@link TargetingMode} used by this {@link VariableTargetSelector} to select a target.
     */
    private final TargetingMode targetingMode;

    /**
     * Creates a new {@link VariableTargetSelector} with given target class, filter and targeting
     * mode.
     *
     * @param targetClass   the class of the {@link Targetable} objects selected by this selector
     * @param targetFilter  the {@link SubjectComparator} used to filter the set of active
     *                      targetables
     * @param targetingMode the {@link TargetingMode} used by this {@link VariableTargetSelector}
     *                      to select a target
     * @throws NullPointerException if the given target class or targeting mode is <code>null</code>
     * @see TargetingMode
     * @see SubjectComparator
     */
    public VariableTargetSelector(Class<T> targetClass,
                                  SubjectComparator<T> targetFilter,
                                  TargetingMode targetingMode) {
        super(targetClass);
        Objects.requireNonNull(targetingMode, "Targeting mode cannot be null.");
        this.targetFilter = targetFilter;
        this.targetingMode = targetingMode;
    }

    /**
     * Returns a set of {@link Targetable}s from which a valid target can be selected. The set is
     * filtered by the {@link #targetFilter} if the selector has one (and against the set of
     * forbidden targets).
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     *                         (provided by the {@link TargetingManager})
     * @return a set of targetables from which a valid target can be selected
     * @throws NullPointerException if the given set of forbidden targets is <code>null</code>
     * @see Targetable
     * @see TargetingManager
     */
    public Set<T> getActiveTargetables(Set<Targetable> forbiddenTargets) {
        Objects.requireNonNull(forbiddenTargets, "Forbidden targets set cannot be null.");
        Stream<T> activeTargetables = GameState.getScene().getTargetables().stream()
                                               .filter(targetClass::isInstance)
                                               .filter(target -> !forbiddenTargets.contains(target))
                                               .map(targetClass::cast);

        if (targetFilter != null)
            return activeTargetables.filter(targetFilter::compare).collect(Collectors.toSet());
        return activeTargetables.collect(Collectors.toSet());
    }

    /**
     * Selects a target from the set of active targetables provided by the
     * {@link #getActiveTargetables} method using this selector's {@link #targetingMode}.
     * <br><br>
     * This method is called by the {@link TargetingManager} during the evaluation of this
     * selector's {@link Effect}.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if the target selection was successful, <code>false</code>
     *         otherwise
     * @throws NullPointerException if the given set of forbidden targets is <code>null</code>
     * @see TargetingManager
     * @see TargetingMode
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        Objects.requireNonNull(forbiddenTargets, "Forbidden targets set cannot be null.");
        return this.targetingMode.selectTarget(getActiveTargetables(forbiddenTargets),
                                               this);
    }

    /**
     * Validates the given {@link Targetable} object by checking if it matches the requirements of
     * the {@link #targetFilter}.
     *
     * @param target the target to validate.
     * @throws InvalidTargetException if the target does not match the filter's requirements
     * @see SubjectComparator
     */
    @Override
    protected void validateTarget(T target) {
        if (this.targetFilter != null && !this.targetFilter.compare(target))
            throw new InvalidTargetException("Target does not match the target filter " +
                                             "requirements.");
    }
}
