package exchangemage.effects.triggers;

import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.effects.value.HealEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EffectTypeTriggerTest {
    private static GameState         mockGameState;
    private static DamageEffect<?>   mockDamageEffect;
    private static HealEffect<?>     mockHealEffect;
    private static EffectTypeTrigger trigger;

    @BeforeAll
    static void setUp() {
        mockGameState = Mockito.mock(GameState.class);
        mockDamageEffect = Mockito.mock(DamageEffect.class);
        mockHealEffect = Mockito.mock(HealEffect.class);
        trigger = new EffectTypeTrigger(new EffectInResolutionGetter(),
                                        HealEffect.class);
        GameStateLocator.init(mockGameState);
    }

    @Test
    void testIsFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockHealEffect);
        assertTrue(trigger.isActivated());
    }

    @Test
    void testIsNotFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertFalse(trigger.isActivated());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertFalse(trigger.isActivated());
    }
}