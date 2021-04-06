package utils;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static utils.Costants.PATHREPOSITORY;

public class InitRepo {


    private InitRepo() {
    }


    /**
     * Return string path
     *
     * @param path: path directory
     * @return: string path
     */
    public static String returnRepo(String path) {
        //Prende il repository path definito all'interno del path_repository.properties
        Properties properties = new Properties();
        try(FileInputStream fileInput = new FileInputStream(PATHREPOSITORY)) {
            properties.load(fileInput);
        } catch(Exception e) {LogFile.errorLog("Errore nel file path repository.");}
        return properties.getProperty(path);
    }

    /**
     * Return repository
     *
     * @param path: string path directory
     * @return: repository
     * @throws IOException: IOException
     */
    public static Repository repository(String path) throws IOException {
        String repoPath = returnRepo(path);

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.setGitDir(new File(repoPath)).setMustExist(true).build();
    }

    /**Metodo che ritorna il Path per il treeWalk
     *
     * @return: path
     */
    public static Path returnPath(String path) {
        String rootPath = returnRepo(path);
        return Paths.get(rootPath);
    }
}
