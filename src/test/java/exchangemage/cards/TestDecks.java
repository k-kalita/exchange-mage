package exchangemage.cards;

public enum TestDecks {
    EMPTY {
        @Override
        public Deck getDeck() {return new Deck();}
    },
    ;

    public abstract Deck getDeck();
}