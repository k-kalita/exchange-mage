package exchangemage.effects.value;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.selectors.TargetSelector;

/**
 * Abstract base class for all {@link Effect}s which execution relies upon a numerical value. Value
 * effects are used to represent effects such as <i>damage</i> or <i>healing</i>.
 * <br><br>
 * Value effects use a {@link ValueGenerator} to generate the value of the effect and during their
 * resolution can receive {@link ValueModifier}s which will affect it before execution.
 *
 * @param <T> the type of target chosen by this effect's {@link TargetSelector}
 * @see ValueGenerator
 * @see ValueModifier
 * @see EffectPlayer
 */
public abstract class ValueEffect<T extends Targetable> extends Effect<T> {
    /**
     * Enum representing the possible states of the value carried by a {@link ValueEffect}.
     */
    public enum ValueState {
        /**
         * The state representing the initial value generated by a {@link ValueEffect}'s
         * {@link ValueGenerator} at the start of its resolution.
         */
        ORIGINAL {
            /**
             * Returns the result of {@link ValueEffect#getOriginalValue} of the specified
             * {@link ValueEffect}.
             *
             * @param effect the effect to retrieve the value from
             * @return original value of the effect
             */
            @Override
            public int getValue(ValueEffect<?> effect) {return effect.getOriginalValue();}
        },
        /**
         * The state representing current value of a {@link ValueEffect} without applying any
         * {@link ValueModifier}s.
         */
        UNMODIFIED {
            /**
             * Returns the result of {@link ValueEffect#getUnmodifiedValue} of the specified
             * {@link ValueEffect}.
             *
             * @param effect the effect to retrieve the value from
             * @return unmodified value of the effect
             */
            @Override
            public int getValue(ValueEffect<?> effect) {return effect.getUnmodifiedValue();}
        },
        /**
         * The state representing current value of a {@link ValueEffect} after applying all
         * {@link ValueModifier}s.
         */
        MODIFIED {
            /**
             * Returns the result of {@link ValueEffect#getModifiedValue} of the specified
             * {@link ValueEffect}.
             *
             * @param effect the effect to retrieve the value from
             * @return modified value of the effect
             */
            @Override
            public int getValue(ValueEffect<?> effect) {return effect.getModifiedValue();}
        };

        /**
         * Returns the value for the specified {@link ValueEffect} according to this state.
         *
         * @param effect the effect to retrieve the value from
         * @return the value for the specified effect according to this state
         */
        public abstract int getValue(ValueEffect<?> effect);
    }

    /**
     * The {@link ValueGenerator} used to generate the value of the effect.
     */
    ValueGenerator valueGenerator;

    /**
     * The list of {@link ValueModifier}s applied to the value of the effect.
     */
    List<ValueModifier> valueModifiers = new ArrayList<>();

    /**
     * The initial value generated by the {@link #valueGenerator} at the start of the effect's
     * resolution.
     */
    int originalValue;

    /**
     * Constructs a new value effect with the specified {@link ValueGenerator}, {@link Trigger},
     * {@link TargetSelector}, and {@link ResolutionMode}.
     *
     * @param valueGenerator the value generator used to generate the value of the effect
     * @param trigger        the trigger of the effect
     * @param targetSelector the target selector of the effect
     * @param resolutionMode the resolution mode of the effect
     * @throws NullPointerException if any of the arguments are <code>null</code>
     */
    public ValueEffect(ValueGenerator valueGenerator,
                       Trigger trigger,
                       TargetSelector<T> targetSelector,
                       ResolutionMode resolutionMode) {
        super(trigger, targetSelector, resolutionMode);
        Objects.requireNonNull(valueGenerator, "Value generator cannot be null");
        this.valueGenerator = valueGenerator;
    }

    /**
     * Selects a target for the value effect using the superclass {@link Effect#selectTarget} method
     * and saves the value generated by the {@link #valueGenerator} at this initial point in the
     * effect's resolution.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise
     * @see Effect#selectTarget
     * @see ValueGenerator
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        if (super.selectTarget(forbiddenTargets)) {
            this.originalValue = valueGenerator.generate();
            return true;
        }
        return false;
    }

    /**
     * Adds a new {@link ValueModifier} to the effect's list of value modifiers.
     *
     * @param valueModifier the value modifier to add
     * @throws NullPointerException     if the value modifier is <code>null</code>
     * @throws IllegalArgumentException if the value modifier has already been added
     * @see ValueModifier
     */
    public void addValueModifier(ValueModifier valueModifier) {
        Objects.requireNonNull(valueModifier, "Value modifier cannot be null.");
        if (this.valueModifiers.contains(valueModifier))
            throw new IllegalArgumentException("Cannot add value modifier that has already " +
                                               "been added.");
        this.valueModifiers.add(valueModifier);
    }

    // --------------------------------- value getter methods --------------------------------- //

    /**
     * Returns the initial value generated by the {@link #valueGenerator} at the start of the
     * effect's resolution.
     *
     * @return initial value generated by the value generator
     */
    public int getOriginalValue() {return this.originalValue;}

    /**
     * Returns the current value returned by the {@link #valueGenerator} without applying any
     * {@link ValueModifier}s.
     *
     * @return the current value returned by the value generator
     */
    public int getUnmodifiedValue() {return this.valueGenerator.generate();}

    /**
     * Returns the current value returned by the {@link #valueGenerator} after applying all
     * {@link ValueModifier}s.
     *
     * @return the current, possibly modified, value returned by the value generator
     */
    public int getModifiedValue() {
        int value = this.valueGenerator.generate();
        for (ValueModifier modifier : this.valueModifiers)
            value = modifier.modify(value);
        return value;
    }
}
