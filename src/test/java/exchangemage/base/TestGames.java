package exchangemage.base;

public enum TestGames {
    PLACEHOLDER {
        @Override
        public void get() {Game.getGame();}
    },
    ;

    public abstract void get();
}
