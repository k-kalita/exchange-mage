package exchangemage.effects.base;

import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Comparator;

import exchangemage.encounters.Scene;
import exchangemage.cards.Card;

/**
 * EffectPlayer is responsible for resolving effects of played cards as well as triggering
 * persistent effects present in the scene in an order determined by the resolution process stage
 * they can activate in.
 * <br><br>
 * The crucial methods of this class are:
 * <ul>
 *     <li>{@link #playCard(Card)} which allows for resolution of all effects of a card (along
 *     with any persistent effects triggered in the process).</li>
 *     <li>{@link #enqueueEffect(Effect, boolean)} which enqueues an effect into the resolution
 *     queue.</li>
 *     <li>{@link #resolveQueue()} which resolves all currently enqueued effects (along with any
 *     persistent effects triggered in the process).</li>
 * </ul>
 *
 * @see Effect
 * @see PersistentEffect
 * @see EffectResolutionStage
 */
public class EffectPlayer {
    private final Scene scene;
    private Effect currentEffect;
    private final Queue<Effect> resolutionQueue;
    private final Set<Effect> effectsPlayed;

    /**
     * EffectResolutionStage is an enum representing the stage of the resolution process of an
     * effect and is used to define the order in which the activation of persistent effects is
     * resolved.
     * <br><br>
     * The stages are:
     * <ul>
     *     <li>{@link #ACTIVATION} - if the conditions of a persistent effect's trigger depend on
     *     the initial, unmodified version of the effect being resolved and do not modify the effect
     *     itself (e.g. <i>Whenever an effect with a damage of 1 is played, give the target bleed
     *     3</i>) the persistent effect's activation should be resolved in this stage.</li>
     *     <li>{@link #MODIFICATION} - if the conditions of a persistent effect's trigger depend on
     *     the initial, unmodified version of the effect being resolved and modify the effect
     *     itself (e.g. <i>Whenever an effect with a damage of 1 is played, increase its damage by
     *     1</i>) the persistent effect's activation should be resolved in this stage.</li>
     *     <li>{@link #RESOLUTION} - if the conditions of a persistent effect's trigger depend
     *     on the modified version of the effect being resolved and/or their activation may act as
     *     a final intervention in the resolution process (e.g. <i>Whenever an effect would deal
     *     2 or more damage to this entity, reduce the damage to 1</i>) the persistent effect's
     *     activation should be resolved in this stage.</li>
     *     <li>{@link #RESPONSE} - if the conditions of a persistent effect's trigger depend on the
     *     final version of the effect being resolved and do not modify the effect itself (e.g.
     *     <i>Whenever you deal 3 or more damage to an entity, heal 1</i>) the persistent effect's
     *     activation should be resolved in this stage.</li>
     * </ul>
     *
     * @see Effect
     * @see PersistentEffect
     * @see EffectPlayer
     */
    public enum EffectResolutionStage {
        ACTIVATION, MODIFICATION, RESOLUTION, RESPONSE;

        /**
         * Sorts an array of persistent effects by their trigger stage in ascending order as
         * defined by the {@link EffectResolutionStage} enum.
         *
         * @param effects the array of persistent effects to sort.
         * @return the sorted array of persistent effects.
         *
         * @see PersistentEffect
         */
        public static PersistentEffect[] sortPersistentEffects(PersistentEffect[] effects) {
            Arrays.sort(effects, Comparator.comparing(PersistentEffect::getTriggerStage));
            return effects;
        }
    }

    /**
     * Creates a new EffectPlayer for the given Scene.
     *
     * @param scene the Scene to create the EffectPlayer for.
     * @throws IllegalArgumentException if the given Scene is null.
     *
     * @see Scene
     */
    public EffectPlayer(Scene scene) {
        if (scene == null)
            throw new IllegalArgumentException("EffectPlayer cannot be created with null Scene.");

        this.scene = scene;
        this.currentEffect = null;
        this.resolutionQueue = new LinkedList<>();
        this.effectsPlayed = new HashSet<>();
    }

    /**
     * Returns the current effect being resolved (or null if no effect is being resolved).
     *
     * @return the current effect being resolved.
     *
     * @see Effect
     */
    public Effect getCurrentEffect() {
        return this.currentEffect;
    }

    /**
     * Enqueues the given effect into the resolution queue if it can be activated and if requested
     * immediately chooses a target for it.
     *
     * @param effect the effect to enqueue.
     * @param chooseTargetImmediately whether to immediately choose a target for the effect.
     * @throws IllegalArgumentException if the given effect is null.
     */
    public void enqueueEffect(Effect effect, boolean chooseTargetImmediately) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to enqueue cannot be null.");

        if (effect.canActivate()) {
            if (chooseTargetImmediately)
                effect.chooseTarget();
            this.resolutionQueue.add(effect);
        }
    }

    /**
     * Resolves the current dequeued effect and the activation of persistent effects. Calls on the
     * {@link Effect#chooseTarget()} method of the current effect in the case it was not called upon
     * enqueueing the effect.
     *
     * @throws IllegalStateException if there is no effect to resolve.
     *
     * @see Effect
     * @see PersistentEffect
     * @see EffectResolutionStage
     */
    private void resolveEffect() {
        if (this.currentEffect == null)
            throw new IllegalStateException("No effect to resolve.");

        this.currentEffect.chooseTarget();

        PersistentEffect[] persistentEffects = EffectResolutionStage.sortPersistentEffects(
                this.scene.getEffects()
        );

        for (PersistentEffect effect : persistentEffects)
            if (effect.canActivate())
                effect.execute();
    }

    /**
     * Resolves all currently enqueued effects (along with any persistent effects activated in the
     * process).
     *
     * @return this EffectPlayer.
     *
     * @see Effect
     * @see PersistentEffect
     */
    public EffectPlayer resolveQueue() {
        while (!this.resolutionQueue.isEmpty()) {
            Effect effect = this.resolutionQueue.poll();

            if (!effect.isActivated())
                continue;

            this.currentEffect = effect;
            this.resolveEffect();
            this.effectsPlayed.add(effect);
        }

        this.currentEffect = null;
        return this;
    }

    /**
     * Enqueues all effects of the given card, immediately choosing targets for them and resolves
     * the resolution queue.
     *
     * @param card the card to play.
     * @throws IllegalArgumentException if the given card is null.
     */
    public void playCard(Card card) {
        if (card == null)
            throw new IllegalArgumentException("Card to play cannot be null.");

        for (Effect effect : card.getEffects())
            this.enqueueEffect(effect, true);

        this.resolveQueue();
    }
}
