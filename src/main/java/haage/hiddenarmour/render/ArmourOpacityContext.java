package haage.hiddenarmour.render;

public final class ArmourOpacityContext {
    private static final ThreadLocal<Integer> ALPHA_OVERRIDE = ThreadLocal.withInitial(() -> 255);

    private ArmourOpacityContext() {
    }

    public static void setAlpha(int alpha) {
        ALPHA_OVERRIDE.set(Math.max(0, Math.min(255, alpha)));
    }

    public static int getAlpha() {
        return ALPHA_OVERRIDE.get();
    }

    public static void clear() {
        ALPHA_OVERRIDE.set(255);
    }
}
