package exchangemage.actors;

import exchangemage.cards.TestDecks;

public enum TestPlayers {
    PLACEHOLDER {
        @Override
        public Player get() {
            return new Player(TestDecks.EMPTY.getDeck(), 10);
        }
    },
    ;

    public abstract Player get();
}