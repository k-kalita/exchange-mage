package exchangemage.effects.value;

import java.util.List;
import java.util.Set;

import exchangemage.base.Game;
import exchangemage.base.GameLocator;
import exchangemage.base.GameStateLocator;
import exchangemage.base.BaseGameState;
import exchangemage.actors.Player;
import exchangemage.actors.Enemy;
import exchangemage.cards.Deck;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.conditions.ComparisonCondition;
import exchangemage.effects.triggers.conditions.Condition;
import exchangemage.effects.triggers.conditions.ConditionStatement;
import exchangemage.effects.triggers.conditions.EffectTypeCondition;
import exchangemage.effects.targeting.selectors.VariableTargetSelector;
import exchangemage.effects.triggers.conditions.comparators.NonNullComparator;
import exchangemage.effects.triggers.conditions.getters.EffectInResolutionGetter;
import exchangemage.effects.triggers.conditions.getters.EffectSourceGetter;
import exchangemage.scenes.BasicTurnPlayer;
import exchangemage.scenes.Encounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DamageEffectTest {
    private static Player              player;
    private static Enemy               enemy;
    private static DamageEffect<Enemy> simpleEffect;
    private static ConditionalTrigger  trigger;

    @BeforeAll
    static void setUp() {
        Game mockGame = Mockito.mock(Game.class);
        player = new Player(Mockito.mock(Deck.class), 100);
        GameLocator.init(mockGame);
        GameStateLocator.init(new BaseGameState());
        Mockito.when(mockGame.getPlayer()).thenReturn(player);
        enemy = new Enemy(Mockito.mock(Deck.class), 100);
        VariableTargetSelector<Enemy> selector = new VariableTargetSelector<>(
                Enemy.class,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        Encounter encounter = new Encounter(new BasicTurnPlayer(), Set.of(enemy));
        Mockito.when(mockGame.getScene()).thenReturn(encounter);
        simpleEffect = new DamageEffect<>(
                "Deal 1 damage to random enemy",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );
        Condition damageEffectInResolution = new EffectTypeCondition(
                new EffectInResolutionGetter(),
                DamageEffect.class
        );
        Condition playerIsSource = new ComparisonCondition<Player>(
                new EffectSourceGetter<>(Player.class, new EffectInResolutionGetter()),
                new NonNullComparator<>()
        );
        trigger = new ConditionalTrigger(
                new ConditionStatement(ConditionStatement.Operator.AND,
                                       List.of(damageEffectInResolution, playerIsSource))
        );
    }

    @BeforeEach
    void reset() {
        player.setCurrentHealth(100);
        enemy.setCurrentHealth(100);
    }

    @Test
    void testSimpleResolve() {
        simpleEffect.resetSource();
        GameStateLocator.getGameState().getEffectPlayer().evaluateEffect(simpleEffect);
        assertEquals(99, enemy.getCurrentHealth());
    }

    @Test
    void testWithModifier() {
        ValueModifierEffect modifierEffect = new ValueModifierEffect(
                "Increase damage by 1", 1
        );
        PersistentEffect triggeredModifierEffect = new PersistentEffect(
                "Whenever you cast a damaging spell, increase damage by 1",
                List.of(modifierEffect),
                EffectPlayer.EffectResolutionStage.MODIFICATION,
                trigger
        );
        GameStateLocator.getGameState().getPlayer().addPersistentEffect(
                triggeredModifierEffect
        );
        simpleEffect.setSource(player);
        GameStateLocator.getGameState().getEffectPlayer().evaluateEffect(simpleEffect);
        assertEquals(98, enemy.getCurrentHealth());
    }
}