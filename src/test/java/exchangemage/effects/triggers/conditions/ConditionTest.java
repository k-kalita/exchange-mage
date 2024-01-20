package exchangemage.effects.triggers.conditions;

import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.ValueEffect;
import exchangemage.effects.triggers.getters.SubjectGetter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConditionTest {
    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testInstanceComparator() {
        String                    test1     = new String("test");
        String                    test2     = new String("test");
        SubjectGetter<String>     getter    = () -> test1;
        InstanceCondition<String> condition = new InstanceCondition<>(getter);
        assertTrue(condition.evaluate(test1));
        assertFalse(condition.evaluate(test2));
        assertFalse(condition.evaluate(null));
    }

    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testValueComparator() {
        String                 test1     = new String("test");
        String                 test2     = new String("test");
        ValueCondition<String> condition = new ValueCondition<>(test1);
        assertTrue(condition.evaluate(test1));
        assertTrue(condition.evaluate(test2));
        assertFalse(condition.evaluate("not test"));
        assertFalse(condition.evaluate(null));
    }

    @ParameterizedTest
    @CsvSource({"EQ,1,1,true", "EQ,1,0,false", "NEQ,1,1,false", "NEQ,1,0,true",
                "LT,1,1,false", "LT,1,2,false", "LT,1,0,true",
                "LTE,1,1,true", "LTE,1,2,false", "LTE,1,0,true",
                "GT,1,1,false", "GT,1,2,true", "GT,1,0,false",
                "GTE,1,1,true", "GTE,1,2,true", "GTE,1,0,false"})
    void testNumericValueComparator(NumericValueCondition.Operator operator,
                                    int value,
                                    int comparedValue,
                                    boolean result) {
        NumericValueCondition<Integer> condition = new NumericValueCondition<>(value, operator);
        assertEquals(condition.evaluate(comparedValue), result);
    }

    @Test
    void testNonNullComparator() {
        NonNullCondition<String> condition = new NonNullCondition<>();
        assertTrue(condition.evaluate("test"));
        assertFalse(condition.evaluate(null));
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testTypeComparator() {
        Effect<?>             mockEffect             = Mockito.mock(Effect.class);
        DamageEffect<?>       mockDamageEffect       = Mockito.mock(DamageEffect.class);
        NotificationEffect    mockNotificationEffect = Mockito.mock(NotificationEffect.class);
        TypeCondition<Effect> condition              = new TypeCondition<>(ValueEffect.class);
        assertFalse(condition.evaluate(mockEffect));
        assertTrue(condition.evaluate(mockDamageEffect));
        assertFalse(condition.evaluate(mockNotificationEffect));
        assertFalse(condition.evaluate(null));
    }
}