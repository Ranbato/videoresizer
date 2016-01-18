package lambertland.videoresizer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.*;

/**
 * Created by Ranbato on 1/18/2016.
 */
public class MyOptionsTest {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testParseOptionsHelp() throws Exception {

        final String output = normalizeString("usage: videoresizer [-b <handbrake-path>] [-h] [-l <logfile>] [-p\n" +
                "       <search-path>] [-s <size>]\n" +
                "Resize video files using lower quality h.254\n" +
                "\n" +
                " -b,--handbrake-path <handbrake-path>   Location of Handbrake\n" +
                " -h,--help                              Shows this help\n" +
                " -l,--logfile <logfile>                 use given file for log\n" +
                " -p,--search-path <search-path>         Path to search for videos\n" +
                " -s,--size <size>                       Max KB/hour\n" +
                "\n" +
                "Footer");

        MyOptions options = new MyOptions();
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertEquals(output, normalizeString(systemOutRule.getLog())));
        // run the test
        options.parseOptions(new String[]{"-h"});


    }

    /**
     * The System-Rules library can normalize line endings, but I find that removing all whitespace
     * makes the comparison much easier.
     *
     * @param input String to be normalized
     * @return String with no whitespace
     */

    private static String normalizeString(String input){
        return input.replaceAll("\\s*","");

    }
}