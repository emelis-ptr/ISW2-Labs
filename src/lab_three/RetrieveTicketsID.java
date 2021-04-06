package lab_three;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entity.Tickets;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import utils.JsonUtils;
import utils.LogFile;

import static utils.Costants.*;

public class RetrieveTicketsID {

    private RetrieveTicketsID() {
    }

    public static void main(String[] args) throws IOException, JSONException, ParseException {
        Integer j;
        Integer i = 0;
        Integer total;

        Tickets tickets = null;
        ArrayList<Tickets> listTickets = new ArrayList<>();
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + PROJ_NAME + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i.toString() + "&maxResults=" + j.toString();

            JSONObject json = JsonUtils.readJsonFromUrl(url);
            JSONArray issues = json.getJSONArray(ISSUES);

            total = json.getInt(TOTAL);

            int totalVersion;
            String nameVersion;

            for (; i < total && i < j; i++) {
                //Iterate through each bug
                ArrayList<String> listAV = new ArrayList<>();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

                String idTicket = issues.getJSONObject(i%1000).get(KEY).toString();
                JSONObject issue =  issues.getJSONObject(i % 1000);
                Date created = df.parse(issue.getJSONObject(FIELDS).get(CREATED).toString());
                Date resolutionDate = df.parse(issue.getJSONObject(FIELDS).get(RESOLUTION_DATE).toString());

                JSONArray affectedVersions = issue.getJSONObject(FIELDS).getJSONArray(VERSIONS);
                totalVersion = affectedVersions.length();

                for(int k=0; k<totalVersion; k++){
                    nameVersion = affectedVersions.getJSONObject(k).get(ID_VERSION).toString();
                    listAV.add(nameVersion);
                }

                tickets = new Tickets(idTicket, resolutionDate, created, listAV);
                listTickets.add(tickets);
            }
        } while (i < total);

        LogFile.infoLog("Tickets in jira: " + listTickets.size());
    }


}
