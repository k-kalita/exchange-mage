package exchangemage.effects.triggers;

import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.DamageEffect;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTriggerTest {
    private static GameState           mockGameState;
    private static Player              mockPlayer;
    private static NotificationTrigger trigger;

    @BeforeAll
    static void setUp() {
        mockGameState = Mockito.mock(GameState.class);
        mockPlayer    = Mockito.mock(Player.class);
        trigger       = new NotificationTrigger(Actor.ActorEvent.DAMAGE_RECEIVED);
        GameStateLocator.init(mockGameState);
    }

    /**
     * Tests if the {@link NotificationTrigger} checking for effect a <code>DAMAGE_RECEIVED</code>
     * notification is fulfilled if the {@link NotificationEffect} in resolution contains that
     * notification.
     */
    @Test
    void testIsFulfilled() {
        NotificationEffect effect = new NotificationEffect(Actor.ActorEvent.DAMAGE_RECEIVED,
                                                           mockPlayer);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> effect);
        assertTrue(trigger.isActivated());
    }

    /**
     * Tests if the {@link NotificationTrigger} checking for effect a <code>DAMAGE_RECEIVED</code>
     * notification is not fulfilled if the {@link NotificationEffect} in resolution contains a
     * different notification, if the effect in resolution is not a {@link NotificationEffect}, or
     * if there is no effect in resolution.
     */
    @Test
    void testIsNotFulfilled() {
        NotificationEffect effect       = new NotificationEffect(Actor.ActorEvent.DEATH, mockPlayer);
        DamageEffect<?>    damageEffect = Mockito.mock(DamageEffect.class);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> effect);
        assertFalse(trigger.isActivated());
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> damageEffect);
        assertFalse(trigger.isActivated());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertFalse(trigger.isActivated());
    }
}