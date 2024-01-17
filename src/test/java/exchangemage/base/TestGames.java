package exchangemage.base;

public enum TestGames {
    PLACEHOLDER {
        @Override
        public Game get() {return new Game();}
    },
    ;

    public abstract Game get();
}
