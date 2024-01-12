package exchangemage.base;

public enum TestGames {
    PLACEHOLDER {
        @Override
        public void getGame() {Game.getGame();}
    },
    ;

    public abstract void getGame();
}
