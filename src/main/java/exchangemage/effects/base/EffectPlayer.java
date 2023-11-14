package exchangemage.effects.base;

import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Comparator;

import exchangemage.encounters.Scene;
import exchangemage.cards.Card;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.Condition;

/**
 * EffectPlayer is responsible for resolving effects of played cards as well as triggering
 * persistent effects present in the scene in an order determined by the resolution process stage
 * they can activate in.
 * <br><br>
 * The crucial methods of this class are:
 * <ul>
 *     <li>{@link #playCard(Card)} which allows for resolution of all effects of a card (along
 *     with any persistent effects triggered in the process).</li>
 *     <li>{@link #enqueueEffect(Effect)} which enqueues an effect into the resolution
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
     * EffectResolutionStage is an enum representing the stages of the resolution process of an
     * {@link Effect} and is used to define the order in which the activation of
     * {@link PersistentEffect}s is resolved.
     * <br><br>
     * The stages are:
     * <ul>
     *     <li>{@link #ACTIVATION} - if the conditions of a persistent effect's activation trigger
     *     depend on the initial, unmodified version of the effect being resolved and do not
     *     modify the effect itself (e.g. <i>Whenever an effect with a damage of 1 is played,
     *     give the target bleed 3</i>) the persistent effect's activation should be resolved in
     *     this stage.</li>
     *     <li>{@link #MODIFICATION} - if the conditions of a persistent effect's activation
     *     trigger depend on the initial, unmodified version of the effect being resolved and
     *     modify the effect itself (e.g. <i>Whenever an effect with a damage of 1 is played,
     *     increase its damage by 1</i>) the persistent effect's activation should be resolved in
     *     this stage.</li>
     *     <li>{@link #RESOLUTION} - if the conditions of a persistent effect's activation trigger
     *     depend on the modified version of the effect being resolved and/or their activation
     *     may act as a final intervention in the resolution process (e.g. <i>Whenever an effect
     *     would deal 2 or more damage to this entity, reduce the damage to 1</i>) the persistent
     *     effect's activation should be resolved in this stage.</li>
     *     <li>{@link #RESPONSE} - if the conditions of a persistent effect's activation trigger
     *     depend on the final version of the effect being resolved and do not modify the effect
     *     itself (e.g. <i>Whenever you deal 3 or more damage to an entity, heal 1</i>) the
     *     persistent effect's activation should be resolved in this stage.</li>
     * </ul>
     *
     * @see Effect
     * @see PersistentEffect
     * @see EffectPlayer
     */
    public enum EffectResolutionStage {
        /**
         * The initial stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which do not modify the {@link Effect} being resolved and
         * whose {@link Trigger} {@link Condition}s depend on its initial, unmodified version.
         *
         * @see Effect
         * @see PersistentEffect
         */
        ACTIVATION,
        /**
         * The second stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which modify the {@link Effect} being resolved and whose
         * {@link Trigger} {@link Condition}s depend on its initial, unmodified version.
         *
         * @see Effect
         * @see PersistentEffect
         */
        MODIFICATION,
        /**
         * The third stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s whose {@link Trigger} {@link Condition}s depend on the
         * final version of the {@link Effect} being resolved and/or whose activation may act as
         * a final intervention in the resolution process.
         *
         * @see Effect
         * @see PersistentEffect
         */
        RESOLUTION,
        /**
         * The final stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which do not modify the {@link Effect} being resolved and
         * whose {@link Trigger} {@link Condition}s depend on its final version, acting as a
         * response to the resolution of the effect.
         *
         * @see Effect
         * @see PersistentEffect
         */
        RESPONSE;

        /**
         * Sorts an array of {@link PersistentEffect}s by their trigger stage in ascending order as
         * defined by the {@link EffectResolutionStage} enum.
         *
         * @param effects the array of persistent effects to sort.
         * @return the sorted array of persistent effects.
         *
         * @see PersistentEffect
         */
        public static PersistentEffect[] sortPersistentEffects(PersistentEffect[] effects) {
            Arrays.sort(effects, Comparator.comparing(PersistentEffect::getActivationStage));
            return effects;
        }
    }

    /**
     * Creates a new {@link EffectPlayer} for the given {@link Scene}.
     *
     * @param scene the Scene to create the EffectPlayer for.
     *              
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
     * Returns the current {@link Effect} being resolved (or null if no effect is being resolved).
     *
     * @return the current effect being resolved.
     *
     * @see Effect
     */
    public Effect getCurrentEffect() { return this.currentEffect; }

    /**
     * Enqueues the given {@link Effect} into the resolution queue if it can be activated and
     * calls on its {@link Effect#chooseTarget()} method.
     *
     * @param effect the effect to enqueue.
     *
     * @throws IllegalArgumentException if the given effect is null.
     *
     * @see Effect
     * @see exchangemage.effects.targeting.TargetSelector
     */
    public void enqueueEffect(Effect effect) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to enqueue cannot be null.");

        if (effect.isActivated()) {
            if (effect.chooseTarget())
                this.resolutionQueue.add(effect);
        }
    }

    /**
     * Resolves the current, dequeued {@link Effect} and the activation of
     * {@link PersistentEffect}s which could be triggered by it.
     *
     * @throws IllegalStateException if there is no effect to resolve or the effect to resolve
     * has no target.
     *
     * @see Effect
     * @see PersistentEffect
     * @see EffectResolutionStage
     */
    private void resolveEffect() {
        if (this.currentEffect == null)
            throw new IllegalStateException("No effect to resolve.");
        if (!this.currentEffect.hasTarget())
            throw new IllegalStateException("Effect to resolve has no target.");

        PersistentEffect[] persistentEffects = EffectResolutionStage.sortPersistentEffects(
                this.scene.getEffects()
        );

        for (PersistentEffect effect : persistentEffects)
            enqueueEffect(effect);
    }

    /**
     * Resolves all currently enqueued {@link Effect}s (along with any persistent effects activated
     * in the process).
     *
     * @return this {@link EffectPlayer}
     *
     * @see Effect
     * @see PersistentEffect
     */
    public EffectPlayer resolveQueue() {
        while (!this.resolutionQueue.isEmpty()) {
            Effect effect = this.resolutionQueue.poll();

            if (!effect.canEvaluate())
                continue;

            this.currentEffect = effect;
            this.resolveEffect();
            this.effectsPlayed.add(effect);
        }

        this.currentEffect = null;
        return this;
    }

    /**
     * Enqueues all {@link Effect}s of the given {@link Card}, immediately choosing targets for
     * them and resolves the resolution queue.
     *
     * @param card the card to play.
     * @throws IllegalArgumentException if the given card is null.
     *
     * @see Effect
     * @see Card
     */
    public void playCard(Card card) {
        if (card == null)
            throw new IllegalArgumentException("Card to play cannot be null.");

        for (Effect effect : card.getEffects())
            this.enqueueEffect(effect);

        this.resolveQueue();
    }
}
