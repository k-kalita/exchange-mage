package exchangemage.effects.targeting;

import exchangemage.base.GameState;
import exchangemage.encounters.Scene;
import exchangemage.effects.base.Effect;
import exchangemage.effects.base.PersistentEffect;
import exchangemage.effects.base.NotificationEffect;

/**
 * A {@link ConstantTargetSelector} which always targets the current {@link Scene}. Used by
 * {@link Effect}s which influence the current scene (e.g. by adding {@link PersistentEffect}s) or
 * by simple effects which do not require any particular target (e.g. {@link NotificationEffect}s).
 *
 * @see Scene
 * @see ConstantTargetSelector
 * @see TargetSelector
 */
public class SceneSelector extends ConstantTargetSelector {
    /**
     * Creates a new {@link SceneSelector}.
     *
     * @see Scene
     * @see ConstantTargetSelector
     */
    public SceneSelector() {super(GameState::getScene);}
}
