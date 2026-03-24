package haage.hiddenarmour.client;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class HiddenArmourScreen extends Screen {
    private static final int PANEL_W = 210;
    private static final int PANEL_H = 232;
    private static final int INSET = 8;
    private static final int SPACING = 6;
    private static final int BUTTON_H = 20;
    private static final int ICON_BUTTON_W = 30;
    private static final int ICON_BUTTON_H = 22;

    private final Screen parent;

    private Button armourBtn;
    private Button glintBtn;
    private Button nameTagBtn;
    private Button horseBtn;
    private Button wolfBtn;
    private Button nautilusBtn;
    private Button helmetIconBtn;
    private Button chestIconBtn;
    private Button leggingsIconBtn;
    private Button bootsIconBtn;
    private Button elytraIconBtn;

    private int panelX;
    private int panelY;

    private int iconRowY;
    private int iconStartX;
    private int mobSectionTitleY;
    private int playerSectionSeparatorY;
    private int bottomSectionSeparatorY;

    public HiddenArmourScreen(Screen parent) {
        super(Component.translatable("screen.hiddenarmour.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();

        panelX = (width - PANEL_W) / 2;
        panelY = (height - PANEL_H) / 2;
        int buttonWidth = (PANEL_W - (INSET * 2) - SPACING) / 2;
        int fullWidth = PANEL_W - (INSET * 2);
        int leftX = panelX + INSET;
        int rightX = leftX + buttonWidth + SPACING;
        int y = panelY + 36;

        armourBtn = addRenderableWidget(Button.builder(getArmourText(), button -> {
            toggleArmour();
            refreshButtonLabels();
        }).bounds(leftX, y, fullWidth, BUTTON_H).build());

        y += BUTTON_H + SPACING;
        int iconSpacing = 6;
        iconStartX = panelX + (PANEL_W - ((ICON_BUTTON_W * 5) + (iconSpacing * 4))) / 2;
        iconRowY = y;

        helmetIconBtn = addRenderableWidget(Button.builder(Component.empty(), button -> {
            toggleHelmet();
            refreshButtonLabels();
        }).bounds(iconStartX, y, ICON_BUTTON_W, ICON_BUTTON_H).build());

        chestIconBtn = addRenderableWidget(Button.builder(Component.empty(), button -> {
            toggleChestplate();
            refreshButtonLabels();
        }).bounds(iconStartX + (ICON_BUTTON_W + iconSpacing), y, ICON_BUTTON_W, ICON_BUTTON_H).build());

        leggingsIconBtn = addRenderableWidget(Button.builder(Component.empty(), button -> {
            toggleLeggings();
            refreshButtonLabels();
        }).bounds(iconStartX + ((ICON_BUTTON_W + iconSpacing) * 2), y, ICON_BUTTON_W, ICON_BUTTON_H).build());

        bootsIconBtn = addRenderableWidget(Button.builder(Component.empty(), button -> {
            toggleBoots();
            refreshButtonLabels();
        }).bounds(iconStartX + ((ICON_BUTTON_W + iconSpacing) * 3), y, ICON_BUTTON_W, ICON_BUTTON_H).build());

        elytraIconBtn = addRenderableWidget(Button.builder(Component.empty(), button -> {
            toggleElytra();
            refreshButtonLabels();
        }).bounds(iconStartX + ((ICON_BUTTON_W + iconSpacing) * 4), y, ICON_BUTTON_W, ICON_BUTTON_H).build());

        y += ICON_BUTTON_H + SPACING;
        glintBtn = addRenderableWidget(Button.builder(getGlintText(), button -> {
            toggleGlint();
            refreshButtonLabels();
        }).bounds(leftX, y, buttonWidth, BUTTON_H).build());

        nameTagBtn = addRenderableWidget(Button.builder(getNameTagText(), button -> {
            toggleNameTag();
            refreshButtonLabels();
        }).bounds(rightX, y, buttonWidth, BUTTON_H).build());

        y += BUTTON_H + SPACING;
        playerSectionSeparatorY = y;
        mobSectionTitleY = playerSectionSeparatorY + SPACING;
        y = mobSectionTitleY + font.lineHeight + SPACING;

        horseBtn = addRenderableWidget(Button.builder(getHorseText(), button -> {
            toggleHorse();
            refreshButtonLabels();
        }).bounds(leftX, y, buttonWidth, BUTTON_H).build());

        wolfBtn = addRenderableWidget(Button.builder(getWolfText(), button -> {
            toggleWolf();
            refreshButtonLabels();
        }).bounds(rightX, y, buttonWidth, BUTTON_H).build());

        y += BUTTON_H + SPACING;
        nautilusBtn = addRenderableWidget(Button.builder(getNautilusText(), button -> {
            toggleNautilus();
            refreshButtonLabels();
        }).bounds(leftX, y, buttonWidth, BUTTON_H).build());

        y += BUTTON_H + SPACING;
        bottomSectionSeparatorY = y;
        y += SPACING;

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> minecraft.setScreen(parent))
                .bounds(leftX, y, fullWidth, BUTTON_H)
                .build());

        refreshTooltips();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        int borderColor = 0xFFFFFFFF;

        guiGraphics.fillGradient(0, 0, width, height, 0xC0101010, 0xD0101010);
        guiGraphics.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, 0xFF111111);
        guiGraphics.outline(panelX - 1, panelY - 1, PANEL_W + 2, PANEL_H + 2, borderColor);

        int centerX = panelX + (PANEL_W / 2);
        int left = panelX + INSET;
        int right = panelX + PANEL_W - INSET;
        int playerTitleY = panelY + 24;
        guiGraphics.centeredText(font, title, centerX, panelY + 8, borderColor);
        guiGraphics.horizontalLine(left, right, panelY + 20, borderColor);
        guiGraphics.centeredText(font, Component.literal("Player Render"), centerX, playerTitleY, 0xFFDADADA);
        guiGraphics.horizontalLine(left, right, playerSectionSeparatorY, borderColor);
        guiGraphics.centeredText(font, Component.literal("Mob Renders"), centerX, mobSectionTitleY, 0xFFDADADA);
        guiGraphics.horizontalLine(left, right, bottomSectionSeparatorY, borderColor);

        super.extractRenderState(guiGraphics, mouseX, mouseY, delta);

        // Render icon overlays after widgets so button backgrounds do not cover them.
        renderToggleItem(guiGraphics, iconStartX, iconRowY, new ItemStack(Items.DIAMOND_HELMET), HiddenArmourConfig.get().isShowHelmet());
        renderToggleItem(guiGraphics, iconStartX + (ICON_BUTTON_W + 6), iconRowY, new ItemStack(Items.DIAMOND_CHESTPLATE), HiddenArmourConfig.get().isShowChestplate());
        renderToggleItem(guiGraphics, iconStartX + ((ICON_BUTTON_W + 6) * 2), iconRowY, new ItemStack(Items.DIAMOND_LEGGINGS), HiddenArmourConfig.get().isShowLeggings());
        renderToggleItem(guiGraphics, iconStartX + ((ICON_BUTTON_W + 6) * 3), iconRowY, new ItemStack(Items.DIAMOND_BOOTS), HiddenArmourConfig.get().isShowBoots());
        renderToggleItem(guiGraphics, iconStartX + ((ICON_BUTTON_W + 6) * 4), iconRowY, new ItemStack(Items.ELYTRA), !HiddenArmourConfig.get().includeElytra);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void refreshButtonLabels() {
        armourBtn.setMessage(getArmourText());
        glintBtn.setMessage(getGlintText());
        nameTagBtn.setMessage(getNameTagText());
        horseBtn.setMessage(getHorseText());
        wolfBtn.setMessage(getWolfText());
        nautilusBtn.setMessage(getNautilusText());
        helmetIconBtn.setMessage(Component.empty());
        chestIconBtn.setMessage(Component.empty());
        leggingsIconBtn.setMessage(Component.empty());
        bootsIconBtn.setMessage(Component.empty());
        elytraIconBtn.setMessage(Component.empty());

        refreshTooltips();
    }

    private Component getArmourText() {
        return hiddenShownText("Armour", HiddenArmourConfig.get().hideArmour);
    }

    private Component getGlintText() {
        return hiddenShownText("Glint", HiddenArmourConfig.get().hideEnchantmentGlint);
    }

    private Component getNameTagText() {
        return hiddenShownText("Nametags", HiddenArmourConfig.get().hideNameTags);
    }

    private Component getHorseText() {
        return hiddenShownText("Horse", HiddenArmourConfig.get().hideHorseArmor);
    }

    private Component getWolfText() {
        return hiddenShownText("Wolf", HiddenArmourConfig.get().hideWolfArmor);
    }

    private Component getNautilusText() {
        return hiddenShownText("Nautilus", HiddenArmourConfig.get().hideNautilusArmor);
    }

    private Component getHelmetText() {
        return shownHiddenText("Helmet", HiddenArmourConfig.get().isShowHelmet());
    }

    private Component getChestplateText() {
        return shownHiddenText("Chestplate", HiddenArmourConfig.get().isShowChestplate());
    }

    private Component getLeggingsText() {
        return shownHiddenText("Leggings", HiddenArmourConfig.get().isShowLeggings());
    }

    private Component getBootsText() {
        return shownHiddenText("Boots", HiddenArmourConfig.get().isShowBoots());
    }

    private Component getElytraText() {
        return shownHiddenText("Elytra", !HiddenArmourConfig.get().includeElytra);
    }

    private Component shownHiddenText(String label, boolean shown) {
        MutableComponent state = Component.literal(shown ? "Shown" : "Hidden")
                .withStyle(shown ? ChatFormatting.GREEN : ChatFormatting.RED);
        return Component.literal(label + ": ").append(state);
    }

    private Component hiddenShownText(String label, boolean hidden) {
        MutableComponent state = Component.literal(hidden ? "Hidden" : "Shown")
                .withStyle(hidden ? ChatFormatting.GREEN : ChatFormatting.RED);
        return Component.literal(label + ": ").append(state);
    }

    private void renderToggleItem(GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack stack, boolean enabled) {
        // Draw a dark tile and border so icon buttons match the classic per-piece style.
        guiGraphics.fill(x + 1, y + 1, x + ICON_BUTTON_W - 1, y + ICON_BUTTON_H - 1, 0xFF0B0B0B);
        guiGraphics.outline(x, y, ICON_BUTTON_W, ICON_BUTTON_H, 0xFFA0A0A0);

        int itemOffsetX = (ICON_BUTTON_W - 16) / 2;
        int itemOffsetY = (ICON_BUTTON_H - 16) / 2;
        guiGraphics.item(stack, x + itemOffsetX, y + itemOffsetY);

        String marker = enabled ? "✔" : "X";
        int color = enabled ? 0xFF2EEB4F : 0xFFFF4242;
        int markerX = x + ICON_BUTTON_W - 8;
        int markerY = y + ICON_BUTTON_H - 9;
        guiGraphics.text(font, marker, markerX, markerY, color, false);
        if (enabled) {
            // Draw twice for a slightly bolder checkmark.
            guiGraphics.text(font, marker, markerX + 1, markerY, color, false);
        }
    }

    private void refreshTooltips() {
        armourBtn.setTooltip(Tooltip.create(Component.literal("Hide/show all player armour")));
        glintBtn.setTooltip(Tooltip.create(Component.literal("Hide/show enchantment glint")));
        nameTagBtn.setTooltip(Tooltip.create(Component.literal("Hide/show player nametags")));
        horseBtn.setTooltip(Tooltip.create(Component.literal("Hide/show horse equipment")));
        wolfBtn.setTooltip(Tooltip.create(Component.literal("Hide/show wolf armour")));
        nautilusBtn.setTooltip(Tooltip.create(Component.literal("Hide/show nautilus equipment")));

        helmetIconBtn.setTooltip(Tooltip.create(shownHiddenText("Helmet", HiddenArmourConfig.get().isShowHelmet())));
        chestIconBtn.setTooltip(Tooltip.create(shownHiddenText("Chestplate", HiddenArmourConfig.get().isShowChestplate())));
        leggingsIconBtn.setTooltip(Tooltip.create(shownHiddenText("Leggings", HiddenArmourConfig.get().isShowLeggings())));
        bootsIconBtn.setTooltip(Tooltip.create(shownHiddenText("Boots", HiddenArmourConfig.get().isShowBoots())));
        elytraIconBtn.setTooltip(Tooltip.create(shownHiddenText("Elytra", !HiddenArmourConfig.get().includeElytra)));
    }

    private void toggleArmour() {
        var config = HiddenArmourConfig.get();
        config.hideArmour = !config.hideArmour;
        HiddenArmourConfig.save();
    }

    private void toggleGlint() {
        var config = HiddenArmourConfig.get();
        config.hideEnchantmentGlint = !config.hideEnchantmentGlint;
        HiddenArmourConfig.save();
    }

    private void toggleNameTag() {
        var config = HiddenArmourConfig.get();
        config.hideNameTags = !config.hideNameTags;
        HiddenArmourConfig.save();
    }

    private void toggleHorse() {
        var config = HiddenArmourConfig.get();
        config.hideHorseArmor = !config.hideHorseArmor;
        HiddenArmourConfig.save();
    }

    private void toggleWolf() {
        var config = HiddenArmourConfig.get();
        config.hideWolfArmor = !config.hideWolfArmor;
        HiddenArmourConfig.save();
    }

    private void toggleNautilus() {
        var config = HiddenArmourConfig.get();
        config.hideNautilusArmor = !config.hideNautilusArmor;
        HiddenArmourConfig.save();
    }

    private void toggleHelmet() {
        var config = HiddenArmourConfig.get();
        config.setShowHelmet(!config.isShowHelmet());
        HiddenArmourConfig.save();
    }

    private void toggleChestplate() {
        var config = HiddenArmourConfig.get();
        config.setShowChestplate(!config.isShowChestplate());
        HiddenArmourConfig.save();
    }

    private void toggleLeggings() {
        var config = HiddenArmourConfig.get();
        config.setShowLeggings(!config.isShowLeggings());
        HiddenArmourConfig.save();
    }

    private void toggleBoots() {
        var config = HiddenArmourConfig.get();
        config.setShowBoots(!config.isShowBoots());
        HiddenArmourConfig.save();
    }

    private void toggleElytra() {
        var config = HiddenArmourConfig.get();
        config.includeElytra = !config.includeElytra;
        HiddenArmourConfig.save();
    }
}
