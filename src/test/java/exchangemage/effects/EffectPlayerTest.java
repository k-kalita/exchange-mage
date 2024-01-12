package exchangemage.effects;

import exchangemage.actors.Player;
import exchangemage.scenes.Encounter;
import exchangemage.scenes.TestEncounters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import exchangemage.base.Game;
import exchangemage.base.GameState;
import exchangemage.base.TestGames;
import exchangemage.actors.TestPlayers;

class EffectPlayerTest {
    private Encounter encounter;
    private Player player;
    private EffectPlayer effectPlayer;

    @BeforeEach
    void setUp() {
        TestGames.PLACEHOLDER.get();
        this.player = TestPlayers.PLACEHOLDER.get();
        Game.getGame().setPlayer(this.player);
        this.encounter = TestEncounters.PLACEHOLDER.get();
        Game.getGame().setScene(encounter);
        this.effectPlayer = GameState.getEffectPlayer();
    }

    @Test
    void testBasicDamageEffectWithNoPersistentEffects() {
        int initialHealth = encounter.getEnemies().iterator().next().getCurrentHealth();

        effectPlayer.evaluateEffect(TestEffects.DEAL_1_DAMAGE_TO_RANDOM_ENEMY.get());
        effectPlayer.resolveQueue();

        int finalHealth = encounter.getEnemies().iterator().next().getCurrentHealth();
        Assertions.assertEquals(initialHealth - 1, finalHealth);
    }

    @Test
    void testBasicDamageEffectWithBasicPersistentEffect() {
        int initialHealth = encounter.getEnemies().iterator().next().getCurrentHealth();
        var damageEffect = TestEffects.DEAL_1_DAMAGE_TO_RANDOM_ENEMY.get();

        damageEffect.setSource(player);
        player.addPersistentEffect(
                TestPersistentEffects.WHENEVER_ENEMY_IS_DAMAGED_DEAL_THEM_1_DAMAGE.get()
        );
        effectPlayer.evaluateEffect(damageEffect);
        effectPlayer.resolveQueue();

        int finalHealth = encounter.getEnemies().iterator().next().getCurrentHealth();
        Assertions.assertEquals(initialHealth - 2, finalHealth);
    }
}