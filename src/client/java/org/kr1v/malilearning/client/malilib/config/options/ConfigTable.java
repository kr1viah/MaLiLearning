package org.kr1v.malilearning.client.malilib.config.options;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.options.ConfigBase;
import org.jetbrains.annotations.NotNull;
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
    private final List<String> labels;
    private final boolean allowNewEntry;
    private final boolean showEntryNumbers;

    public ConfigTable(String name, String comment, String prettyName, String translatedName,
                       @Nullable String displayString, List<List<Object>> defaultValue,
                       List<String> labels, boolean showEntryNumbers, boolean allowAddNewEntry,
                       Class<?>... types) {
        super(null, name, comment, prettyName, translatedName);
        this.labels = labels;
        this.allowNewEntry = allowAddNewEntry;
        this.showEntryNumbers = showEntryNumbers;

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

    @Override
    public List<String> getLabels() {
        return labels;
    }

    @Override
    public boolean allowNewEntry() {
        return allowNewEntry;
    }

    @Override
    public boolean showEntryNumbers() {
        return showEntryNumbers;
    }

    public static class Builder {
        private static @NotNull List<Object> getDummy(List<Class<?>> types) {
            List<Object> dummy = new ArrayList<>();
            for (Class<?> type : types) {
                if (type == String.class) {
                    dummy.add("");
                } else if (type == Integer.class) {
                    dummy.add(0);
                } else if (type == Double.class) {
                    dummy.add(0.0);
                } else {
                    throw new IllegalStateException("Unsupported type: " + type.getName());
                }
            }
            return dummy;
        }

        private String name;
        private String comment = null;
        private String prettyName = null;
        private String translatedName = null;
        private @Nullable String displayString = null;
        private List<List<Object>> defaultValue = null;
        private List<String> labels = List.of();
        private boolean showEntryNumbers = true;
        private boolean allowAddNewEntry = true;
        private Class<?>[] types;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setPrettyName(String prettyName) {
            this.prettyName = prettyName;
            return this;
        }

        public Builder setTranslatedName(String translatedName) {
            this.translatedName = translatedName;
            return this;
        }

        public Builder setDisplayString(@Nullable String displayString) {
            this.displayString = displayString;
            return this;
        }

        public Builder setDefaultValue(List<List<Object>> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder setLabels(List<String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder setShowEntryNumbers(boolean showEntryNumbers) {
            this.showEntryNumbers = showEntryNumbers;
            return this;
        }

        public Builder setAllowAddNewEntry(boolean allowAddNewEntry) {
            this.allowAddNewEntry = allowAddNewEntry;
            return this;
        }

        public Builder setTypes(Class<?>... types) {
            this.types = types;
            return this;
        }

        public ConfigTable build() {
            defaultValue = new ArrayList<>();
            defaultValue.add(getDummy(List.of(types)));
            if (comment == null) comment = name + " Comment?";
            if (prettyName == null) prettyName = name;
            if (translatedName == null) translatedName = name;


            if (labels.size() != types.length) {
                throw new IllegalArgumentException("Labels size mismatch: expected " + types.length + " but got " + labels.size());
            }
            for (List<Object> v : defaultValue) {
                for (int j = 0; j < types.length; j++) {
                    if (v.get(j).getClass() != types[j] || (types[j] != Integer.class && types[j] != Double.class && types[j] != String.class)) {
                        throw new IllegalArgumentException("Type mismatch: expected " + types[j] + " but got " + v.get(j).getClass());
                    }
                }
            }

            return new ConfigTable(name, comment, prettyName, translatedName, displayString, defaultValue, labels, showEntryNumbers, allowAddNewEntry, types);
        }
    }
}
