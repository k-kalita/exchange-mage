package exchangemage.scenes;

public enum TestEncounters {
    BASIC {
        @Override
        public Encounter getEncounter() {
            return null;
        }
    },
    ;

    public abstract Encounter getEncounter();
}
