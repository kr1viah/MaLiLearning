package org.kr1v.malilearning.client.malilib.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IConfigTable extends IConfigBase {
    List<List<Object>> getTable();

    ImmutableList<List<Object>> getDefaultTable();

    void setTable(List<List<Object>> newTable);

    void setModified();

    @Nullable String getDisplayString();

    List<Class<?>> getTypes();
}
