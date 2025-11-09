// src/main/java/haage/hiddenarmour/client/TextureButtonWidget.java
package haage.hiddenarmour.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextureButtonWidget extends ButtonWidget {
    private final ItemStack itemStack;
    private boolean isEnabled;

    public TextureButtonWidget(int x, int y, int width, int height, 
                               Identifier enabledTexture, Identifier disabledTexture,
                               boolean initialState, PressAction onPress) {
        super(x, y, width, height, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        // We'll use the enabledTexture path to determine which armor piece this is
        this.itemStack = getItemStackFromTexturePath(enabledTexture.getPath());
        this.isEnabled = initialState;
    }
    
    private ItemStack getItemStackFromTexturePath(String path) {
        // Parse the texture path to determine which item to show
        if (path.contains("helmet")) {
            return new ItemStack(net.minecraft.item.Items.DIAMOND_HELMET);
        } else if (path.contains("chestplate")) {
            return new ItemStack(net.minecraft.item.Items.DIAMOND_CHESTPLATE);
        } else if (path.contains("leggings")) {
            return new ItemStack(net.minecraft.item.Items.DIAMOND_LEGGINGS);
        } else if (path.contains("boots")) {
            return new ItemStack(net.minecraft.item.Items.DIAMOND_BOOTS);
        } else if (path.contains("elyta") || path.contains("elytra")) {
            return new ItemStack(net.minecraft.item.Items.ELYTRA);
        }
        // Default fallback
        return new ItemStack(net.minecraft.item.Items.BARRIER);
    }

    public void setState(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw solid button background like the old emoji buttons
        int bgColor = this.isHovered() ? 0xE0404040 : 0xC0303030;
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
        
        // Draw border
        int borderColor = this.isHovered() ? 0xFFFFFFFF : 0xFF8B8B8B;
        // Top border
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, borderColor);
        // Bottom border
        context.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, borderColor);
        // Left border
        context.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, borderColor);
        // Right border
        context.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, borderColor);
        
        // Calculate centered position for 16x16 item icon
        int iconSize = 16;
        int iconX = this.getX() + (this.width - iconSize) / 2;
        int iconY = this.getY() + (this.height - iconSize) / 2;
        
        // Draw the item stack
        context.drawItem(itemStack, iconX, iconY);
        
        // Draw checkmark or X next to the item
        var client = net.minecraft.client.MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        
        String symbol = isEnabled ? "✔" : "❌";
        int symbolColor = isEnabled ? 0xFF00FF00 : 0xFFFF0000;
        
        // Position the symbol to the bottom-right of the item
        int symbolX = iconX + iconSize - 2;
        int symbolY = iconY + iconSize - 8;
        
        // Draw small background for better visibility
        int symbolWidth = textRenderer.getWidth(symbol);
        context.fill(symbolX - 1, symbolY - 1, symbolX + symbolWidth + 1, symbolY + 9, 0xAA000000);
        
        // Draw the symbol
        context.drawText(textRenderer, symbol, symbolX, symbolY, symbolColor, false);
    }
}
