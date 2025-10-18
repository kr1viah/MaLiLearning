package org.kr1v.malilearning.client.malilib.config.options;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.options.ConfigBase;
import org.jetbrains.annotations.Nullable;
import org.kr1v.malilearning.client.malilib.config.IConfigTable;

import java.util.*;

public class ConfigTable extends ConfigBase<ConfigTable> implements IConfigTable {
    // Is this necessary?
//    public static final Codec<ConfigTable> CODEC = RecordCodecBuilder.create(
//            inst -> inst.group(
//                    PrimitiveCodec.STRING.fieldOf("name").forGetter(ConfigBase::getName),
//                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("defaultKeys").forGetter(get -> {
//                        List<String> keys = new ArrayList<>();
//                        for (Pair<String, String> entry : get.defaultMap) {
//                            keys.add(entry.getRight());
//                        }
//                        return keys;
//                    }),
//                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("defaultValues").forGetter(get -> {
//                        List<String> values = new ArrayList<>();
//                        for (Pair<String, String> entry : get.defaultMap) {
//                            values.add(entry.getLeft());
//                        }
//                        return values;
//                    }),
//                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("keys").forGetter(get -> {
//                        List<String> keys = new ArrayList<>();
//                        for (Pair<String, String> entry : get.map) {
//                            keys.add(entry.getRight());
//                        }
//                        return keys;
//                    }),
//                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("values").forGetter(get -> {
//                        List<String> values = new ArrayList<>();
//                        for (Pair<String, String> entry : get.map) {
//                            values.add(entry.getLeft());
//                        }
//                        return values;
//                    }),
//                    PrimitiveCodec.STRING.fieldOf("comment").forGetter(get -> get.comment),
//                    PrimitiveCodec.STRING.fieldOf("prettyName").forGetter(get -> get.prettyName),
//                    PrimitiveCodec.STRING.fieldOf("translatedName").forGetter(get -> get.translatedName)
//            ).apply(inst, ConfigTable::new)
//    );

//    private ConfigTable(String name, List<String> defaultKeys, List<String> defaultValues, List<String> keys, List<String> values, String comment, String prettyName, String translatedName) {
//        this(name, getMapFrom2Lists(defaultKeys, defaultValues), comment, prettyName, translatedName);
//        this.map.addAll(getMapFrom2Lists(keys, values));
//    }

    private final ImmutableList<List<Object>> defaultTable;
    private final List<List<Object>> table = new ArrayList<>();
    private final @Nullable String displayString;
    private final ImmutableList<Class<?>> types;

    public ConfigTable(String name, String comment, String prettyName, String translatedName, @Nullable String displayString, List<List<Object>> defaultValue, Class<?>... types) {
        super(checkDefaultValue(defaultValue, types), name, comment, prettyName, translatedName);

        ImmutableList.Builder<Class<?>> ilb = ImmutableList.builder();
        for (Class<?> type : types) {
            ilb.add(type);
        }

        this.types = ilb.build();
        this.displayString = displayString;
        ImmutableList.Builder<List<Object>> ilb2 = ImmutableList.builder();
        for (List<Object> list : defaultValue) {
            ilb2.add(List.copyOf(list));
        }
        this.defaultTable = ilb2.build();
        this.table.addAll(defaultTable);
    }

    private static ConfigType checkDefaultValue(List<List<Object>> defaultValue, Object[] types) {
        for (int i = 0; i < defaultValue.size(); i++) {
            List<Object> v = defaultValue.get(i);
            if (!(types[i] == String.class || types[i] == Double.class || types[i] == Integer.class)) {
                throw new IllegalArgumentException("Invalid type: " + types[i]);
            }
            for (int j = 0; j < types.length; j++) {
                if (v.get(j).getClass() != types[j]) {
                    throw new IllegalArgumentException("Type mismatch: expected " + types[j] + " but got " + v.get(j).getClass());
                }
            }
        }
        return null;
    }

    @Override
    public List<List<Object>> getTable() {
        return table;
    }

    @Override
    public ImmutableList<List<Object>> getDefaultTable() {
        return defaultTable;
    }

    @Override
    public void setTable(List<List<Object>> newTable) {
        if (!this.table.equals(newTable)) {
            this.table.clear();
            this.table.addAll(newTable);
            this.onValueChanged();
        }
    }

    @Override
    public void setModified() {
        this.onValueChanged();
    }

    @Override
    public @Nullable String getDisplayString() {
        return this.displayString;
    }

    @Override
    public List<Class<?>> getTypes() {
        return types;
    }

    @Override
    public void resetToDefault() {
        setTable(defaultTable);
    }

    @Override
    public boolean isModified() {
        return !table.equals(defaultTable);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        List<List<Object>> oldTable = new ArrayList<>(table);
        table.clear();
        try {
            JsonArray arr = element.getAsJsonArray();

            for (JsonElement el : arr) {
                if (!(el instanceof JsonArray jarr)) {
                    throw new Exception();

                }
                var tempList = new ArrayList<>();
                for (JsonElement el2 : jarr) {
                    if (el2.isJsonPrimitive()) {
                        if (el2.getAsJsonPrimitive().isString()) {
                            tempList.add(el2.getAsString());
                        } else if (el2.getAsJsonPrimitive().isNumber()) {
                            Number num = el2.getAsNumber();
                            try {
                                tempList.add(Integer.valueOf(el2.getAsString()));
                            } catch (Exception e) {
                                tempList.add(num.doubleValue());
                            }
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                }
                table.add(tempList);
            }

            if (!table.equals(oldTable)) {
                onValueChanged();
            }
        } catch (Exception e) {
            MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonArray tableArr = new JsonArray();

        for (var entry : table) {
            JsonArray entryArr = new JsonArray();
            for (var obj : entry) {
                if (obj instanceof String str) {
                    entryArr.add(str);
                } else if (obj instanceof Integer integer) {
                    entryArr.add(integer);
                } else if (obj instanceof Double dbl) {
                    entryArr.add(dbl);
                }
            }
            tableArr.add(entryArr);
        }

        return tableArr;
    }
}
