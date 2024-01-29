package exchangemage.base.factory;

import java.util.Objects;

import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.getters.SubjectGetter;
import exchangemage.effects.triggers.conditions.Condition;

public class FactoryLocator {
    private static Factory<Trigger>          triggerFactory;
    private static Factory<SubjectGetter<?>> getterFactory;
    private static Factory<Condition>        conditionFactory;

    public static void init(Factory<Trigger> triggerFactory,
                            Factory<SubjectGetter<?>> getterFactory,
                            Factory<Condition> conditionFactory) {
        Objects.requireNonNull(triggerFactory, "Trigger factory cannot be null");
        FactoryLocator.triggerFactory = triggerFactory;
    }

    public static Factory<Trigger> getTriggerFactory() {
        if (triggerFactory == null)
            throw new IllegalStateException("FactoryLocator has not been initialized");
        return triggerFactory;
    }

    public static Factory<SubjectGetter<?>> getGetterFactory() {
        if (getterFactory == null)
            throw new IllegalStateException("FactoryLocator has not been initialized");
        return getterFactory;
    }

    public static Factory<Condition> getConditionFactory() {
        if (conditionFactory == null)
            throw new IllegalStateException("FactoryLocator has not been initialized");
        return conditionFactory;
    }
}
