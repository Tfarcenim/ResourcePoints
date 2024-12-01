package tfar.resourcepoints.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.menu.DepositTerminalMenu;

public class DepositTerminalScreen extends AbstractContainerScreen<DepositTerminalMenu> {

    private static final ResourceLocation CONTAINER_LOCATION = ResourcePoints.id("textures/gui/deposit_terminal.png");


    public DepositTerminalScreen(DepositTerminalMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        this.renderBackground($$0);
        super.render($$0, $$1, $$2, $$3);
        this.renderTooltip($$0, $$1, $$2);
    }

    @Override
    protected void renderBg(GuiGraphics $$0, float $$1, int $$2, int $$3) {
        int $$4 = (this.width - this.imageWidth) / 2;
        int $$5 = (this.height - this.imageHeight) / 2;
        $$0.blit(CONTAINER_LOCATION, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);
    }
}
