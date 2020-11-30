package yopoyka.brigadier.coremod;

import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradleMcp {
    public static GradleMcp instance = new GradleMcp();
    protected File fieldsFile;
    protected File methodsFile;
    protected Map<String, String> fieldMap;
    protected Map<String, String> methodMap;

    {
        try {
            final Class<?> gradle = Class.forName("net.minecraftforge.gradle.GradleStartCommon", false, Launch.classLoader);
            final Field csv_dir = gradle.getDeclaredField("CSV_DIR");
            csv_dir.setAccessible(true);
            final File csvDir = (File) csv_dir.get(null);
            fieldsFile = csvDir.toPath().resolve("fields.csv").toFile();
            methodsFile = csvDir.toPath().resolve("methods.csv").toFile();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            if (Boolean.parseBoolean(System.getProperty("yopoyka.gradlemcp.verbose"))) {
                System.out.println("GradleStartCommon is not loaded!");
                e.printStackTrace();
            }
        }
    }

    public String fromSrg(String srgName) {
        if (srgName.startsWith("field"))
            return fromSrgField(srgName);
        if (srgName.startsWith("func"))
            return fromSrgMethod(srgName);
        return srgName;
    }

    public String fromSrgMethod(String srgName) {
        if (methodMap == null)
            if (methodsFile == null)
                methodMap = Collections.emptyMap();
            else
                methodMap = readLines(methodsFile);

        return methodMap.getOrDefault(srgName, srgName);
    }

    public String fromSrgField(String srgName) {
        if (fieldMap == null)
            if (fieldsFile == null)
                fieldMap = Collections.emptyMap();
            else
                fieldMap = readLines(fieldsFile);

        return fieldMap.getOrDefault(srgName, srgName);
    }

    protected static Map<String, String> readLines(File file) {
        try {
            final Map<String, String> map = new HashMap<>();
            final List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(line -> {
                final String[] split = line.split(",", 3);
                map.put(split[0], split[1]);
            });
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
