package exchangemage.effects.triggers.conditions.getters;

import exchangemage.base.GameState;
import exchangemage.base.GameStateLocator;
import exchangemage.base.Notification;
import exchangemage.actors.Enemy;
import exchangemage.actors.Player;
import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.value.ValueEffect;
import exchangemage.effects.value.DamageEffect;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SubjectGetterTest {
    private static Effect<?>                mockEffect;
    private static GameState                mockGameState;
    private static Player                   mockPlayer;
    private static Enemy                    mockEnemy;
    private static EffectInEvaluationGetter effectInEvaluationGetter;
    private static EffectInResolutionGetter effectInResolutionGetter;

    @BeforeAll
    static void init() {
        mockEffect = Mockito.mock(Effect.class);
        mockGameState = Mockito.mock(GameState.class);
        mockPlayer = Mockito.mock(Player.class);
        mockEnemy = Mockito.mock(Enemy.class);
        effectInEvaluationGetter = new EffectInEvaluationGetter();
        effectInResolutionGetter = new EffectInResolutionGetter();
        GameStateLocator.init(mockGameState);
    }

    @Test
    void testEffectInEvaluationGetter() {
        Mockito.when(mockGameState.getEffectInEvaluation()).thenAnswer(invocation -> mockEffect);
        assertEquals(mockEffect, effectInEvaluationGetter.getSubject());
        Mockito.when(mockGameState.getEffectInEvaluation()).thenAnswer(invocation -> null);
        assertNull(effectInEvaluationGetter.getSubject());
    }

    @Test
    void testEffectInResolutionGetter() {
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        assertEquals(mockEffect, effectInResolutionGetter.getSubject());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> null);
        assertNull(effectInResolutionGetter.getSubject());
    }

    @Test
    void testEffectSourceGetter() {
        EffectSourceGetter<Player> getter = new EffectSourceGetter<>(Player.class,
                                                                     effectInResolutionGetter);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        Mockito.when(mockEffect.getSource()).thenReturn(mockPlayer);
        assertEquals(mockPlayer, getter.getSubject());
        Mockito.when(mockEffect.getSource()).thenReturn(mockEnemy);
        assertNull(getter.getSubject());
    }

    @Test
    void testEffectTargetGetter() {
        EffectTargetGetter<Enemy> getter = new EffectTargetGetter<>(Enemy.class,
                                                                    effectInResolutionGetter);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        Mockito.when(mockEffect.getTarget()).thenAnswer(invocation -> mockEnemy);
        assertEquals(mockEnemy, getter.getSubject());
        Mockito.when(mockEffect.getTarget()).thenAnswer(invocation -> mockPlayer);
        assertNull(getter.getSubject());
    }

    @ParameterizedTest
    @CsvSource({"ORIGINAL,1", "UNMODIFIED,2", "MODIFIED,3"})
    void testEffectValueGetter(ValueEffect.ValueState valueState, int value) {
        EffectValueGetter getter           = new EffectValueGetter(valueState);
        DamageEffect<?>   mockDamageEffect = Mockito.mock(DamageEffect.class);
        Mockito.when(mockDamageEffect.getOriginalValue()).thenReturn(1);
        Mockito.when(mockDamageEffect.getUnmodifiedValue()).thenReturn(2);
        Mockito.when(mockDamageEffect.getModifiedValue()).thenReturn(3);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertEquals(getter.getSubject(), value);
    }

    @Test
    void testEffectValueGetterWithNoValueEffect() {
        EffectValueGetter getter = new EffectValueGetter(ValueEffect.ValueState.ORIGINAL);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        assertNull(getter.getSubject());
    }

    @Test
    void testNotificationGetter() {
        NotificationGetter getter                 = new NotificationGetter();
        NotificationEffect mockNotificationEffect = Mockito.mock(NotificationEffect.class);
        Notification       mockNotification       = Mockito.mock(Notification.class);
        Mockito.when(mockNotificationEffect.getNotification()).thenReturn(mockNotification);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockNotificationEffect);
        assertEquals(getter.getSubject(), mockNotification);
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        assertNull(getter.getSubject());
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testEffectSubclassGetter() {
        EffectSubclassGetter<DamageEffect> getter = new EffectSubclassGetter<>(
                DamageEffect.class,
                effectInResolutionGetter
        );
        DamageEffect<?>    mockDamageEffect       = Mockito.mock(DamageEffect.class);
        NotificationEffect mockNotificationEffect = Mockito.mock(NotificationEffect.class);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockDamageEffect);
        assertEquals(getter.getSubject(), mockDamageEffect);
        Mockito.when(mockGameState.getEffectInResolution())
               .thenAnswer(invocation -> mockNotificationEffect);
        assertNull(getter.getSubject());
        Mockito.when(mockGameState.getEffectInResolution()).thenAnswer(invocation -> mockEffect);
        assertNull(getter.getSubject());
    }
}