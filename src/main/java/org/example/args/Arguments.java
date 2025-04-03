package org.example.args;

import java.util.HashMap;
import java.util.Map;

public class Arguments {
    private final Map<String, String> argsMap = new HashMap<>();

    public Arguments(String[] args) {
        for (int i = 0; i < args.length - 1; i += 2) {
            argsMap.put(args[i], args[i + 1]);
        }
    }

    public String get(String key) {
        return argsMap.get(key);
    }

    public boolean isValid() {
        return argsMap.containsKey("--data")
                && argsMap.containsKey("--indexed-column-id")
                && argsMap.containsKey("--input-file")
                && argsMap.containsKey("--output-file");
    }
}