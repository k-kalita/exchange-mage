package exchangemage.effects;

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

    @BeforeEach
    void setUp() {
        TestGames.PLACEHOLDER.getGame();
        Game.getGame().setPlayer(TestPlayers.PLACEHOLDER.getPlayer());
        this.encounter = TestEncounters.PLACEHOLDER.getEncounter();
        Game.getGame().setScene(encounter);
    }

    @Test
    void testBasicDamageEffectWithNoPersistentEffects() {
        int initialHealth = encounter.getEnemies().iterator().next().getCurrentHealth();
        var effectPlayer = GameState.getEffectPlayer();
        effectPlayer.evaluateEffect(TestEffects.DEAL_1_DAMAGE_TO_RANDOM_ENEMY.getEffect());
        effectPlayer.resolveQueue();
        int finalHealth = encounter.getEnemies().iterator().next().getCurrentHealth();
        Assertions.assertEquals(initialHealth - 1, finalHealth);
    }
}