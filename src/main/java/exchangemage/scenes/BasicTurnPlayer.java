package exchangemage.scenes;

import java.util.List;
import java.util.ArrayList;

import exchangemage.actors.Actor;
import exchangemage.base.GameStateLocator;

public class BasicTurnPlayer implements TurnPlayer {
    private Encounter encounter;
    private final List<Actor> queue = new ArrayList<>();

    @Override
    public void init(Encounter encounter) {
        this.encounter = encounter;
        queue.add(GameStateLocator.getGameState().getPlayer());
        queue.addAll(encounter.getEnemies());
    }

    @Override
    public void start() {
        while (encounter.enemiesAlive()) {
            notifyRoundStarted();

            for (Actor actor : queue) {
                if (actor.isDead()) {
                    queue.remove(actor);
                    continue;
                }

                notifyTurnStarted(actor);
                actor.takeTurn();
                notifyTurnEnded(actor);
            }

            notifyRoundEnded();
        }
    }
}
