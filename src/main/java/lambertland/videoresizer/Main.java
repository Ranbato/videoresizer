/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambertland.videoresizer;

// Only ever import slf4j Logging APIs
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Mark
 */
public class Main extends SimpleFileVisitor<Path> {

    static private Logger logger = LoggerFactory.getLogger(Main.class);
    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
            logger.info("Symbolic link: {} ({} bytes)", file, attr.size());
        } else if (attr.isRegularFile()) {
            logger.info("Regular file: {} ({} bytes)", file, attr.size());
        } else {
            logger.info("Other: {} ({} bytes)", file, attr.size());
        }
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                          IOException exc) {
        logger.info("Directory: {}", dir);
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException 
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                       IOException exc) {
        logger.warn("Visit failed {}",exc);
        return CONTINUE;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Main main = new Main();
        try {
            // TODO code application logic here
            Files.walkFileTree(Paths.get("\\\\master-htpc\\Videos\\Video"), main); //@todo make command line parameter
        } catch (IOException ex) {
            logger.error("Critical Error {}", ex);
        }
    }
    
    
    private String render(String destDir, String inputFile){
        return "Dummy String";
    }
    
}
