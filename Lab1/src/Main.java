import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import utils.InitRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args){

        retrieveCommentTicket();

    }

    public static void retrieveCommentTicket()  {
        Repository repo = null;
        try {
            repo = InitRepo.repository();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Git git = new Git(repo);

        Iterable<RevCommit> logs = null;
        try {
            logs = git.log().call();
        } catch (NoHeadException e) {
            e.printStackTrace();
        }
        Iterator<RevCommit> iterator =logs.iterator();

        ArrayList<RevCommit> list = new ArrayList<>();
        String comment = "Added";

        while(iterator.hasNext()) {

            RevCommit rev = iterator.next();

            if (rev.getFullMessage().contains(comment)) {
                list.add(rev);
            }
        }

        System.out.println(list);
    }
}
