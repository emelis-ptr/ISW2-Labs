package entity;

import java.util.Date;
import java.util.List;

public class Tickets {

    private String idTickets;
    private Date fixedVersion;
    private Date openingVersion;
    private List<String> affectedVersions;

    public Tickets(String idTickets, Date fixedVersion, Date openingVersion, List<String> affectedVersions) {
        this.idTickets = idTickets;
        this.fixedVersion = fixedVersion;
        this.openingVersion = openingVersion;
        this.affectedVersions = affectedVersions;
    }

    public String getIdTickets() {
        return idTickets;
    }

    public void setIdTickets(String idTickets) {
        this.idTickets = idTickets;
    }

    public Date getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(Date fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public Date getOpeningVersion() {
        return openingVersion;
    }

    public void setOpeningVersion(Date openingVersion) {
        this.openingVersion = openingVersion;
    }

    public List<String> getAffectedVersions() {
        return affectedVersions;
    }

    public void setAffectedVersions(List<String> affectedVersions) {
        this.affectedVersions = affectedVersions;
    }
}
