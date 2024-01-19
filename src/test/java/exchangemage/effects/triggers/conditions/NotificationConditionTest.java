package exchangemage.effects.triggers.conditions;

import exchangemage.actors.Player;
import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.actors.Actor;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.DamageEffect;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class NotificationConditionTest {
    private static GameState mockGameState;
    private static Player mockPlayer;
    private static NotificationCondition condition;

    @BeforeAll
    static void setUp() {
        mockGameState = Mockito.mock(GameState.class);
        mockPlayer = Mockito.mock(Player.class);
        condition = new NotificationCondition(Actor.ActorEvent.DAMAGE_RECEIVED);
        GameStateLocator.init(mockGameState);
    }

    @Test
    void testIsFulfilled() {
        NotificationEffect effect = new NotificationEffect(Actor.ActorEvent.DAMAGE_RECEIVED,
                                                           mockPlayer);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> effect);
        assertTrue(condition.isFulfilled());
    }

    @Test
    void testIsNotFulfilled() {
        NotificationEffect effect = new NotificationEffect(Actor.ActorEvent.DEATH, mockPlayer);
        DamageEffect<?> mockDamageEffect = Mockito.mock(DamageEffect.class);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> effect);
        assertFalse(condition.isFulfilled());
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertFalse(condition.isFulfilled());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertFalse(condition.isFulfilled());
    }
}