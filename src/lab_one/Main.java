package lab_one;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import utils.InitRepo;
import utils.LogFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {

        try {
            retrieveCommentTicket();
        } catch (NoHeadException | IOException e) {
            LogFile.errorLog("Errore!");
            e.printStackTrace();
        }

    }

    /**
     * Metodo che verifica se esiste una determinata string nel commento di un commit
     * @throws NoHeadException
     * @throws IOException
     */
    public static void retrieveCommentTicket() throws NoHeadException, IOException {
        Repository repo = InitRepo.repository();

        Git git = new Git(repo);

        Iterable<RevCommit> logs = git.log().call();

        Iterator<RevCommit> iterator = logs.iterator();

        ArrayList<RevCommit> list = new ArrayList<>();
        String comment = "Added";

        while (iterator.hasNext()) {

            RevCommit rev = iterator.next();

            if (rev.getFullMessage().contains(comment)) {
                list.add(rev);
            }
        }

        LogFile.setupLogger();
        if (list.isEmpty()) {
            LogFile.warnLog("Lista vuota");
        } else {
            LogFile.infoLog(list.toString());
        }
    }
}
