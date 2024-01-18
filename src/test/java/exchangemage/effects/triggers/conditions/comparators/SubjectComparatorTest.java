package exchangemage.effects.triggers.conditions.comparators;

import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.ValueEffect;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SubjectComparatorTest {
    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testInstanceComparator() {
        String                     test1      = new String("test");
        String                     test2      = new String("test");
        SubjectGetter<String>      getter     = () -> test1;
        InstanceComparator<String> comparator = new InstanceComparator<>(getter);
        assertTrue(comparator.compare(test1));
        assertFalse(comparator.compare(test2));
        assertFalse(comparator.compare(null));
    }

    @Test
    @SuppressWarnings("StringOperationCanBeSimplified")
    void testValueComparator() {
        String                  test1      = new String("test");
        String                  test2      = new String("test");
        ValueComparator<String> comparator = new ValueComparator<>(test1);
        assertTrue(comparator.compare(test1));
        assertTrue(comparator.compare(test2));
        assertFalse(comparator.compare("not test"));
        assertFalse(comparator.compare(null));
    }

    @ParameterizedTest
    @CsvSource({"EQ,1,1,true", "EQ,1,0,false", "NEQ,1,1,false", "NEQ,1,0,true",
                "LT,1,1,false", "LT,1,2,false", "LT,1,0,true",
                "LTE,1,1,true", "LTE,1,2,false", "LTE,1,0,true",
                "GT,1,1,false", "GT,1,2,true", "GT,1,0,false",
                "GTE,1,1,true", "GTE,1,2,true", "GTE,1,0,false"})
    void testNumericValueComparator(NumericValueComparator.Operator operator,
                                    int value,
                                    int comparedValue,
                                    boolean result) {
        NumericValueComparator<Integer> comparator = new NumericValueComparator<>(value, operator);
        assertEquals(comparator.compare(comparedValue), result);
    }

    @Test
    void testNonNullComparator() {
        NonNullComparator<String> comparator = new NonNullComparator<>();
        assertTrue(comparator.compare("test"));
        assertFalse(comparator.compare(null));
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testTypeComparator() {
        Effect<?>              mockEffect             = Mockito.mock(Effect.class);
        DamageEffect<?>        mockDamageEffect       = Mockito.mock(DamageEffect.class);
        NotificationEffect     mockNotificationEffect = Mockito.mock(NotificationEffect.class);
        TypeComparator<Effect> comparator             = new TypeComparator<>(ValueEffect.class);
        assertFalse(comparator.compare(mockEffect));
        assertTrue(comparator.compare(mockDamageEffect));
        assertFalse(comparator.compare(mockNotificationEffect));
        assertFalse(comparator.compare(null));
    }
}