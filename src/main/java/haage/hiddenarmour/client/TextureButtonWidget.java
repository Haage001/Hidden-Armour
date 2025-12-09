// src/main/java/haage/hiddenarmour/client/TextureButtonWidget.java
package haage.hiddenarmour.client;

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
                               boolean initialState, Runnable onPress) {
        super(x, y, width, height, net.minecraft.text.Text.literal(""), 
              button -> onPress.run(), 
              DEFAULT_NARRATION_SUPPLIER);
        this.itemStack = getItemStackFromTexturePath(enabledTexture.getPath());
        this.isEnabled = initialState;
    }
    
    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw the item stack centered
        int x = this.getX();
        int y = this.getY();
        
        // Draw frame around the button
        int frameColor = this.isHovered() ? 0xFFFFFFFF : 0xFF888888;
        context.fill(x, y, x + this.width, y + 1, frameColor); // top
        context.fill(x, y + this.height - 1, x + this.width, y + this.height, frameColor); // bottom
        context.fill(x, y, x + 1, y + this.height, frameColor); // left
        context.fill(x + this.width - 1, y, x + this.width, y + this.height, frameColor); // right
        
        int iconSize = 16;
        int iconX = x + (this.width - iconSize) / 2;
        int iconY = y + (this.height - iconSize) / 2;
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
}
