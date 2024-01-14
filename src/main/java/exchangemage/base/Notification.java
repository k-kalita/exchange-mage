package exchangemage.base;

import exchangemage.effects.EffectSource;

public interface Notification {
    default String getText(EffectSource source) {return "";};
}
