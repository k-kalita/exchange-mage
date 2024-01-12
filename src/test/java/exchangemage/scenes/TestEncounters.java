package exchangemage.scenes;

import java.util.Set;

import exchangemage.actors.TestEnemies;

public enum TestEncounters {
    PLACEHOLDER {
        @Override
        public Encounter get() {
            return new Encounter(new BasicTurnPlayer(),
                                 Set.of(TestEnemies.PLACEHOLDER.getEnemy()));
        }
    },
    ;

    public abstract Encounter get();
}
