package org.kr1v.malilearning.client.malilib.gui.button;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IConfigGui;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import org.jetbrains.annotations.Nullable;
import org.kr1v.malilearning.client.malilib.config.IConfigTable;
import org.kr1v.malilearning.client.malilib.gui.GuiMapEdit;

import java.util.List;

public class ConfigButtonTable extends ButtonGeneric {
    private final IConfigTable config;
    private final IConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonTable(int x, int y, int width, int height, IConfigTable config, IConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
        super(x, y, width, height, "");

        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;

        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClickedImpl(mouseX, mouseY, mouseButton);

        if (this.dialogHandler != null) {
            this.dialogHandler.openDialog(new GuiMapEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiMapEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        if (this.config.getDisplayString() != null) {
            this.displayString = this.config.getDisplayString();
            return;
        }
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        boolean addDivider = false;
        for (List<Object> entry : this.config.getTable()) {
            if (addDivider) {
                sb.append("; ");
            }
            for (Object entryPart : entry) {
                sb.append(entryPart.toString());
                sb.append(", ");
            }
            addDivider = true;
        }
        sb.append("}");

        this.displayString = sb.toString();
    }
}
