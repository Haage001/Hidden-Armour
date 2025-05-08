package haage.hiddenarmour.client;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class HiddenArmourScreen extends Screen {
    private static final int PANEL_W       = 220;
    private static final int PANEL_H       = 200;
    private static final int INSET         = 10;
    private static final int SPACING       = 5;
    private static final int SEP_THICKNESS = 1;
    private static final int BUTTON_H      = 20;

    private final Screen parent;
    private ButtonWidget armourBtn, elytraBtn, glintBtn, nameTagBtn, horseBtn, doneBtn;
    private int panelX, panelY;
    private int yTitle, ySep1, ySub1, yArmour, yElytra, yGlint, ySep2, ySub2, yHorse, ySep3, yDone;

    public HiddenArmourScreen(Screen parent) {
        super(Text.translatable("screen.hiddenarmour.title"));
        this.parent = parent;
    }

    /** Default constructor passes the current screen as parent */
    public HiddenArmourScreen() {
        this(MinecraftClient.getInstance().currentScreen);
    }

    @Override
    protected void init() {
        super.init();
        int cx = this.width  / 2;
        int cy = this.height / 2;
        panelX = cx - PANEL_W / 2;
        panelY = cy - PANEL_H / 2;

        TextRenderer tr = this.textRenderer;
        yTitle  = panelY + 8;
        ySep1   = yTitle + tr.fontHeight + SPACING;
        ySub1   = ySep1 + SEP_THICKNESS + SPACING;
        yArmour = ySub1 + tr.fontHeight + SPACING;
        yElytra = yArmour + BUTTON_H + SPACING;
        yGlint  = yElytra + BUTTON_H + SPACING;
        ySep2   = yGlint + BUTTON_H + SPACING;
        ySub2   = ySep2 + SEP_THICKNESS + SPACING;
        yHorse  = ySub2 + tr.fontHeight + SPACING;
        ySep3   = yHorse + BUTTON_H + SPACING;
        yDone   = ySep3 + SEP_THICKNESS + SPACING;

        armourBtn = addDrawableChild(
                ButtonWidget.builder(getArmourText(), b -> toggleArmour(b))
                        .dimensions(panelX + INSET, yArmour, PANEL_W - 2 * INSET, BUTTON_H)
                        .build()
        );

        elytraBtn = addDrawableChild(
                ButtonWidget.builder(getElytraText(), b -> toggleElytra(b))
                        .dimensions(panelX + INSET, yElytra, PANEL_W - 2 * INSET, BUTTON_H)
                        .build()
        );

        int toggleWidth = (PANEL_W - 2 * INSET - SPACING) / 2;
        glintBtn = addDrawableChild(
                ButtonWidget.builder(getGlintText(), b -> toggleGlint(b))
                        .dimensions(panelX + INSET, yGlint, toggleWidth, BUTTON_H)
                        .build()
        );
        nameTagBtn = addDrawableChild(
                ButtonWidget.builder(getNameTagText(), b -> toggleNameTag(b))
                        .dimensions(panelX + INSET + toggleWidth + SPACING, yGlint, toggleWidth, BUTTON_H)
                        .build()
        );

        horseBtn = addDrawableChild(
                ButtonWidget.builder(getHorseArmorText(), b -> toggleHorse(b))
                        .dimensions(panelX + INSET, yHorse, PANEL_W - 2 * INSET, BUTTON_H)
                        .build()
        );

        doneBtn = addDrawableChild(
                ButtonWidget.builder(Text.translatable("gui.done"), b ->
                                MinecraftClient.getInstance().setScreen(parent))
                        .dimensions(panelX + INSET, yDone, PANEL_W - 2 * INSET, BUTTON_H)
                        .build()
        );
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // disable Minecraft's default blur
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // dark tint full-screen
        ctx.fillGradient(0, 0, width, height, 0xC0101010, 0xD0101010);

        // panel background and border
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, 0xFF111111);
        int border = 0xFFFFFFFF;
        ctx.fill(panelX - 1, panelY - 1, panelX + PANEL_W + 1, panelY, border);
        ctx.fill(panelX - 1, panelY + PANEL_H, panelX + PANEL_W + 1, panelY + PANEL_H + 1, border);
        ctx.fill(panelX - 1, panelY, panelX, panelY + PANEL_H, border);
        ctx.fill(panelX + PANEL_W, panelY, panelX + PANEL_W + 1, panelY + PANEL_H, border);

        // titles & separators
        ctx.drawCenteredTextWithShadow(textRenderer, title.asOrderedText(),
                panelX + PANEL_W / 2, yTitle, 0xFFFFFFFF);

        ctx.fill(panelX + INSET, ySep1, panelX + PANEL_W - INSET, ySep1 + SEP_THICKNESS, border);
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.hiddenarmour.category.main"),
                panelX + PANEL_W / 2, ySub1, border);

        ctx.fill(panelX + INSET, ySep2, panelX + PANEL_W - INSET, ySep2 + SEP_THICKNESS, border);
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.hiddenarmour.category.horse"),
                panelX + PANEL_W / 2, ySub2, border);

        ctx.fill(panelX + INSET, ySep3, panelX + PANEL_W - INSET, ySep3 + SEP_THICKNESS, border);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private MutableText styledText(String key, boolean flag) {
        String full = Text.translatable(key + (flag ? "enabled" : "disabled")).getString();
        String[] parts = full.split(": ", 2);
        String prefix = parts[0] + ": ";
        String suffix = parts.length > 1 ? parts[1] : "";
        return Text.literal(prefix)
                .append(Text.literal(suffix)
                        .formatted(flag ? Formatting.GREEN : Formatting.RED));
    }

    private Text getArmourText()    { return styledText("button.hiddenarmour.toggleArmour.", HiddenArmourConfig.get().hideArmour); }
    private Text getElytraText()    { return styledText("button.hiddenarmour.toggleElytra.", HiddenArmourConfig.get().includeElytra); }
    private Text getGlintText() { return styledText("button.hiddenarmour.toggleGlint.", HiddenArmourConfig.get().hideEnchantmentGlint); }
    private Text getNameTagText()   { return styledText("button.hiddenarmour.toggleNameTags.", HiddenArmourConfig.get().hideNameTags); }
    private Text getHorseArmorText(){ return styledText("button.hiddenarmour.toggleHorseArmor.", HiddenArmourConfig.get().hideHorseArmor); }

    private void toggleArmour(ButtonWidget b) {
        var c = HiddenArmourConfig.get(); c.hideArmour = !c.hideArmour; HiddenArmourConfig.save(); b.setMessage(getArmourText());
    }
    private void toggleElytra(ButtonWidget b) {
        var c = HiddenArmourConfig.get(); c.includeElytra = !c.includeElytra; HiddenArmourConfig.save(); b.setMessage(getElytraText());
    }
    private void toggleGlint(ButtonWidget b) {
        var c = HiddenArmourConfig.get(); c.hideEnchantmentGlint = !c.hideEnchantmentGlint; HiddenArmourConfig.save(); b.setMessage(getGlintText());
    }
    private void toggleNameTag(ButtonWidget b) {
        var c = HiddenArmourConfig.get(); c.hideNameTags = !c.hideNameTags; HiddenArmourConfig.save(); b.setMessage(getNameTagText());
    }
    private void toggleHorse(ButtonWidget b) {
        var c = HiddenArmourConfig.get(); c.hideHorseArmor = !c.hideHorseArmor; HiddenArmourConfig.save(); b.setMessage(getHorseArmorText());
    }
}
