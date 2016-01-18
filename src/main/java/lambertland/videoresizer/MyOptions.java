package lambertland.videoresizer;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Ranbato on 1/16/2016.
 */
public class MyOptions {

    static private Logger logger = LoggerFactory.getLogger(MyOptions.class);

    public Properties parseOptions (String [] args){

        // Get default properties from properties file

        Properties properties = new Properties();
        try {
            properties.load(MyOptions.class.getResourceAsStream("/videoresizer.properties"));
        } catch (IOException e) {
            logger.error("Unable to load properties file videoresizer.properties: {}",e);
            System.exit(-2);
        }
        Options options = buildOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options,args);

            if(commandLine.hasOption('h')){
                usage(options);
                System.exit(1);
            }

        } catch (ParseException e) {
            logger.error("Invalid parameters: {}",e);
            usage(options);

            System.exit(-1);
        }


        return properties;
    }

    private Options buildOptions(){
        Option logfile   = Option.builder("l").argName( "logfile" )
                .hasArg()
                .longOpt("logfile")
                .desc(  "use given file for log" )
                .build();

        Option handbrake    = Option.builder("b").argName( "handbrake-path" )
                .hasArg()
                .longOpt("handbrake-path")
                .desc( "Location of Handbrake")
                .build();
        Option sizeperhour = Option.builder("s").argName("size")
                .hasArg()
                .longOpt("size")
                .desc("Max KB/hour")
                .build();
        Option searchPath = Option.builder("p").argName("search-path")
                .longOpt("search-path")
                .hasArg()
                .desc("Path to search for videos")
                .build();
        Option help = Option.builder( "h")
                .longOpt("help")
                .desc("Shows this help" )
                .build();

        Options options = new Options();
        options.addOption(logfile);
        options.addOption(handbrake);
        options.addOption(sizeperhour);
        options.addOption(searchPath);
        options.addOption(help);

        return options;
    }

    private void usage(Options options){
        // automatically generate the help statement
        String header = "Resize video files using lower quality h.254\n\n";
        String footer = "\nFooter";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "videoresizer",header, options,footer, true );
    }
}
