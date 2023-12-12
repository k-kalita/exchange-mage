package exchangemage.effects.triggers.conditions;

import java.util.Objects;
import java.util.List;

/**
 * A {@link Condition} which represents a logical statement composed of other conditions. Used to
 * combine multiple conditions into a single one using logical operators such as AND, OR, NOT, XOR.
 * More complex statements can be built by nesting {@link ConditionStatement}s.
 *
 * @see Condition
 * @see ConditionStatement.Operator
 */
public class ConditionStatement implements Condition {
    /**
     * Enumerates the logical operators which can be used to combine multiple {@link Condition}s
     * into a single {@link ConditionStatement}.
     */
    public enum Operator {
        /**
         * The logical AND operator.
         */
        AND {
            /**
             * Checks whether all operands of the logical statement are fulfilled.
             *
             * @param operands the operands of the logical statement
             * @return <code>true</code> if all operands are fulfilled, <code>false</code> otherwise
             */
            @Override
            public boolean eval(List<Condition> operands) {
                for (Condition operand : operands)
                    if (!operand.isFulfilled())
                        return false;
                return true;
            }
        },
        /**
         * The logical OR operator.
         */
        OR {
            /**
             * Checks whether at least one of the operands of the logical statement is fulfilled.
             *
             * @param operands the operands of the logical statement
             * @return <code>true</code> if at least one operand is fulfilled, <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Condition> operands) {
                for (Condition operand : operands)
                    if (operand.isFulfilled())
                        return true;
                return false;
            }
        },
        /**
         * The logical NOT operator.
         */
        NOT {
            /**
             * Checks whether the operand of the logical statement is not fulfilled.
             *
             * @param operands a list containing the single operand of the logical statement
             * @return <code>true</code> if the operand is not fulfilled <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Condition> operands) {
                if (operands.size() != 1)
                    throw new IllegalArgumentException("NOT operator can only have one operand.");
                return !operands.get(0).isFulfilled();
            }
        },
        /**
         * The logical XOR operator.
         */
        XOR {
            /**
             * Checks whether exactly one of the operands of the logical statement is fulfilled.
             *
             * @param operands a list containing the two operands of the logical statement
             * @return <code>true</code> if exactly one operand is fulfilled, <code>false</code>
             * otherwise
             */
            @Override
            public boolean eval(List<Condition> operands) {
                if (operands.size() != 2)
                    throw new IllegalArgumentException("XOR operator can only have two operands.");
                return operands.get(0).isFulfilled() ^ operands.get(1).isFulfilled();
            }
        };

        /**
         * Evaluates the logical statement represented by this {@link Operator} applied to the given
         * list of operands.
         *
         * @param operands the operands of the logical statement
         * @return <code>true</code> if the statement is fulfilled, <code>false</code> otherwise
         */
        public abstract boolean eval(List<Condition> operands);
    }

    /**
     * The {@link Operator} used to combine the operands of this {@link ConditionStatement}.
     */
    private final Operator operator;

    /**
     * The operands of this {@link ConditionStatement}.
     */
    private final List<Condition> operands;

    /**
     * Creates a new {@link ConditionStatement} with given {@link Operator} and operands.
     *
     * @param operator the operator used to combine the operands
     * @param operands the operands of the logical statement
     * @throws NullPointerException if the operator or the operands are null
     * @throws IllegalArgumentException if the operands list is empty
     * @see Condition
     */
    public ConditionStatement(Operator operator, List<Condition> operands) {
        Objects.requireNonNull(operator, "Condition operator cannot be null.");
        Objects.requireNonNull(operands, "Condition operands cannot be null.");

        if (operands.isEmpty())
            throw new IllegalArgumentException("Condition operands cannot be empty.");

        this.operator = operator;
        this.operands = operands;
    }

    /**
     * Evaluates the logical statement represented by this {@link ConditionStatement} to a boolean
     * value.
     *
     * @return <code>true</code> if the statement is fulfilled, <code>false</code> otherwise
     */
    @Override
    public boolean isFulfilled() {return this.operator.eval(operands);}
}
