package org.kr1v.malilearning.client.malilib.gui;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGui;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.kr1v.malilearning.client.malilib.config.IConfigTable;
import org.kr1v.malilearning.client.malilib.gui.widgets.WidgetListTableEdit;
import org.kr1v.malilearning.client.malilib.gui.widgets.WidgetTableEditEntry;

import java.util.List;

//       TODO: optional display of entry number
public class GuiMapEdit extends GuiListBase<List<Object>, WidgetTableEditEntry, WidgetListTableEdit> {
    protected final IConfigTable config;
    protected final IConfigGui configGui;
    protected int dialogWidth;
    protected int dialogHeight;
    protected int dialogLeft;
    protected int dialogTop;
    @Nullable
    protected final IDialogHandler dialogHandler;

    public GuiMapEdit(IConfigTable config, IConfigGui configGui, @Nullable IDialogHandler dialogHandler, Screen parent) {
        super(0, 0);

        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.title = "Edit table for '" + config.getName() + "'";

        if (this.dialogHandler == null) {
            this.setParent(parent);
        }
    }

    protected void setWidthAndHeight() {
        this.dialogWidth = 400;
        this.dialogHeight = GuiUtils.getScaledWindowHeight() - 90;
    }

    protected void centerOnScreen() {
        if (this.getParent() != null) {
            this.dialogLeft = this.getParent().width / 2 - this.dialogWidth / 2;
            this.dialogTop = this.getParent().height / 2 - this.dialogHeight / 2;
        } else {
            this.dialogLeft = 20;
            this.dialogTop = 20;
        }
    }

    @Override
    public void initGui() {
        this.setWidthAndHeight();
        this.centerOnScreen();
        this.reCreateListWidget();

        super.initGui();
    }

    public IConfigTable getConfig() {
        return this.config;
    }

    @Override
    protected int getBrowserWidth() {
        return this.dialogWidth - 14;
    }

    @Override
    protected int getBrowserHeight() {
        return this.dialogHeight - 40;
    }

    @Override
    protected WidgetListTableEdit createListWidget(int listX, int listY) {
        return new WidgetListTableEdit(this.dialogLeft + 10, this.dialogTop + 30, this.getBrowserWidth(), this.getBrowserHeight(), this.dialogWidth - 100, this);
    }

    @Override
    public void removed() {
        if (this.getListWidget().wereConfigsModified()) {
            this.getListWidget().applyPendingModifications();
            ConfigManager.getInstance().onConfigsChanged(this.configGui.getModId());
        }

        super.removed();
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        if (this.getParent() != null) {
            this.getParent().render(drawContext, mouseX, mouseY, partialTicks);
        }

        super.render(drawContext, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawScreenBackground(DrawContext drawContext, int mouseX, int mouseY) {
        RenderUtils.drawOutlinedBox(drawContext, this.dialogLeft, this.dialogTop, this.dialogWidth, this.dialogHeight, 0xFF000000, COLOR_HORIZONTAL_BAR);
    }

    @Override
    protected void drawTitle(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        this.drawStringWithShadow(drawContext, this.title, this.dialogLeft + 10, this.dialogTop + 6, COLOR_WHITE);
        for (int i = 0; i < this.config.getLabels().size(); i++) {
            String str = this.config.getLabels().get(i);
            this.drawStringWithShadow(drawContext, str, dialogLeft + 33 + i * ((dialogWidth - 190) / this.config.getLabels().size()) + 2, this.dialogTop + 18, COLOR_WHITE);
        }
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KeyCodes.KEY_ESCAPE && this.dialogHandler != null) {
            this.dialogHandler.closeDialog();
            return true;
        } else {
            return super.onKeyTyped(keyCode, scanCode, modifiers);
        }
    }
}
