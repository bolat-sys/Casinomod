package com.bolat.casinomod.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CasinoScreen extends AbstractContainerScreen<CasinoMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
    public CasinoScreen(CasinoMenu menu, Inventory inventory, Component title) { super(menu, inventory, title); this.imageWidth = 176; this.imageHeight = 166; }
    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2, y = (height - imageHeight) / 2;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF24152E);
        graphics.fill(x + 4, y + 4, x + imageWidth - 4, y + 76, 0xFF49305A);
        graphics.fill(x + 35, y + 27, x + 53, y + 45, 0xFF17101E);
        graphics.fill(x + 107, y + 27, x + 125, y + 45, 0xFF17101E);
        graphics.drawString(font, "STAKE", x + 39, y + 13, 0xFFFFD45C, false);
        graphics.drawString(font, "PRIZE", x + 111, y + 13, 0xFF71FF9A, false);
        graphics.fill(x + 4, y + 80, x + imageWidth - 4, y + imageHeight - 4, 0xFF30243A);
    }
    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "Your inventory", 8, 72, 0xFFFFFFFF, false);
    }
}
