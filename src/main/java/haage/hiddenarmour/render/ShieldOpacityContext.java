package haage.hiddenarmour.render;

public final class ShieldOpacityContext {
    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Integer> ALPHA = ThreadLocal.withInitial(() -> 255);

    private ShieldOpacityContext() {
    }

    public static void activate(int alpha) {
        ACTIVE.set(true);
        ALPHA.set(Math.max(0, Math.min(255, alpha)));
    }

    public static boolean isActive() {
        return ACTIVE.get();
    }

    public static int getAlpha() {
        return ALPHA.get();
    }

    public static void clear() {
        ACTIVE.set(false);
        ALPHA.set(255);
    }
}