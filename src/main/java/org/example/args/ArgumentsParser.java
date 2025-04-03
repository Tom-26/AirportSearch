package org.example.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentsParser {
    private final Map<String, String> args;

    public ArgumentsParser(String[] inputArgs) {
        args = new HashMap<>();
        for (int i = 0; i < inputArgs.length - 1; i += 2) {
            args.put(inputArgs[i], inputArgs[i + 1]);
        }
    }

    public boolean isValid() {
        return args.containsKey("--data") && args.containsKey("--indexed-column-id")
                && args.containsKey("--input-file") && args.containsKey("--output-file");
    }

    public String getDataFile() { return args.get("--data"); }
    public String getInputFile() { return args.get("--input-file"); }
    public String getOutputFile() { return args.get("--output-file"); }
    public int getIndexedColumnId() { return Integer.parseInt(args.get("--indexed-column-id")); }
}