package exchangemage.base.factory;

import exchangemage.effects.Effect;
import exchangemage.effects.NotificationEffect;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.value.DamageEffect;
import exchangemage.effects.value.ValueEffect;

public enum EffectType {
    BASE("base", Effect.class),
    VALUE("value", ValueEffect.class),
    DAMAGE("damage", DamageEffect.class),
    HEAL("heal", ValueEffect.class),
    NOTIFICATION("notification", NotificationEffect.class),
    PERSISTENT("persistent", PersistentEffect.class);

    private final String val;

    private final Class<?> effectClass;

    private EffectType(String val, Class<?> effectClass) {
        this.val = val;
        this.effectClass = effectClass;
    }

    public String val() {return val;}

    public Class<?> effectClass() {return effectClass;}

    public static EffectType fromVal(String val) {
        for (var type : EffectType.values())
            if (type.val.equals(val))
                return type;
        return null;
    }
}
