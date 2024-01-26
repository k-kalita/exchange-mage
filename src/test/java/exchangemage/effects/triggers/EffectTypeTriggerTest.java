package exchangemage.effects.triggers;

import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.actors.Actor;
import exchangemage.effects.targeting.selectors.TargetSelector;
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

    private static class SpecialHealEffect<T extends Actor> extends HealEffect<T> {
        public SpecialHealEffect(String description, int value,
                                 TargetSelector<T> targetSelector,
                                 ResolutionMode resolutionMode) {
            super(description, value, targetSelector, resolutionMode);
        }
    }

    @BeforeAll
    static void setUp() {
        mockGameState    = Mockito.mock(GameState.class);
        mockDamageEffect = Mockito.mock(DamageEffect.class);
        mockHealEffect   = Mockito.mock(HealEffect.class);
        trigger          = new EffectTypeTrigger(new EffectInResolutionGetter(),
                                                 HealEffect.class);
        GameStateLocator.init(mockGameState);
    }

    /**
     * Tests if the {@link EffectTypeTrigger} checking for effect in resolution is fulfilled if the
     * effect in resolution is of the specified type.
     */
    @Test
    void testIsFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockHealEffect);
        assertTrue(trigger.isActivated());
    }

    /**
     * Tests if the {@link EffectTypeTrigger} checking for effect in resolution is fulfilled if the
     * effect in resolution is of a subclass of the specified type.
     */
    @Test
    void testIsFulfilledWithSubclass() {
        SpecialHealEffect<?> specialHealEffect = Mockito.mock(SpecialHealEffect.class);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> specialHealEffect);
        assertTrue(trigger.isActivated());
    }

    /**
     * Tests if the {@link EffectTypeTrigger} checking for effect in resolution is not fulfilled if
     * the effect in resolution is of a superclass of the specified type.
     */
    @Test
    void testIsFulfilledWithSuperclass() {
        EffectTypeTrigger specialTrigger = new EffectTypeTrigger(new EffectInResolutionGetter(),
                                                                 SpecialHealEffect.class);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockHealEffect);
        assertFalse(specialTrigger.isActivated());
    }

    /**
     * Tests if the {@link EffectTypeTrigger} checking for effect in resolution is not fulfilled if
     * the effect in resolution is not of the specified type or if there is no effect in resolution.
     */
    @Test
    void testIsNotFulfilled() {
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertFalse(trigger.isActivated());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertFalse(trigger.isActivated());
    }
}