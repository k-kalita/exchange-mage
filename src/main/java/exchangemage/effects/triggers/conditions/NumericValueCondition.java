package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.getters.SubjectGetter;

/**
 * A {@link Condition} used to compare the value of a numeric subject against a target value
 * using the given {@link Operator}.
 *
 * @see ConditionalTrigger
 */
public class NumericValueCondition implements Condition {
    /** An enum of operators used to compare the subject to the target value. */
    public enum Operator {
        /** Equal to operator. */
        EQ {
            /**
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
        /** Not equal to operator. */
        NEQ {
            /**
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
        /** Less than operator. */
        LT {
            /**
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
        /** Less than or equal to operator. */
        LTE {
            /**
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
        /** Greater than operator. */
        GT {
            /**
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
        /** Greater than or equal to operator. */
        GTE {
            /**
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
         * @param subject     the subject to be compared
         * @param targetValue the target value to compare the subject to
         * @return <code>true</code> if the statement represented by the comparison is fulfilled,
         * <code>false</code> otherwise.
         */
        public abstract boolean compare(Number subject, Number targetValue);
    }

    /** The {@link SubjectGetter} used to retrieve the value the subject value is compared to. */
    private final SubjectGetter<Number> targetValueGetter;

    /** The operator used to compare the subject to the target value. */
    private final Operator operator;

    /**
     * @param targetValue the value the subject is compared to
     * @param operator    the {@link Operator} used to compare the subject to the target value
     * @throws NullPointerException if either the target value or the operator is
     *                              <code>null</code>.
     */
    public NumericValueCondition(Number targetValue,
                                 Operator operator) {
        Objects.requireNonNull(targetValue, "Target value cannot be null.");
        Objects.requireNonNull(operator, "Operator cannot be null.");
        this.targetValueGetter = () -> targetValue;
        this.operator          = operator;
    }

    /**
     * @param targetValueGetter the {@link SubjectGetter} used to retrieve the value the subject
     *                          value is compared to
     * @param operator          the {@link Operator} used to compare the subject to the target value
     * @throws NullPointerException if either the target value getter or the operator is
     *                             <code>null</code>.
     */
    public NumericValueCondition(SubjectGetter<Number> targetValueGetter,
                                 Operator operator) {
        Objects.requireNonNull(targetValueGetter, "Target value getter cannot be null.");
        Objects.requireNonNull(operator, "Operator of cannot be null.");
        this.targetValueGetter = targetValueGetter;
        this.operator          = operator;
    }

    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the statement represented by the comparison is fulfilled,
     * <code>false</code> otherwise.
     * @throws SubjectMismatchException if the subject is not a number
     */
    @Override
    public boolean evaluate(Object subject) {
        if (subject == null)
            return false;
        if (!(subject instanceof Number))
            throw new SubjectMismatchException();
        return operator.compare((Number) subject, targetValueGetter.getSubject());
    }
}
