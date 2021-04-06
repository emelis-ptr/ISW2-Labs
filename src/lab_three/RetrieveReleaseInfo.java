package lab_three;

import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import utils.JsonUtils;

import static utils.Costants.*;


public class RetrieveReleaseInfo {

    private RetrieveReleaseInfo() {
    }

    private static Map<LocalDateTime, String> releaseNames = new HashMap<>();
    private static Map<LocalDateTime, String> releaseID;
    private static List<LocalDateTime> releases;

    public static void main(String[] args) throws IOException, JSONException {

        //Fills the arraylist with releases dates and orders them
        //Ignores releases with missing dates
        releases = new ArrayList<>();
        Integer i;
        String url = "https://issues.apache.org/jira/rest/api/2/project/" + PROJ_NAME.toUpperCase();
        JSONObject json = JsonUtils.readJsonFromUrl(url);
        JSONArray versions = json.getJSONArray(VERSIONS);
        releaseNames = new HashMap<>();
        releaseID = new HashMap<>();

        for (i = 0; i < versions.length(); i++) {
            String name = "";
            String id = "";
            if (versions.getJSONObject(i).has(RELEASE_DATE)) {
                if (versions.getJSONObject(i).has(NAME_VERSION))
                    name = versions.getJSONObject(i).get(NAME_VERSION).toString();
                if (versions.getJSONObject(i).has(ID_VERSION))
                    id = versions.getJSONObject(i).get(ID_VERSION).toString();
                addRelease(versions.getJSONObject(i).get(RELEASE_DATE).toString(),
                        name, id);
            }
        }
        // order releases by date
        Collections.sort(releases, Comparator.naturalOrder());

        WriteLabThree.writeReleaseInfo(releaseNames, releaseID, releases);
    }


    public static void addRelease(String strDate, String name, String id) {
        LocalDate date = LocalDate.parse(strDate);
        LocalDateTime dateTime = date.atStartOfDay();
        if (!releases.contains(dateTime))
            releases.add(dateTime);
        releaseNames.put(dateTime, name);
        releaseID.put(dateTime, id);
    }


}