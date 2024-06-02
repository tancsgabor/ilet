package com.taga.ilet.helper;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
Loads a single LogProcessingConfiguration JSON file and creates an instance
*/
public class LPCLoader {

    private List<LogProcessingConfiguration> logProcessingConfigurations = new ArrayList<>();

    private LogProcessingConfiguration load(String configFilePath) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(configFilePath)), StandardCharsets.UTF_8);
        LogProcessingConfiguration config = new Gson().fromJson(jsonString, LogProcessingConfiguration.class);
        return config;
    }

    public void loadConfigFolder(String configFolderPath) throws IOException {
        if (Files.isDirectory(Paths.get(configFolderPath))) {
            File dir = new File(configFolderPath);
            for  (File config : dir.listFiles()){
                if (config.isFile() && config.getName().endsWith(".json")){
                    logProcessingConfigurations.add(load(config.getPath()));
                }
            }
        } else {
            throw new IOException("Config folder path invalid!:" + configFolderPath);
        }
    }

    public List<LogProcessingConfiguration> getLogProcessingConfigurations() {
        return logProcessingConfigurations;
    }
}
