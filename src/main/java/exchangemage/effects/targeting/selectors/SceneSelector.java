package exchangemage.effects.targeting.selectors;

import exchangemage.base.GameStateLocator;
import exchangemage.scenes.Scene;
import exchangemage.effects.Effect;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.NotificationEffect;

/**
 * A {@link ConstantTargetSelector} which always targets the current {@link Scene}. Used by
 * {@link Effect}s which influence the current scene (e.g. by adding {@link PersistentEffect}s) or
 * by simple effects which do not require any particular target (e.g. {@link NotificationEffect}s).
 *
 * @see Scene
 * @see ConstantTargetSelector
 * @see TargetSelector
 */
public class SceneSelector extends ConstantTargetSelector<Scene> {
    /** Creates a new {@link SceneSelector}.*/
    public SceneSelector() {super(() -> GameStateLocator.getGameState().getScene(), Scene.class);}
}
