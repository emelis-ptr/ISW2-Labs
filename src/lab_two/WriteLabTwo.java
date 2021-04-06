package lab_two;

import utils.LogFile;

import java.io.FileWriter;
import java.util.*;

import static utils.Costants.PROJ_NAME;

public class WriteLabTwo {

    private WriteLabTwo(){}

    /**
     * Write results lab two in csv
     *
     * @param bugginessFiles: file and his bugginess
     */
    public static void writeRelease(Map<String, String> bugginessFiles) {
        String outname = "results/labtwo" + PROJ_NAME + ".csv";

        try(FileWriter fileWriter = new FileWriter(outname)) {

            //Name of CSV for output
            fileWriter.append("Files_name; Bugginess");
            fileWriter.append("\n");

            for (Map.Entry<String, String> entry : bugginessFiles.entrySet()) {
                fileWriter.append(entry.getKey());
                fileWriter.append(";");
                fileWriter.append(entry.getValue());
                fileWriter.append("\n");
            }

        } catch (Exception e) {
            LogFile.errorLog("Error in csv writer");
            e.printStackTrace();
        }

    }
}
