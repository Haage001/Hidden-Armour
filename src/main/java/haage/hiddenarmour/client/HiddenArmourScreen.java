// src/main/java/haage/hiddenarmour/client/HiddenArmourScreen.java
package haage.hiddenarmour.client;

import java.util.List;

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
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HiddenArmourScreen extends Screen {
    private static final int PANEL_W       = 220;
    private static final int PANEL_H       = 225;
    private static final int INSET         = 10;
    private static final int SPACING       = 5;
    private static final int SEP_THICKNESS = 1;
    private static final int BUTTON_H      = 20;
    private static final int EMOJI_BTN_W   = 35;

    // Texture identifiers for armor piece buttons (direct PNG references)
    private static final Identifier HELMET_ENABLED = Identifier.of("hiddenarmour", "textures/gui/icons/helmet.png");
    private static final Identifier HELMET_DISABLED = Identifier.of("hiddenarmour", "textures/gui/icons/helmet_disabled.png");
    private static final Identifier CHEST_ENABLED = Identifier.of("hiddenarmour", "textures/gui/icons/chestplate.png");
    private static final Identifier CHEST_DISABLED = Identifier.of("hiddenarmour", "textures/gui/icons/chestplate_disabled.png");
    private static final Identifier LEGGINGS_ENABLED = Identifier.of("hiddenarmour", "textures/gui/icons/leggings.png");
    private static final Identifier LEGGINGS_DISABLED = Identifier.of("hiddenarmour", "textures/gui/icons/leggings_disabled.png");
    private static final Identifier BOOTS_ENABLED = Identifier.of("hiddenarmour", "textures/gui/icons/boots.png");
    private static final Identifier BOOTS_DISABLED = Identifier.of("hiddenarmour", "textures/gui/icons/boots_disabled.png");
    private static final Identifier ELYTRA_ENABLED = Identifier.of("hiddenarmour", "textures/gui/icons/elyta.png");
    private static final Identifier ELYTRA_DISABLED = Identifier.of("hiddenarmour", "textures/gui/icons/elyta_disabed.png");

    private final Screen parent;
    private int panelX, panelY;
    private int yTitle, ySep1, ySub1, yArmour, yIcon,
            yGlint, ySep2, ySub2, yHorse, yWolf, ySep3, yDone;
    private int emojiX, emojiGap;

    private ButtonWidget armourBtn, glintBtn, nameTagBtn, horseBtn, wolfBtn, nautilusBtn;
    private TextureButtonWidget helmetBtn, chestBtn, leggingsBtn, bootsBtn, elytraBtn;

    public HiddenArmourScreen(Screen parent) {
        super(Text.translatable("screen.hiddenarmour.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        panelX = (width  - PANEL_W) / 2;
        panelY = (height - PANEL_H) / 2;

        TextRenderer tr = textRenderer;
        yTitle  = panelY + 8;
        ySep1   = yTitle + tr.fontHeight + SPACING;
        ySub1   = ySep1 + SEP_THICKNESS + SPACING;
        yArmour = ySub1 + tr.fontHeight + SPACING;
        yIcon   = yArmour + BUTTON_H + SPACING;
        yGlint  = yIcon   + BUTTON_H + SPACING;

        ySep2   = yGlint + BUTTON_H + SPACING;
        ySub2   = ySep2 + SEP_THICKNESS + SPACING;

        yHorse  = ySub2 + tr.fontHeight + SPACING;
        yWolf   = yHorse + BUTTON_H + SPACING;
        ySep3   = yWolf + BUTTON_H + SPACING;
        yDone   = ySep3   + SEP_THICKNESS + SPACING;

        // ─── MAIN TOGGLES ───────────────────────────────────────────────────────
        armourBtn = addDrawableChild(
                ButtonWidget.builder(getArmourText(), b -> {
                            toggleArmour();
                            armourBtn.setMessage(getArmourText());
                        })
                        .dimensions(panelX + INSET, yArmour, PANEL_W - 2*INSET, BUTTON_H)
                        .build()
        );

        // Centered Glint & Nametag Buttons
        int btnWidth   = (PANEL_W - 3*INSET) / 2 - SPACING + 5;
        int groupWidth = btnWidth * 2 + SPACING;
        int startX     = panelX + (PANEL_W - groupWidth) / 2;

        glintBtn = addDrawableChild(
                ButtonWidget.builder(getGlintText(), b -> {
                            toggleGlint();
                            glintBtn.setMessage(getGlintText());
                        })
                        .dimensions(startX, yGlint, btnWidth, BUTTON_H)
                        .build()
        );
        nameTagBtn = addDrawableChild(
                ButtonWidget.builder(getNameTagText(), b -> {
                            toggleNameTag();
                            nameTagBtn.setMessage(getNameTagText());
                        })
                        .dimensions(startX + btnWidth + SPACING, yGlint, btnWidth, BUTTON_H)
                        .build()
        );

        // Mob render buttons in 2-1 grid layout (Horse and Wolf on top row, Nautilus below)
        int mobBtnWidth = (PANEL_W - 3*INSET) / 2 - SPACING + 5;
        
        horseBtn = addDrawableChild(
                ButtonWidget.builder(getHorseText(), b -> {
                            toggleHorse();
                            horseBtn.setMessage(getHorseText());
                        })
                        .dimensions(panelX + INSET, yHorse, mobBtnWidth, BUTTON_H)
                        .build()
        );
        wolfBtn = addDrawableChild(
                ButtonWidget.builder(getWolfText(), b -> {
                            toggleWolf();
                            wolfBtn.setMessage(getWolfText());
                        })
                        .dimensions(panelX + INSET + mobBtnWidth + SPACING, yHorse, mobBtnWidth, BUTTON_H)
                        .build()
        );
        nautilusBtn = addDrawableChild(
                ButtonWidget.builder(getNautilusText(), b -> {
                            toggleNautilus();
                            nautilusBtn.setMessage(getNautilusText());
                        })
                        .dimensions(panelX + INSET, yWolf, mobBtnWidth, BUTTON_H)
                        .build()
        );
        addDrawableChild(
                ButtonWidget.builder(Text.translatable("gui.done"),
                                b -> MinecraftClient.getInstance().setScreen(parent))
                        .dimensions(panelX + INSET, yDone, PANEL_W - 2*INSET, BUTTON_H)
                        .build()
        );

        // ─── EMOJI BUTTONS ───────────────────────────────────────────────────────
        int total    = 5;
        int rowWidth = total * EMOJI_BTN_W + (total - 1) * SPACING;
        emojiX      = panelX + (PANEL_W - rowWidth) / 2;
        emojiGap    = EMOJI_BTN_W + SPACING;

        helmetBtn = addDrawableChild(
                new TextureButtonWidget(emojiX + 0*emojiGap, yIcon, EMOJI_BTN_W, BUTTON_H,
                        HELMET_ENABLED, HELMET_DISABLED,
                        !HiddenArmourConfig.get().isShowHelmet(), () -> {
                            toggleHelmet();
                            helmetBtn.setState(!HiddenArmourConfig.get().isShowHelmet());
                        })
        );
        chestBtn = addDrawableChild(
                new TextureButtonWidget(emojiX + 1*emojiGap, yIcon, EMOJI_BTN_W, BUTTON_H,
                        CHEST_ENABLED, CHEST_DISABLED,
                        !HiddenArmourConfig.get().isShowChestplate(), () -> {
                            toggleChestplate();
                            chestBtn.setState(!HiddenArmourConfig.get().isShowChestplate());
                        })
        );
        leggingsBtn = addDrawableChild(
                new TextureButtonWidget(emojiX + 2*emojiGap, yIcon, EMOJI_BTN_W, BUTTON_H,
                        LEGGINGS_ENABLED, LEGGINGS_DISABLED,
                        !HiddenArmourConfig.get().isShowLeggings(), () -> {
                            toggleLeggings();
                            leggingsBtn.setState(!HiddenArmourConfig.get().isShowLeggings());
                        })
        );
        bootsBtn = addDrawableChild(
                new TextureButtonWidget(emojiX + 3*emojiGap, yIcon, EMOJI_BTN_W, BUTTON_H,
                        BOOTS_ENABLED, BOOTS_DISABLED,
                        !HiddenArmourConfig.get().isShowBoots(), () -> {
                            toggleBoots();
                            bootsBtn.setState(!HiddenArmourConfig.get().isShowBoots());
                        })
        );
        elytraBtn = addDrawableChild(
                new TextureButtonWidget(emojiX + 4*emojiGap, yIcon, EMOJI_BTN_W, BUTTON_H,
                        ELYTRA_ENABLED, ELYTRA_DISABLED,
                        HiddenArmourConfig.get().includeElytra, () -> {
                            toggleElytra();
                            elytraBtn.setState(HiddenArmourConfig.get().includeElytra);
                        })
        );
    }

    @Override
    public void renderBackground(DrawContext ctx, int mx, int my, float delta) {
        // no blur
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        // backdrop
        ctx.fillGradient(0, 0, width, height, 0xC0101010, 0xD0101010);

        // panel + border
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, 0xFF111111);
        int border = 0xFFFFFFFF;
        ctx.fill(panelX - 1, panelY - 1, panelX + PANEL_W + 1, panelY, border);
        ctx.fill(panelX - 1, panelY + PANEL_H, panelX + PANEL_W + 1, panelY + PANEL_H + 1, border);
        ctx.fill(panelX - 1, panelY, panelX, panelY + PANEL_H, border);
        ctx.fill(panelX + PANEL_W, panelY, panelX + PANEL_W + 1, panelY + PANEL_H, border);

        // title & first separator
        ctx.drawCenteredTextWithShadow(textRenderer, title.asOrderedText(),
                panelX + PANEL_W/2, yTitle, border);
        ctx.fill(panelX + INSET,
                ySep1,
                panelX + PANEL_W - INSET,
                ySep1 + SEP_THICKNESS,
                border);

        // ``Player Render`` label
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.hiddenarmour.category.main"),
                panelX + PANEL_W/2, ySub1, border);

        // ─── middle separator with extra padding ───────────────────────────────
        ctx.fill(panelX + INSET,
                ySep2,
                panelX + PANEL_W - INSET,
                ySep2 + SEP_THICKNESS,
                border);

        // ``Horse Render`` label
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.hiddenarmour.category.horse"),
                panelX + PANEL_W/2, ySub2, border);

        // bottom separator above Done
        ctx.fill(panelX + INSET,
                ySep3,
                panelX + PANEL_W - INSET,
                ySep3 + SEP_THICKNESS,
                border);

        super.render(ctx, mx, my, delta);

        // optional tooltips…
        if (armourBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide selected armour pieces")), mx, my);
        }
        if (helmetBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Include Helmet in main render")), mx, my);
        }
        if (chestBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Include Chestplate in main render")), mx, my);
        }
        if (leggingsBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Include Leggings in main render")), mx, my);
        }
        if (bootsBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Include Boots in main render")), mx, my);
        }
        if (elytraBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Include Elytra in main render")), mx, my);
        }
        if (glintBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide enchantment glint on items and armor")), mx, my);
        }
        if (nameTagBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide name tags on players and other entities")), mx, my);
        }
        if (horseBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide horse/donkey/mule armour and saddle")), mx, my);
        }
        if (wolfBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide wolf armour")), mx, my);
        }
        if (nautilusBtn.isHovered()) {
            ctx.drawTooltip(textRenderer,
                    List.of(Text.literal("Hide nautilus shell")), mx, my);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // ─── MAIN TOGGLE GETTERS & HANDLERS ───────────────────────────────────────
    private Text getArmourText() {
        boolean hidden = HiddenArmourConfig.get().hideArmour;
        MutableText label = Text.literal("Armour: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleArmour() {
        var c = HiddenArmourConfig.get();
        c.hideArmour = !c.hideArmour;
        HiddenArmourConfig.save();
    }

    private Text getGlintText() {
        boolean hidden = HiddenArmourConfig.get().hideEnchantmentGlint;
        MutableText label = Text.literal("Glint: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleGlint() {
        var c = HiddenArmourConfig.get();
        c.hideEnchantmentGlint = !c.hideEnchantmentGlint;
        HiddenArmourConfig.save();
    }

    private Text getNameTagText() {
        boolean hidden = HiddenArmourConfig.get().hideNameTags;
        MutableText label = Text.literal("Nametags: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleNameTag() {
        var c = HiddenArmourConfig.get();
        c.hideNameTags = !c.hideNameTags;
        HiddenArmourConfig.save();
    }

    private Text getHorseText() {
        boolean hidden = HiddenArmourConfig.get().hideHorseArmor;
        MutableText label = Text.literal("Horse: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleHorse() {
        var c = HiddenArmourConfig.get();
        c.hideHorseArmor = !c.hideHorseArmor;
        HiddenArmourConfig.save();
    }

    private Text getWolfText() {
        boolean hidden = HiddenArmourConfig.get().hideWolfArmor;
        MutableText label = Text.literal("Wolf: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleWolf() {
        var c = HiddenArmourConfig.get();
        c.hideWolfArmor = !c.hideWolfArmor;
        HiddenArmourConfig.save();
    }

    private Text getNautilusText() {
        boolean hidden = HiddenArmourConfig.get().hideNautilusArmor;
        MutableText label = Text.literal("Nautilus: ");
        MutableText value = Text.literal(hidden ? "Hidden" : "Shown")
                .formatted(hidden ? Formatting.GREEN : Formatting.RED);
        return label.append(value);
    }
    private void toggleNautilus() {
        var c = HiddenArmourConfig.get();
        c.hideNautilusArmor = !c.hideNautilusArmor;
        HiddenArmourConfig.save();
    }

    // ─── TOGGLE HANDLERS ─────────────────────────────────────────────────────
    private void toggleHelmet() {
        var c = HiddenArmourConfig.get();
        c.setShowHelmet(!c.isShowHelmet());
        HiddenArmourConfig.save();
    }

    private void toggleChestplate() {
        var c = HiddenArmourConfig.get();
        c.setShowChestplate(!c.isShowChestplate());
        HiddenArmourConfig.save();
    }

    private void toggleLeggings() {
        var c = HiddenArmourConfig.get();
        c.setShowLeggings(!c.isShowLeggings());
        HiddenArmourConfig.save();
    }

    private void toggleBoots() {
        var c = HiddenArmourConfig.get();
        c.setShowBoots(!c.isShowBoots());
        HiddenArmourConfig.save();
    }

    private void toggleElytra() {
        var c = HiddenArmourConfig.get();
        c.includeElytra = !c.includeElytra;
        HiddenArmourConfig.save();
    }
}
