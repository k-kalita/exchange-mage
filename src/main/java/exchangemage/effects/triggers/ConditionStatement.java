package exchangemage.effects.triggers;

import java.util.Objects;
import java.util.List;

/**
 * A wrapper {@link Trigger} which represents a logical statement composed of other triggers. Used
 * to combine multiple {@link ConditionalTrigger}s into a single one using logical operators.
 * More complex statements can be built by nesting condition statements.
 *
 * @see ConditionalTrigger
 * @see ConditionStatement.Operator
 */
public class ConditionStatement implements Trigger {
    /**
     * An enum of logical operators which can be used to combine multiple {@link Trigger}s into a
     * single {@link ConditionStatement}.
     */
    public enum Operator {
        /** The logical AND operator. */
        AND {
            /**
             * @param operands the operands of the logical statement
             * @return <code>true</code> if all operands are activated, <code>false</code> otherwise
             */
            @Override
            public boolean eval(List<Trigger> operands) {
                for (Trigger operand : operands)
                    if (!operand.isActivated())
                        return false;
                return true;
            }
        },
        /** The logical OR operator. */
        OR {
            /**
             * @param operands the operands of the logical statement
             * @return <code>true</code> if at least one operand is activated, <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Trigger> operands) {
                for (Trigger operand : operands)
                    if (operand.isActivated())
                        return true;
                return false;
            }
        },
        /** The logical NOT operator. */
        NOT {
            /**
             * @param operands a list containing the single operand of the logical statement
             * @return <code>true</code> if the operand is not activated <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Trigger> operands) {
                if (operands.size() != 1)
                    throw new IllegalArgumentException("NOT operator can only have one operand.");
                return !operands.get(0).isActivated();
            }
        },
        /** The logical XOR operator. */
        XOR {
            /**
             * @param operands a list containing the two operands of the logical statement
             * @return <code>true</code> if exactly one operand is activated, <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Trigger> operands) {
                if (operands.size() != 2)
                    throw new IllegalArgumentException("XOR operator can only have two operands.");
                return operands.get(0).isActivated() ^ operands.get(1).isActivated();
            }
        };

        /**
         * Evaluates the logical statement represented by this {@link Operator} applied to the given
         * list of operands.
         *
         * @param operands the operands of the logical statement
         * @return <code>true</code> if the statement is fulfilled, <code>false</code> otherwise
         */
        public abstract boolean eval(List<Trigger> operands);
    }

    /** The {@link Operator} used to combine the operands of this {@link ConditionStatement}. */
    private final Operator operator;

    /** The operands of this {@link ConditionStatement}. */
    private final List<Trigger> operands;

    /**
     * @param operator the {@link Operator} used to combine the operands of this statement
     * @param operands the operands of the logical statement
     * @throws NullPointerException if the operator or the operands are <code>null</code>
     * @throws IllegalArgumentException if the operands list is empty
     * @see ConditionalTrigger
     */
    public ConditionStatement(Operator operator, List<Trigger> operands) {
        Objects.requireNonNull(operator, "Condition statement operator cannot be null.");
        Objects.requireNonNull(operands, "Condition statement operands cannot be null.");

        if (operands.isEmpty())
            throw new IllegalArgumentException("Condition statement operands cannot be empty.");

        this.operator = operator;
        this.operands = operands;
    }

    /** @return <code>true</code> if the statement is fulfilled, <code>false</code> otherwise */
    @Override
    public boolean isActivated() {return this.operator.eval(operands);}
}
