package exchangemage.effects.triggers.conditions.comparators;

import java.util.Objects;

import exchangemage.effects.triggers.conditions.ComparisonCondition;

/**
 * A {@link SubjectComparator} used to compare the value of a numeric subject to a given value using
 * a given {@link Operator}.
 *
 * @param <T> the type of the subject being compared. Must be a subclass of {@link Number}.
 * @see SubjectComparator
 * @see ComparisonCondition
 */
public class NumericValueComparator<T extends Number> implements SubjectComparator<T> {
    /**
     * An enum of operators used to compare a subject's value to a target value.
     */
    public enum Operator {
        /**
         * Equal to operator.
         */
        EQ {
            /**
             * Checks if the subject is equal to the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is equal to the target value,
             * <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return Double.compare(subject.doubleValue(), targetValue.doubleValue()) == 0;
            }
        },
        /**
         * Not equal to operator.
         */
        NEQ {
            /**
             * Checks if the subject is not equal to the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is not equal to the target value,
             * <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return !EQ.compare(subject, targetValue);
            }
        },
        /**
         * Less than operator.
         */
        LT {
            /**
             * Checks if the subject is less than the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is less than the target value,
             * <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return Double.compare(subject.doubleValue(), targetValue.doubleValue()) < 0;
            }
        },
        /**
         * Less than or equal to operator.
         */
        LTE {
            /**
             * Checks if the subject is less than or equal to the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is less than or equal to the target value,
             * <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return Double.compare(subject.doubleValue(), targetValue.doubleValue()) <= 0;
            }
        },
        /**
         * Greater than operator.
         */
        GT {
            /**
             * Checks if the subject is greater than the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is greater than the target value,
             * <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return Double.compare(subject.doubleValue(), targetValue.doubleValue()) > 0;
            }
        },
        /**
         * Greater than or equal to operator.
         */
        GTE {
            /**
             * Checks if the subject is greater than or equal to the target value.
             *
             * @param subject the subject to be compared
             * @param targetValue the target value to compare the subject to
             * @return <code>true</code> if the subject is greater than or equal to the target
             * value, <code>false</code> otherwise.
             */
            @Override
            public boolean compare(Number subject, Number targetValue) {
                return Double.compare(subject.doubleValue(), targetValue.doubleValue()) >= 0;
            }
        };

        /**
         * Compares the subject to the target value using this operator.
         *
         * @param subject the subject to be compared
         * @param targetValue the target value to compare the subject to
         * @return the result of the comparison
         */
        public abstract boolean compare(Number subject, Number targetValue);
    }

    /**
     * The value the subject is compared to.
     */
    private final T targetValue;

    /**
     * The operator used to compare the subject to the target value.
     */
    private final Operator operator;

    /**
     * Creates a new {@link NumericValueComparator} with the given target value and operator.
     *
     * @param targetValue the value the subject is compared to
     * @param operator the operator used to compare the subject to the target value
     */
    public NumericValueComparator(T targetValue, Operator operator) {
        Objects.requireNonNull(targetValue,
                               "Target value of NumericValueComparator cannot be null.");
        Objects.requireNonNull(operator,
                               "Operator of NumericValueComparator cannot be null.");
        this.targetValue = targetValue;
        this.operator = operator;
    }

    /**
     * Compares the subject to the target value using the operator.
     *
     * @param subject the subject to be compared
     * @return <code>true</code> if the statement represented by the comparison is fulfilled,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean compare(T subject) {
        if (subject == null)
            return false;
        return operator.compare(subject, targetValue);
    }
}
