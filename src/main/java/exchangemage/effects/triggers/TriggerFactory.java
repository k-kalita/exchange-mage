package exchangemage.effects.triggers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import exchangemage.base.Factory;
import exchangemage.base.FactoryLocator;

public class TriggerFactory implements Factory<Trigger> {
    private enum TriggerType implements FactoryNode<Trigger> {
        CONDITIONAL {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                JsonNode subject     = sourceJson.get("subject");
                JsonNode condition   = sourceJson.get("condition");

                if (subject == null)
                    throw new SourceFormatException("Conditional trigger definition is missing the "
                                                    + "required \"subject\" field.");

                var subjectGetter = FactoryLocator.getGetterFactory().createFromJson(subject);

                if (condition == null)
                    return new ConditionalTrigger(subjectGetter, null);

                return new ConditionalTrigger(
                        subjectGetter,
                        FactoryLocator.getConditionFactory().createFromJson(condition)
                );
            }

            @Override
            public String val() {return "conditional";}
        },
        COMPOSITE {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "composite";}
        },
        EFFECT_TYPE {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "effectType";}
        },
        EFFECT_VALUE {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "effectValue";}
        },
        ACTORS_TURN {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "actorsTurn";}
        },
        NOTIFICATION {
            @Override
            public Trigger createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "notification";}
        }
    }

    private final Map<String, FactoryNode<Trigger>> triggerTypes = new HashMap<>();

    public TriggerFactory(List<FactoryNode<Trigger>> triggerTypes) {
        this();
        triggerTypes.forEach(type -> this.triggerTypes.put(type.val(), type));
    }

    public TriggerFactory() {
        for (var type : TriggerType.values())
            triggerTypes.put(type.val(), type);
    }

    @Override
    public Trigger createFromJson(JsonNode sourceJson) {
        String type = sourceJson.get("type").asText();

        if (type == null)
            throw new SourceFormatException("Trigger definition is missing the required \"type\" "
                                            + "field.");

        if (this.triggerTypes.containsKey(type))
            return this.triggerTypes.get(type).createFromJson(sourceJson);

        throw new SourceFormatException(String.format(
                "Trigger definition has an invalid \"type\" field value: %s", type
        ));
    }
}
