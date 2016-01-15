/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambertland.videoresizer;

// Only ever import slf4j Logging APIs
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


// Video file reader library
//TODO add to dependency list

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;


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
            IContainer video = IContainer.make();
            long length;
            int result = video.open(file.toString(), IContainer.Type.READ,null);
            if(result < 0){
                length = -1;
            } else {
                length = video.getDuration()/60;
            }

            logger.info("Regular file: {} ({} bytes) {} minutes", file, attr.size(),length);

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

    public static long DetermineVideoLength(IContainer container) {
        // query for the total duration
        long duration = container.getDuration();
        /*
         TODO find out what unit this returns
         (not found in documentation http://www.xuggle.com/public/documentation/java/api/ Icontainer-> methods -> getDuration)
         eg seconds, milliseconds, etc...
         assumes seconds
         */
        if (duration > 0)
        return duration;
        else {
            throw new Exception("No duration data found for " + fileName);
        }
    }

    public static boolean NeedToReencode(String file) {
        IContainer container= IContainer.make();
        int result = container.open(file, IContainer.Type.READ, null);

        // check if the operation was successful
        if (result<0)
            throw new RuntimeException("Failed to open media file");
        // TODO add exception handling for getting the duration in the main try catch block
        long duration = DetermineVideoLength(container);
        // This currently assumes DetermineVideoLength returns in seconds
        // Converts to minutes
        duration = duration / 60;
        int numThirtyMin = 0;
        while (duration >= 30) {
            numThirtyMin++;
            duration = duration - 30;
        }
        long fileSize = container.getFileSize();
        if (fileSize < 0) {
            //Shouldn't happen, but don't exactly know what kind of error to throw...
            throw new Exception("Error getting filesize");
        }
        //Convert fileSize to MB
        fileSize = fileSize / (1024*1024);
        int numHalfGig = 0;
        while (fileSize >= 512) {
            numHalfGig++;
            fileSize -= 512;
        }
        if (numHalfGig > numThirtyMin) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Main main = new Main();
        try {
            // TODO code application logic here
            Files.walkFileTree(Paths.get("\\\\master-htpc\\Videos\\Video"), main); //@todo make command line parameter
            //TODO Make list of files found, change loop parameters after
            for (String filename : Files) { // for each video file found
                if (NeedToReencode(filename)) {
                    //TODO Reencode file
                }
            }
        } catch (IOException ex) {
            logger.error("Critical Error {}", ex);
        }
    }
    
    
    private String render(String destDir, String inputFile){
        return "Dummy String";
    }
    
}
