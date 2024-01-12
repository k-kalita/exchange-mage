package exchangemage.base;

public enum TestGames {
    PLACEHOLDER {
        @Override
        public Game getGame() {return new Game();}
    },
    ;

    public abstract Game getGame();
}
