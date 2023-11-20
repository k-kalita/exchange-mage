package exchangemage.effects.base;

import java.util.*;

import exchangemage.effects.targeting.TargetingManager;
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
 *     <li>{@link #evaluateEffect(Effect)} which evaluates whether an effect is activated (and if
 *     it is - handles the resolution in accordance with its {@link Effect.ResolutionMode}).</li>
 *     <li>{@link #resolveQueue()} which resolves all currently enqueued effects (along with any
 *     persistent effects triggered in the process).</li>
 * </ul>
 *
 * @see Effect
 * @see PersistentEffect
 * @see EffectResolutionStage
 * @see TargetingManager
 */
public class EffectPlayer {
    /**
     * The {@link Scene} this {@link EffectPlayer} is associated with.
     */
    private final Scene scene;
    /**
     * The {@link Effect} currently being resolved (or null if no effect is being resolved).
     */
    private Effect currentEffect = null;
    /**
     * The {@link TargetingManager} used to manage the process of choosing a target for
     * {@link Effect}s enqueued into the resolution queue.
     */
    private final TargetingManager targetingManager = new TargetingManager();
    /**
     * The queue of {@link Effect}s to be resolved.
     */
    private final Queue<Effect> resolutionQueue = new LinkedList<>();


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
         * Sorts a list of {@link PersistentEffect}s by their trigger stage in ascending order (as
         * defined by the {@link EffectResolutionStage} enum).
         *
         * @param effects the list of persistent effects to sort.
         * @return the sorted list of persistent effects.
         *
         * @see PersistentEffect
         */
        public static List<PersistentEffect> sortPersistentEffects(List<PersistentEffect> effects) {
            effects.sort(Comparator.comparing(PersistentEffect::getActivationStage));
            return effects;
        }
    }

    /**
     * Creates a new {@link EffectPlayer} for the given {@link Scene}.
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
    }

    /**
     * Returns the {@link EffectPlayer} of the current {@link Scene} in the game.
     *
     * @return the EffectPlayer of the current Scene.
     * @throws IllegalStateException if no Scene could be retrieved.
     *
     * @see EffectPlayer
     * @see Scene
     */
    public static EffectPlayer getEffectPlayer() {
        if (Scene.getScene() == null)
            throw new IllegalStateException("Cannot retrieve current Scene.");
        return Scene.getScene().getEffectPlayer();
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
     * Returns whether there is an {@link Effect} currently being resolved by the
     * {@link EffectPlayer}.
     *
     * @return <code>true</code> if there is an effect currently being resolved, <code>false</code>
     * otherwise.
     *
     * @see Effect
     */
    public boolean effectInResolution() { return this.currentEffect != null; }

    /**
     * Evaluates whether given {@link Effect} has a valid target and if it is activated. If so,
     * handles the resolution in accordance with the effect's {@link Effect.ResolutionMode}.
     *
     * @param effect the effect to evaluate.
     * @throws IllegalArgumentException if the given effect is null.
     * @throws IllegalStateException if the resolution mode of the effect is not recognized.
     *
     * @see Effect
     * @see Trigger
     * @see TargetingManager
     * @see Effect.ResolutionMode
     */
    public void evaluateEffect(Effect effect) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to evaluate cannot be null.");
        if (!this.targetingManager.setActiveEffect(effect).chooseTarget())
            return;
        if (!effect.isActivated())
            return;

        switch (effect.getResolutionMode()) {
            case ENQUEUE -> enqueueEffect(effect);
            case IMMEDIATE -> resolveEffectImmediately(effect);
            default -> throw new IllegalStateException(
                    "Effect resolution mode not recognized: " + effect.getResolutionMode()
            );
        }
    }

    /**
     * Enqueues given {@link Effect} into the resolution queue.
     *
     * @param effect the effect to enqueue.
     * @throws IllegalArgumentException if the effect is null.
     * @throws IllegalStateException if the effect has no target.
     *
     * @see Effect
     * @see TargetingManager
     */
    private void enqueueEffect(Effect effect) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to enqueue cannot be null.");
        if (!effect.hasTarget())
            throw new IllegalStateException("Cannot enqueue effect with no target.");

        this.resolutionQueue.add(effect);
    }

    /**
     * Resolves given {@link Effect} (if it can be resolved) and evaluates the activation of
     * {@link PersistentEffect}s present in the scene.
     *
     * @param effect the effect to resolve
     * @throws IllegalArgumentException if the given effect is null.
     * @throws IllegalStateException if there is already an effect being resolved or if the given
     * effect has no target.
     *
     * @see Effect
     * @see PersistentEffect
     * @see EffectResolutionStage
     */
    private void resolveEffect(Effect effect) {
        if (effect == null)
            throw new IllegalArgumentException("Effect to resolve cannot be null.");
        if (effectInResolution())
            throw new IllegalStateException("There is already an effect being resolved.");
        if (!effect.hasTarget())
            throw new IllegalStateException("Effect to resolve has no target.");
        if (!effect.canResolve())
            return;

        this.currentEffect = effect;
        EffectResolutionStage
                .sortPersistentEffects(this.scene.getEffects())
                .forEach(this::evaluateEffect);
        effect.execute();
        this.currentEffect = null;
    }

    /**
     * Resolves given {@link Effect} immediately, regardless of whether there is another effect
     * being resolved or not. If a resolution of another effect is interrupted by this method, it
     * is resumed after this effect is resolved.
     * <br><br>
     * This method is used to resolve effects with the {@link Effect.ResolutionMode#IMMEDIATE}
     * resolution mode.
     *
     * @param effect the effect to resolve.
     *
     * @see Effect
     * @see Effect.ResolutionMode
     */
    private void resolveEffectImmediately(Effect effect) {
        Effect currentEffect = this.currentEffect;
        this.currentEffect = null;
        evaluateEffect(effect);
        this.currentEffect = currentEffect;
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
        while (!this.resolutionQueue.isEmpty())
            resolveEffect(this.resolutionQueue.poll());
        return this;
    }

    /**
     * Calls the {@link #evaluateEffect} method on all {@link Effect}s of the given {@link Card}
     * to determine their targets and activation. Then resolves all enqueued effects.
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
            this.evaluateEffect(effect);

        this.resolveQueue();
    }
}
