package exchangemage.effects.triggers.conditions;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import exchangemage.base.factory.Factory;
import exchangemage.base.factory.FactoryLocator;
import exchangemage.effects.triggers.getters.SubjectGetter;
import exchangemage.effects.triggers.conditions.NumericValueCondition.Operator;

public class ConditionFactory implements Factory<Condition> {
    private enum ConditionType implements FactoryNode<Condition> {
        SAME_AS {
            @Override
            public Condition createFromJson(JsonNode sourceJson) {
                JsonNode subject = sourceJson.get("subject");

                if (subject == null)
                    throw new SourceFormatException("Same-as condition definition is missing the "
                                                    + "required \"subject\" field.");

                return new InstanceCondition(FactoryLocator.getGetterFactory()
                                                           .createFromJson(subject));
            }

            @Override
            public String val() {return "sameAs";}
        },
        VALUE {
            @Override
            @SuppressWarnings("unchecked")
            public Condition createFromJson(JsonNode sourceJson) {
                JsonNode valueNode    = sourceJson.get("value");
                String   operatorName = sourceJson.get("operator").asText();
                Operator operator;

                if (valueNode == null)
                    throw new SourceFormatException("Value condition definition is missing the "
                                                    + "required \"value\" field.");
                if (operatorName == null)
                    throw new SourceFormatException("Value condition definition is missing the "
                                                    + "required \"operator\" field.");

                try {
                    operator = Operator.valueOf(operatorName);
                } catch (IllegalArgumentException e) {
                    throw new SourceFormatException(String.format(
                            "Value condition definition has an invalid \"operator\" field value: "
                            + "%s", operatorName
                    ));
                }

                if (valueNode.isTextual()) {
                    double value;

                    try {
                        value = Double.parseDouble(valueNode.asText());
                    } catch (NumberFormatException e) {
                        throw new SourceFormatException(String.format(
                                "Value condition definition has an invalid \"value\" field value: "
                                + "%s", valueNode.asText()
                        ));
                    }

                    return new NumericValueCondition(value, operator);
                }

                SubjectGetter<Number> valueGetter;

                try {
                    valueGetter = (SubjectGetter<Number>) FactoryLocator.getGetterFactory()
                                                                        .createFromJson(valueNode);
                } catch (ClassCastException e) {
                    throw new SourceFormatException(String.format(
                            "Value condition definition has an invalid \"value\" field value: %s",
                            valueNode
                    ));
                }

                return new NumericValueCondition(valueGetter, operator);
            }

            @Override
            public String val() {return "value";}
        },
        OF_TYPE {
            @Override
            public Condition createFromJson(JsonNode sourceJson) {
                return null;
            }

            @Override
            public String val() {return "ofType";}
        }
    }

    private final Map<String, FactoryNode<Condition>> conditionTypes = new HashMap<>();

    public ConditionFactory(List<FactoryNode<Condition>> conditionTypes) {
        this();
        conditionTypes.forEach(type -> this.conditionTypes.put(type.val(), type));
    }

    public ConditionFactory() {
        for (var type : ConditionType.values())
            conditionTypes.put(type.val(), type);
    }

    @Override
    public Condition createFromJson(JsonNode sourceJson) {
        String type = sourceJson.get("type").asText();

        if (type == null)
            throw new SourceFormatException("Trigger definition is missing the required \"type\" "
                                            + "field.");

        if (this.conditionTypes.containsKey(type))
            return this.conditionTypes.get(type).createFromJson(sourceJson);

        throw new SourceFormatException(String.format(
                "Trigger definition has an invalid \"type\" field value: %s", type
        ));
    }
}
