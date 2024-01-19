package exchangemage.effects.triggers.conditions;

import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.effects.value.HealEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EffectTypeConditionTest {
    private static GameState mockGameState;
    private static DamageEffect<?> mockDamageEffect;
    private static HealEffect<?> mockHealEffect;
    private static EffectTypeCondition condition;

    @BeforeAll
    static void setUp() {
        mockGameState = Mockito.mock(GameState.class);
        mockDamageEffect = Mockito.mock(DamageEffect.class);
        mockHealEffect = Mockito.mock(HealEffect.class);
        condition = new EffectTypeCondition(new EffectInResolutionGetter(),
                                            HealEffect.class);
        GameStateLocator.init(mockGameState);
    }
    @Test
    void testIsFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockHealEffect);
        assertTrue(condition.isFulfilled());
    }

    @Test
    void testIsNotFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertFalse(condition.isFulfilled());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertFalse(condition.isFulfilled());
    }
}