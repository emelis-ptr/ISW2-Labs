package lab_three;

import utils.LogFile;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static utils.Costants.PROJ_NAME;

public class WriteLabThree {

    private WriteLabThree(){}

    public static void writeReleaseInfo(Map<LocalDateTime, String> releaseNames, Map<LocalDateTime, String> releaseID, List<LocalDateTime> releases){
        String outname = "results/" + PROJ_NAME + "-VersionInfo.csv";

        try(FileWriter fileWriter = new FileWriter(outname) ){
            //Name of CSV for output
            fileWriter.append("Index; Version ID; Version Name;Date");
            fileWriter.append("\n");
            for (int i = 0; i < releases.size(); i++) {
                Integer index = i + 1;
                fileWriter.append(index.toString());
                fileWriter.append(";");
                fileWriter.append(releaseID.get(releases.get(i)));
                fileWriter.append(";");
                fileWriter.append(releaseNames.get(releases.get(i)));
                fileWriter.append(";");
                fileWriter.append(releases.get(i).toString());
                fileWriter.append("\n");
            }

        } catch (Exception e) {
            LogFile.errorLog("Error in csv writer");
            e.printStackTrace();
        }
    }

}
