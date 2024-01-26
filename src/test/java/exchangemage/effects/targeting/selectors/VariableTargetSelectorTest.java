package exchangemage.effects.targeting.selectors;

import java.util.Set;

import exchangemage.base.Game;
import exchangemage.base.GameLocator;
import exchangemage.base.GameStateLocator;
import exchangemage.base.BaseGameState;
import exchangemage.actors.DeckHolderActor;
import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.actors.Enemy;
import exchangemage.cards.Deck;
import exchangemage.effects.Effect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.scenes.BasicTurnPlayer;
import exchangemage.scenes.Encounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class VariableTargetSelectorTest {
    private static Player player;
    private static Enemy  enemy1;
    private static Enemy  enemy2;

    @BeforeAll
    static void setUp() {
        Game mockGame = Mockito.mock(Game.class);
        player = new Player(Mockito.mock(Deck.class), 100);
        GameLocator.init(mockGame);
        GameStateLocator.init(new BaseGameState());
        Mockito.when(mockGame.getPlayer()).thenReturn(player);
        enemy1 = new Enemy(Mockito.mock(Deck.class), 100);
        enemy2 = new Enemy(Mockito.mock(Deck.class), 100);
        Encounter encounter = new Encounter(new BasicTurnPlayer(), Set.of(enemy1, enemy2));
        Mockito.when(mockGame.getScene()).thenReturn(encounter);
    }

    @BeforeEach
    void reset() {
        player.setCurrentHealth(100);
        enemy1.setCurrentHealth(100);
        enemy2.setCurrentHealth(100);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects a random {@link Enemy} in the scene
     * as its target. Then tests if the {@link DamageEffect} the selector is assigned to deals
     * damage exclusively to enemy {@link Actor}s in the current scene.
     */
    @Test
    void testSelectRandomEnemy() {
        VariableTargetSelector<Enemy> selector = new VariableTargetSelector<>(
                Enemy.class,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        DamageEffect<Enemy> effect = new DamageEffect<>(
                "Deal 1 damage to random enemy",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );

        for (int i = 0; i < 99; i++)
             GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 100);
        assertEquals(enemy1.getCurrentHealth() + enemy2.getCurrentHealth(), 101);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects a random {@link Enemy} in the scene
     * whose current health is greater than 50 as its target. Then tests if the {@link DamageEffect}
     * the selector is assigned to deals damage exclusively to enemy {@link Actor}s in the current
     * scene who fit the filter criteria.
     */
    @Test
    void testSelectRandomEnemyWithFilter() {
        VariableTargetSelector<Enemy> selector = new VariableTargetSelector<>(
                Enemy.class,
                subject -> ((Enemy) subject).getCurrentHealth() > 50,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        DamageEffect<Enemy> effect = new DamageEffect<>(
                "Deal 1 damage to random enemy with more than 50 health",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );

        for (int i = 0; i < 100; i++)
             GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 100);
        assertEquals(enemy1.getCurrentHealth(), 50);
        assertEquals(enemy2.getCurrentHealth(), 50);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects a random {@link Enemy} in the scene
     * whose current health is greater than 99 as its target. Then tests if the {@link DamageEffect}
     * the selector is assigned to deals damage exclusively to enemy {@link Actor}s in the current
     * scene who fit the filter criteria. Also tests if the effect evaluation is skipped if no
     * {@link Enemy} in the scene fits the filter criteria.
     */
    @Test
    void testSelectRandomEnemyWithFilterFailedSelection() {

        VariableTargetSelector<Enemy> selector = new VariableTargetSelector<>(
                Enemy.class,
                subject -> ((Enemy) subject).getCurrentHealth() > 99,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        DamageEffect<Enemy> effect = new DamageEffect<>(
                "Deal 1 damage to random enemy with more than 99 health",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );

        for (int i = 0; i < 10; i++)
             GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 100);
        assertEquals(enemy1.getCurrentHealth(), 99);
        assertEquals(enemy2.getCurrentHealth(), 99);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects a random {@link Player} in the scene
     * as its target. Then tests if the {@link DamageEffect} the selector is assigned to deals
     * damage exclusively to the {@link Player} in the current scene.
     */
    @Test
    void testSelectRandomPlayer() {
        VariableTargetSelector<Player> selector = new VariableTargetSelector<>(
                Player.class,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        DamageEffect<Player> effect = new DamageEffect<>(
                "Deal 1 damage to random player",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );

        for (int i = 0; i < 99; i++)
             GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 1);
        assertEquals(enemy1.getCurrentHealth(), 100);
        assertEquals(enemy2.getCurrentHealth(), 100);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects a random {@link Actor} in the scene
     * whose current health is greater than 75 as its target. Then tests if the {@link DamageEffect}
     * the selector is assigned to deals damage to both enemies and the {@link Player} in and
     * if the evaluation is skipped once no actor in the scene fits the filter criteria.
     */
    @Test
    void testSelectRandomActorWithFilter() {
        VariableTargetSelector<DeckHolderActor> selector = new VariableTargetSelector<>(
                DeckHolderActor.class,
                subject -> ((DeckHolderActor) subject).getCurrentHealth() > 75,
                VariableTargetSelector.TargetingMode.RANDOM
        );
        DamageEffect<DeckHolderActor> effect = new DamageEffect<>(
                "Deal 1 damage to random enemy with more than 75 health",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );

        for (int i = 0; i < 60; i++)
             GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertTrue(player.getCurrentHealth() >= 75 && player.getCurrentHealth() <= 100);
        assertTrue(enemy1.getCurrentHealth() >= 75 && enemy1.getCurrentHealth() <= 100);
        assertTrue(enemy2.getCurrentHealth() >= 75 && enemy2.getCurrentHealth() <= 100);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects an {@link Enemy} in the scene
     * chosen by the player as its target. Creates a thread which waits for the
     * {@link TargetingManager} to enter its <i>awaiting-input</i> state and then selects an
     * enemy. Checks if the {@link DamageEffect} the selector is assigned to deals damage to the
     * selected enemy and only that enemy.
     */
    @Test
    @SuppressWarnings("BusyWait")
    void testSelectEnemy() {
        TargetingManager targetingManager = GameStateLocator.getGameState().getTargetingManager();
        VariableTargetSelector<Enemy> selector = new VariableTargetSelector<>(
                Enemy.class,
                VariableTargetSelector.TargetingMode.SELECT
        );
        DamageEffect<Enemy> effect = new DamageEffect<>(
                "Deal 1 damage to selected enemy",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );
        Thread thread = new Thread(() -> {
            synchronized (targetingManager.getPlayerSelectionLock()) {
                try {
                    targetingManager.getPlayerSelectionLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            targetingManager.chooseTarget(enemy1);
        });

        thread.start();

        while (thread.getState() != Thread.State.WAITING) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 100);
        assertEquals(enemy1.getCurrentHealth(), 99);
        assertEquals(enemy2.getCurrentHealth(), 100);
    }

    /**
     * Creates a {@link VariableTargetSelector} which selects any {@link Actor} in the scene
     * chosen by the player as its target. Then plays a {@link DamageEffect} using this selector
     * twice, with the player-simulating threads selecting first an enemy and then the player.
     * Checks if the {@link DamageEffect} the selector is assigned to deals damage to the selected
     * targets.
     */
    @Test
    @SuppressWarnings("BusyWait")
    void testSelectActor() {
        TargetingManager targetingManager = GameStateLocator.getGameState().getTargetingManager();
        VariableTargetSelector<DeckHolderActor> selector = new VariableTargetSelector<>(
                DeckHolderActor.class,
                VariableTargetSelector.TargetingMode.SELECT
        );
        DamageEffect<DeckHolderActor> effect = new DamageEffect<>(
                "Deal 1 damage to selected actor",
                1, selector, Effect.ResolutionMode.IMMEDIATE
        );
        Thread thread1 = new Thread(() -> {
            synchronized (targetingManager.getPlayerSelectionLock()) {
                try {
                    targetingManager.getPlayerSelectionLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            targetingManager.chooseTarget(player);
        });
        Thread thread2 = new Thread(() -> {
            synchronized (targetingManager.getPlayerSelectionLock()) {
                try {
                    targetingManager.getPlayerSelectionLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            targetingManager.chooseTarget(enemy2);
        });

        thread1.start();

        while (thread1.getState() != Thread.State.WAITING) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);
        thread2.start();

        while (thread2.getState() != Thread.State.WAITING) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameStateLocator.getGameState().getScene().getEffectPlayer().evaluateEffect(effect);

        assertEquals(player.getCurrentHealth(), 99);
        assertEquals(enemy1.getCurrentHealth(), 100);
        assertEquals(enemy2.getCurrentHealth(), 99);
    }
}