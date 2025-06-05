package com.VDT_2025_Phase_1.DuongHaiAnh.utils.component;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigEntry;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class YamlBuilderHelper {

    public String buildYaml(List<ConfigEntry> entries) {
        Map<String, Object> flatMap = new LinkedHashMap<>();
        for (ConfigEntry entry : entries) {
            flatMap.put(entry.getKey(), entry.getValue());
        }

        Map<String, Object> nestedMap = convertToNestedMap(flatMap);
        Yaml yaml = new Yaml(new DumperOptions() {{
            setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        }});
        return yaml.dump(nestedMap);
    }

    private Map<String, Object> convertToNestedMap(Map<String, Object> flatMap) {
        Map<String, Object> root = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
            String[] keys = entry.getKey().split("\\.");
            Map<String, Object> current = root;
            for (int i = 0; i < keys.length - 1; i++) {
                current = (Map<String, Object>) current.computeIfAbsent(keys[i], k -> new LinkedHashMap<>());
            }
            current.put(keys[keys.length - 1], entry.getValue());
        }

        return root;
    }
}
