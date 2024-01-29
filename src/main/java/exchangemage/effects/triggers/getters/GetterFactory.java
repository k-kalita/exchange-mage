package exchangemage.effects.triggers.getters;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import exchangemage.actors.Actor;
import exchangemage.actors.Enemy;
import exchangemage.actors.Player;
import exchangemage.base.factory.Factory;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectSource;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.value.ValueEffect;
import exchangemage.scenes.Encounter;
import exchangemage.scenes.Scene;

public class GetterFactory implements Factory<SubjectGetter<?>> {
    private enum GetterType implements FactoryNode<SubjectGetter<?>> {
        EFFECT {
            @Override
            public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
                String origin = sourceJson.get("origin").asText();

                if (origin == null)
                    throw new SourceFormatException("Effect trigger definition is missing the "
                                                    + "required \"origin\" field.");

                if (origin.equals("self"))
                    return new EffectInEvaluationGetter();
                if (origin.equals("resolution"))
                    return new EffectInResolutionGetter();

                throw new SourceFormatException(String.format(
                        "Effect getter definition has an invalid \"origin\" field value: %s",
                        origin
                ));
            }

            @Override
            public String val() {return "effect";}
        },
        EFFECT_SOURCE {
            @Override
            public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
                String                        origin  = sourceJson.get("origin").asText();
                String                        subType = sourceJson.get("subType").asText();
                Class<? extends EffectSource> subTypeClass;

                if (origin == null)
                    throw new SourceFormatException("Effect source getter definition is missing "
                                                    + "the required \"origin\" field.");
                if (subType == null)
                    subType = "effectSource";

                switch (subType) {
                    case "effectSource" -> subTypeClass = EffectSource.class;
                    case "scene" -> subTypeClass = Scene.class;
                    case "encounter" -> subTypeClass = Encounter.class;
                    case "actor" -> subTypeClass = Actor.class;
                    case "player" -> subTypeClass = Player.class;
                    case "enemy" -> subTypeClass = Enemy.class;
                    default -> throw new SourceFormatException(String.format(
                            "Effect source getter definition has an invalid \"subType\" field "
                            + "value: %s", subType
                    ));
                }

                if (origin.equals("self"))
                    return new EffectSourceGetter<>(subTypeClass, new EffectInEvaluationGetter());
                if (origin.equals("resolution"))
                    return new EffectSourceGetter<>(subTypeClass, new EffectInResolutionGetter());

                throw new SourceFormatException(String.format(
                        "Effect source getter definition has an invalid \"origin\" field value: %s",
                        origin
                ));
            }

            @Override
            public String val() {return "effectSource";}
        },
        EFFECT_TARGET {
            @Override
            public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
                String                      origin  = sourceJson.get("origin").asText();
                String                      subType = sourceJson.get("subType").asText();
                Class<? extends Targetable> subTypeClass;

                if (origin == null)
                    throw new SourceFormatException("Effect target getter definition is missing "
                                                    + "the required \"origin\" field.");
                if (subType == null)
                    subType = "targetable";

                switch (subType) {
                    case "targetable" -> subTypeClass = Targetable.class;
                    case "scene" -> subTypeClass = Scene.class;
                    case "encounter" -> subTypeClass = Encounter.class;
                    case "actor" -> subTypeClass = Actor.class;
                    case "player" -> subTypeClass = Player.class;
                    case "enemy" -> subTypeClass = Enemy.class;
                    case "effect" -> subTypeClass = Effect.class;
                    default -> throw new SourceFormatException(String.format(
                            "Effect target getter definition has an invalid \"subType\" field "
                            + "value: %s", subType
                    ));
                }

                if (origin.equals("self"))
                    return new EffectTargetGetter<>(subTypeClass, new EffectInEvaluationGetter());
                if (origin.equals("resolution"))
                    return new EffectTargetGetter<>(subTypeClass, new EffectInResolutionGetter());

                throw new SourceFormatException(String.format(
                        "Effect target getter definition has an invalid \"origin\" field value: %s",
                        origin
                ));
            }

            @Override
            public String val() {return "effectTarget";}
        },
        EFFECT_VALUE {
            @Override
            public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
                String state = sourceJson.get("state").asText();
                ValueEffect.ValueState valueState;

                if (state == null)
                    state = "current";

                switch (state) {
                    case "current" -> valueState = ValueEffect.ValueState.MODIFIED;
                    case "unmodified" -> valueState = ValueEffect.ValueState.UNMODIFIED;
                    case "original" -> valueState = ValueEffect.ValueState.ORIGINAL;
                    default -> throw new SourceFormatException(String.format(
                            "Effect value getter definition has an invalid \"state\" field " +
                            "value: %s", state
                    ));
                }

                return new EffectValueGetter(valueState);
            }

            @Override
            public String val() {return "effectValue";}
        },
        NOTIFICATION {
            @Override
            public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
                return new NotificationGetter();
            }

            @Override
            public String val() {return "notification";}
        }
    }

    private final Map<String, FactoryNode<SubjectGetter<?>>> getterTypes = new HashMap<>();


    public GetterFactory(List<FactoryNode<SubjectGetter<?>>> getterTypes) {
        this();
        getterTypes.forEach(type -> this.getterTypes.put(type.val(), type));
    }

    public GetterFactory() {
        for (var type : GetterType.values())
            getterTypes.put(type.val(), type);
    }

    @Override
    public SubjectGetter<?> createFromJson(JsonNode sourceJson) {
        String type = sourceJson.get("type").asText();

        if (type == null)
            throw new SourceFormatException("Trigger definition is missing the required \"type\" "
                                            + "field.");

        if (this.getterTypes.containsKey(type))
            return this.getterTypes.get(type).createFromJson(sourceJson);

        throw new SourceFormatException(String.format(
                "Trigger definition has an invalid \"type\" field value: %s", type
        ));
    }
}
