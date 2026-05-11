package haage.hiddenarmour.config;

public enum ArmourScope {
    /** Hide armour on all players (self + others). */
    ALL,
    /** Hide armour only on the local player. */
    SELF_ONLY,
    /** Hide armour only on other players. */
    OTHERS_ONLY;

    /** Cycle to the next value in declaration order, wrapping around. */
    public ArmourScope next() {
        ArmourScope[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
