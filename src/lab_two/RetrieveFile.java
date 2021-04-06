package lab_two;

import lab_one.LabOne;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.NullOutputStream;
import utils.Costants;
import utils.InitRepo;

import java.io.IOException;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

import static utils.Costants.*;

public class RetrieveFile {

    public static void main(String[] args) {
        try {
            getFiles();
        } catch (IOException | NoHeadException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che trova tutti i files
     *
     * @throws IOException:     IOException
     * @throws NoHeadException: NoHeadException
     */
    private static void getFiles() throws IOException, NoHeadException {
        String[] path = Costants.getPATH();
        List<RevCommit> listCommit = LabOne.retrieveCommentTicket(path[1]);
        TreeMap<LocalDate, ArrayList<String>> dateFiles = new TreeMap<>();

        setDateToMap(listCommit, dateFiles);

        Path repoPath = InitRepo.returnPath(path[1]);

        Git git = Git.open(repoPath.toFile());

        for (RevCommit commit : listCommit) {

            ObjectId treeId = commit.getTree();

            TreeWalk treeWalk = new TreeWalk(git.getRepository());
            treeWalk.reset(treeId);
            treeWalk.setRecursive(true);

            while (treeWalk.next()) {
                if (treeWalk.getPathString().endsWith(JAVA_EXT)) {
                    String nameFile = treeWalk.getPathString();
                    dateFiles.get(commit.getAuthorIdent().getWhen()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).add(nameFile);
                }
            }

        }

        findChangedFile(path[1], dateFiles, listCommit);
    }

    /**
     * Set LocalDate commit to Map
     *
     * @param listCommit: all commits
     * @param dateFiles:  treeMap
     */
    public static void setDateToMap(List<RevCommit> listCommit, SortedMap<LocalDate, ArrayList<String>> dateFiles) {
        for (RevCommit commit : listCommit) {
            dateFiles.put(commit.getAuthorIdent().getWhen()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), new ArrayList<>());
        }

    }

    /**
     * Metodo che inserisce in un HashMap tutti i file e il loro valore a False
     *
     * @param dateFiles:   treeMap
     * @param commitsList: all commits
     * @throws IOException: IOException
     */
    public static void findChangedFile(String path, SortedMap<LocalDate, ArrayList<String>> dateFiles, List<RevCommit> commitsList) throws IOException {
        Repository repository = InitRepo.repository(path);

        //diffFormatter trova le differenze da i tree di due commit (ovvero tra i file)
        DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
        diffFormatter.setRepository(repository);
        HashMap<String, String> bugginessFiles = new HashMap<>();

        String lastMonth = lastMonth(dateFiles);
        String yearMonthCommit;
        for (Map.Entry<LocalDate, ArrayList<String>> entry : dateFiles.entrySet()) {
            yearMonthCommit = entry.getKey().getYear() + "-" +  entry.getKey().getMonthValue();
            if (yearMonthCommit.equals(lastMonth)) {
                for (String files : entry.getValue()) {
                    bugginessFiles.put(files, Boolean.FALSE.toString());
                }
            }
        }
        findClassTouched(commitsList, diffFormatter, bugginessFiles);
        WriteLabTwo.writeRelease(bugginessFiles);

    }

    /**
     * Metodo che trova tutte le classi che sono state modificate
     *
     * @param commitsList:    all commits
     * @param diffFormatter:  DiffFormatter
     * @param bugginessFiles: hashMap
     * @throws IOException: IOException
     */
    public static void findClassTouched(List<RevCommit> commitsList, DiffFormatter diffFormatter, Map<String, String> bugginessFiles) throws IOException {
        //Iterazione su ogni commit
        for (RevCommit commit : commitsList) {

            //Commit precedente
            RevCommit previousCommit = commit.getParentCount() > 0 ? commit.getParent(0) : null;
            if (previousCommit != null) {

                //diffFormatter restituisce una lista dei path dei file che sono differenti tra i due commit
                for (DiffEntry entry : diffFormatter.scan(previousCommit, commit)) {

                    if ((entry.getChangeType().equals(DiffEntry.ChangeType.MODIFY) || entry.getChangeType().equals(DiffEntry.ChangeType.DELETE))
                            && entry.getNewPath().endsWith(JAVA_EXT)) {
                        bugginessFiles.replace(entry.getNewPath(), Boolean.TRUE.toString());
                    }
                }
            }
        }
    }

    /**
     * Metodo che ritorna l'ultimo mese della prima metà di ciclo di vita del progetto
     *
     * @param dateFiles: treeMap
     * @return last year
     */
    public static String lastMonth(SortedMap<LocalDate, ArrayList<String>> dateFiles) {
        long firstYear = dateFiles.firstKey().getYear();
        long lastYear = dateFiles.lastKey().getYear();
        long middle = (lastYear - firstYear) / 2;

        return (firstYear + middle) + "-12";
    }


}
