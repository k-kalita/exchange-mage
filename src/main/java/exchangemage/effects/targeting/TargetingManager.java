package exchangemage.effects.targeting;

import exchangemage.base.GameState;
import exchangemage.effects.base.Effect;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

public class TargetingManager {
    private final Set<Targetable> forbiddenTargets = new HashSet<>();
    private final Object lock = new Object();

    public boolean selectTarget(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect cannot be null.");

        if (effect.hasTarget())
            return !forbiddenTargets.contains(effect.getTarget());

        return effect.selectTarget(forbiddenTargets);
    }

    public void addForbiddenTargetable(Targetable targetable) {
        Objects.requireNonNull(targetable, "Forbidden targetable cannot be null.");
        this.forbiddenTargets.add(targetable);
    }

    public void clearForbiddenTargetables() {this.forbiddenTargets.clear();}

    public void waitForTarget() {
        synchronized (this.lock) {
            while (!GameState.getEffectInEvaluation().hasTarget()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void chooseTarget(Targetable target) {
        Objects.requireNonNull(target, "Target cannot be null.");
        synchronized (this.lock) {
            GameState.getEffectInEvaluation().getTargetSelector().setTarget(target);
            this.lock.notify();
        }
    }
}
