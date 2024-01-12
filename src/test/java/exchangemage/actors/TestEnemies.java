package exchangemage.actors;

import exchangemage.cards.TestDecks;

public enum TestEnemies {
    PLACEHOLDER {
        @Override
        public Enemy getEnemy() {
            return new Enemy(TestDecks.EMPTY.getDeck(), 10);
        }
    },
    ;

    public abstract Enemy getEnemy();
}