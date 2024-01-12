package exchangemage.scenes;

import java.util.Set;

import exchangemage.actors.TestEnemies;

public enum TestEncounters {
    PLACEHOLDER {
        @Override
        public Encounter getEncounter() {
            return new Encounter(new BasicTurnPlayer(),
                                 Set.of(TestEnemies.PLACEHOLDER.getEnemy()));
        }
    },
    ;

    public abstract Encounter getEncounter();
}
