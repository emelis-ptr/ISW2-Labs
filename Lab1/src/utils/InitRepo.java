package utils;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InitRepo {

    private static final String PATHREPOSITORY = "path_repo_hw.properties";
    private static final String PATH = "PATH";

    private InitRepo() {
    }

    /**
     * All'interno del file properties viene salvato il PATH del repository e
     * attraverso questo metodo si inizializza la repo
     *
     * @return
     * @throws IOException
     */
    public static Repository repository() throws IOException {

        Properties properties = new Properties();
        try(FileInputStream fileInput = new FileInputStream(PATHREPOSITORY)) {
            properties.load(fileInput);
        } catch (Exception e){ e.printStackTrace();}

        String repoPath = properties.getProperty(PATH);

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.setGitDir(new File(repoPath)).setMustExist(true).build();

    }
}
