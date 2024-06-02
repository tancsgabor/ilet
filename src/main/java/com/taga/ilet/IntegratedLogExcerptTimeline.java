package com.taga.ilet;

import com.taga.ilet.helper.InMemoryPreProc;
import com.taga.ilet.helper.LPCLoader;
import com.taga.ilet.helper.LogProcessingConfiguration;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class IntegratedLogExcerptTimeline {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Integrated Log Excerpt Timeline");
        LPCLoader configLoader = new LPCLoader();
        configLoader.loadConfigFolder(System.getProperty("user.dir")+"/example");
        InMemoryPreProc preProc = new InMemoryPreProc();
        try {
            for (LogProcessingConfiguration config : configLoader.getLogProcessingConfigurations()) {
                preProc.preprocess(config);
            }
        } catch (NoSuchFileException e){
            System.out.println("In the JSON configuration files \"logFileFullPath\" should be set properly..");
            throw e;
        }

//        preProc.getPartialResultDisplay();
//        System.out.println("--- --- --- ---");

        preProc.createTimeline();
//        preProc.getTimelineFlowResultDisplay();
//        System.out.println("--- --- --- ---");

        preProc.getTimelineColumnResultDisplay(configLoader.getLogProcessingConfigurations());

    }
}