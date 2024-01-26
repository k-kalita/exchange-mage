package exchangemage.effects.triggers.conditions;

import exchangemage.base.Notification;
import exchangemage.actors.Actor;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectSource;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.HealEffect;
import exchangemage.effects.value.ValueEffect;
import exchangemage.effects.triggers.getters.SubjectGetter;
import exchangemage.effects.targeting.selectors.TargetSelector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConditionTest {
    /**
     * Tests if the {@link InstanceCondition} returns <code>true</code> if the provided value is
     * the same instance as the condition's value, and <code>false</code> in all other cases, even
     * if the provided value is equal to the condition's value.
     */
    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testInstanceCondition() {
        String                test1     = new String("test");
        String                test2     = new String("test");
        SubjectGetter<String> getter    = () -> test1;
        InstanceCondition     condition = new InstanceCondition(getter);
        assertTrue(condition.evaluate(test1));
        assertFalse(condition.evaluate(test2));
        assertFalse(condition.evaluate(null));
    }

    /**
     * Tests if the {@link ValueCondition} returns <code>true</code> if the provided value is
     * equal to the condition's value, regardless of whether it is the same instance or not, and if
     * it returns <code>false</code> if the provided value is not equal to the condition's value
     * or is <code>null</code>.
     */
    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testValueCondition() {
        String         test1     = new String("test");
        String         test2     = new String("test");
        ValueCondition condition = new ValueCondition(test1);
        assertTrue(condition.evaluate(test1));
        assertTrue(condition.evaluate(test2));
        assertFalse(condition.evaluate("not test"));
        assertFalse(condition.evaluate(null));
    }

    /**
     * Tests if the {@link NumericValueCondition} returns the correct result for each comparison
     * operator depending on the provided value parameters.
     *
     * @param operator      The comparison operator to use
     * @param value         The value held by the condition against which the compared value is
     *                      tested
     * @param comparedValue The value to compare against the condition's value
     * @param result        The expected result of the comparison
     */
    @ParameterizedTest
    @CsvSource({"EQ,1,1,true", "EQ,1,0,false", "NEQ,1,1,false", "NEQ,1,0,true",
                "LT,1,1,false", "LT,1,2,false", "LT,1,0,true",
                "LTE,1,1,true", "LTE,1,2,false", "LTE,1,0,true",
                "GT,1,1,false", "GT,1,2,true", "GT,1,0,false",
                "GTE,1,1,true", "GTE,1,2,true", "GTE,1,0,false"})
    void testNumericValueCondition(NumericValueCondition.Operator operator,
                                    int value,
                                    int comparedValue,
                                    boolean result) {
        NumericValueCondition condition = new NumericValueCondition(value, operator);
        assertEquals(condition.evaluate(comparedValue), result);
    }

    /**
     * Tests if the {@link TypeCondition} returns <code>true</code> if the provided value is an
     * instance of the condition's type, and <code>false</code> if it is not (or if it is
     * <code>null</code>).
     */
    @Test
    void testTypeCondition() {
        TargetSelector<Actor> selector         = Mockito.mock();
        TypeCondition         condition        = new TypeCondition(DamageEffect.class);
        Effect<?> healingEffect = new HealEffect<>("Heal 1", 1, selector,
                                                   Effect.ResolutionMode.IMMEDIATE);
        Effect<?> damageEffect = new DamageEffect<>("Deal 1 damage", 1, selector,
                                                    Effect.ResolutionMode.IMMEDIATE);

        assertTrue(condition.evaluate(damageEffect));
        assertFalse(condition.evaluate(healingEffect));
        assertFalse(condition.evaluate(null));
    }

    /**
     * Tests if the {@link TypeCondition} returns <code>true</code> if the provided value is an
     * instance of a subclass of the condition's type, and <code>false</code> if it is not .
     */
    @Test
    void testTypeConditionWithSubclass() {
        TargetSelector<Actor> selector         = Mockito.mock();
        Notification          mockNotification = Mockito.mock(Notification.class);
        EffectSource          mockSource       = Mockito.mock(EffectSource.class);
        TypeCondition         condition        = new TypeCondition(ValueEffect.class);
        Effect<?> healingEffect = new HealEffect<>("Heal 1", 1, selector,
                                                   Effect.ResolutionMode.IMMEDIATE);
        Effect<?> damageEffect = new DamageEffect<>("Deal 1 damage", 1, selector,
                                                    Effect.ResolutionMode.IMMEDIATE);
        Effect<?> notificationEffect = new NotificationEffect(mockNotification, mockSource);

        assertTrue(condition.evaluate(healingEffect));
        assertTrue(condition.evaluate(damageEffect));
        assertFalse(condition.evaluate(notificationEffect));
    }

    /**
     * Tests if the {@link TypeCondition} returns <code>false</code> if the provided value is an
     * instance of a superclass of the condition's type.
     */
    @Test
    void testTypeConditionWithSuperclass() {
        class SpecialDamageEffect<T extends Actor> extends DamageEffect<T> {
            public SpecialDamageEffect(String description, int value,
                                       TargetSelector<T> targetSelector,
                                       ResolutionMode resolutionMode) {
                super(description, value, targetSelector, resolutionMode);
            }
        }

        TargetSelector<Actor> selector         = Mockito.mock();
        TypeCondition         condition        = new TypeCondition(SpecialDamageEffect.class);

        Effect<?> damageEffect = new DamageEffect<>("Deal 1 damage", 1, selector,
                                                    Effect.ResolutionMode.IMMEDIATE);
        Effect<?> specialDamageEffect = new SpecialDamageEffect<>("Deal 1 damage",
                                                                  1, selector,
                                                                  Effect.ResolutionMode.IMMEDIATE);

        assertTrue(condition.evaluate(specialDamageEffect));
        assertFalse(condition.evaluate(damageEffect));
    }
}