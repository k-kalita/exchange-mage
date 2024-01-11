package exchangemage.scenes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import exchangemage.actors.Actor;
import exchangemage.actors.Player;
import exchangemage.base.GameState;
import exchangemage.base.Observer;
import exchangemage.base.Observable;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.targeting.Targetable;

/**
 * Base class for all scenes in the game. A scene is a container for {@link Actor}s and
 * {@link PersistentEffect}s in which game events such as battles or dialogues take place.
 * <br><br>
 * All {@link Effect}s used to affect the elements of the game such as enemies, the player, or the
 * environment are resolved in the context of a scene with the help of an {@link EffectPlayer}.
 * Current scenes can be accessed through the {@link GameState} class.
 *
 * @see GameState
 * @see EffectPlayer
 * @see Encounter
 */
public abstract class Scene
        implements Targetable, PersistentEffectsHolder, EffectSource, Observable {
    /**
     * The set of {@link Actor}s present in the scene, including the {@link Player}.
     */
    protected final Set<Actor> actors = new HashSet<>();

    /**
     * The {@link EffectPlayer} used to evaluate and resolve {@link Effect}s in the scene.
     */
    private final EffectPlayer effectPlayer = new EffectPlayer();

    /**
     * The set of {@link PersistentEffect}s active in the scene.
     */
    private final Set<PersistentEffect> environmentalEffects = new HashSet<>();

    /**
     * The set of {@link Observer}s observing the scene.
     */
    private final Set<Observer> observers = new HashSet<>();

    /**
     * Constructs a new scene with the specified set of environmental effects, adding the
     * {@link Player} the set of {@link Actor}s present in it.
     *
     * @param environmentalEffects the set of {@link PersistentEffect}s active in the scene.
     */
    public Scene(Set<PersistentEffect> environmentalEffects) {
        this.actors.add(GameState.getPlayer());

        if (environmentalEffects != null)
            environmentalEffects.forEach(this::addPersistentEffect);
    }

    /**
     * Starts the scene. Concrete implementations of the scene base class provide their own
     * logic for how a scene starts and is played out.
     */
    public abstract void start();

    /**
     * Removes the specified {@link Actor} from the scene.
     *
     * @param actor actor to remove
     * @throws NullPointerException     if the actor is <code>null</code>
     * @throws IllegalArgumentException if the actor has not been added to the scene
     */
    public void removeActor(Actor actor) {
        Objects.requireNonNull(actor, "Cannot remove null actor.");
        if (!this.actors.contains(actor))
            throw new IllegalArgumentException("Cannot remove actor that has not been added.");
        this.actors.remove(actor);
    }

    // ------------------------------------ getter methods ------------------------------------ //

    /**
     * Returns all {@link Targetable}s present in the scene, including the ones held by the
     * {@link Actor}s present.
     *
     * @return the set of {@link Targetable}s present in the scene
     * @see Targetable
     */
    public Set<Targetable> getTargetables() {
        Set<Targetable> targetables = new HashSet<>();
        this.actors.forEach(actor -> {
            targetables.addAll(actor.getTargetables());
            targetables.add(actor);
        });
        targetables.addAll(this.environmentalEffects);
        return targetables;
    }

    /**
     * Returns the {@link EffectPlayer} used to evaluate and resolve {@link Effect}s in the scene.
     *
     * @return the scene's effect player
     */
    public EffectPlayer getEffectPlayer() {return this.effectPlayer;}

    /**
     * Returns the set of all {@link PersistentEffect}s active in the scene, including the
     * {@link #environmentalEffects} and the ones held by individual {@link Actor}s present.
     *
     * @return the set of all persistent effects active in the scene
     */
    public Set<PersistentEffect> getAllPersistentEffects() {
        Set<PersistentEffect> allEffects = new HashSet<>(this.environmentalEffects);
        this.actors.forEach(actor -> allEffects.addAll(actor.getPersistentEffects()));
        return allEffects;
    }

    // --------------------------- persistent effects holder methods -------------------------- //

    @Override
    public void addPersistentEffect(PersistentEffect effect) {
        Objects.requireNonNull(effect, "Cannot add null persistent effect.");
        if (this.environmentalEffects.contains(effect))
            throw new IllegalArgumentException("Cannot add persistent effect that has already " +
                                               "been added.");
        this.environmentalEffects.add(effect);
    }

    @Override
    public void removePersistentEffect(PersistentEffect effect) {
        Objects.requireNonNull(effect, "Cannot remove null persistent effect.");
        if (!this.environmentalEffects.contains(effect))
            throw new IllegalArgumentException("Cannot remove persistent effect that has not " +
                                               "been added.");
        this.environmentalEffects.remove(effect);
    }

    /**
     * Returns the set of {@link #environmentalEffects} present in the scene. Does not include
     * {@link PersistentEffect}s held by individual {@link Actor}s.
     *
     * @return the set of persistent effects active in the scene
     */
    @Override
    public Set<PersistentEffect> getPersistentEffects() {return this.environmentalEffects;}

    // --------------------------------- observable methods ----------------------------------- //

    @Override
    public void addObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot add null observer.");
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot remove null observer.");
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException("Cannot remove observer that has not been added.");

        this.observers.remove(observer);
    }

    @Override
    public Set<Observer> getObservers() {return this.observers;}
}
