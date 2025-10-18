package org.kr1v.malilearning.client.mixin.malilib;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigResettable;
import fi.dy.masa.malilib.config.IConfigStringList;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import net.minecraft.util.Pair;
import org.kr1v.malilearning.client.malilib.config.IConfigTable;
import org.kr1v.malilearning.client.malilib.config.options.ConfigTable;
import org.kr1v.malilearning.client.malilib.gui.button.ConfigButtonTable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// magic!
@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class WidgetConfigOptionMixin {
    @Unique
    protected ImmutableList<List<Object>> initialTable;

    @Shadow
    @Final
    protected IKeybindConfigGui host;

    @Shadow
    protected abstract void addConfigButtonEntry(int xReset, int yReset, IConfigResettable config, ButtonBase optionButton);

    @Definition(id = "config", local = @Local(type = IConfigBase.class))
    @Definition(id = "IConfigStringList", type = IConfigStringList.class)
    @Expression("config instanceof IConfigStringList")
    @Inject(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void initTable(int x, int y, int width, int height, int labelWidth, int configWidth, GuiConfigsBase.ConfigOptionWrapper wrapper, int listIndex, IKeybindConfigGui host, WidgetListConfigOptionsBase<?, ?> parent, CallbackInfo ci, @Local IConfigBase config) {
        if (config instanceof IConfigTable) {
            this.initialTable = ImmutableList.copyOf(((IConfigTable) config).getTable());
        }
    }

    @Definition(id = "config", local = @Local(type = IConfigBase.class, argsOnly = true))
    @Definition(id = "ConfigBooleanHotkeyed", type = ConfigBooleanHotkeyed.class)
    @Expression("config instanceof ConfigBooleanHotkeyed")
    @Inject(method = "addConfigOption", at = @At("MIXINEXTRAS:EXPRESSION"), cancellable = true)
    private void addConfigOptionTable(int x, int y, float zLevel, int labelWidth, int configWidth, IConfigBase config, CallbackInfo ci, @Local(name = "configHeight") int configHeight) {
        if (config instanceof ConfigTable) {
            ConfigButtonTable optionButton = new ConfigButtonTable(x, y, configWidth, configHeight, (IConfigTable) config, this.host, this.host.getDialogHandler());
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
            ci.cancel();
        }
    }

    @Definition(id = "config", local = @Local(type = IConfigBase.class))
    @Definition(id = "IConfigStringList", type = IConfigStringList.class)
    @Expression("config instanceof IConfigStringList")
    @Inject(method = "wasConfigModified", at = @At("MIXINEXTRAS:EXPRESSION"), cancellable = true)
    private void wasConfigModifiedTable(CallbackInfoReturnable<Boolean> cir, @Local IConfigBase config) {
        if (this.initialTable != null && config instanceof IConfigTable) {
            cir.setReturnValue(!this.initialTable.equals(((IConfigTable) config).getTable()));
        }
    }
}
