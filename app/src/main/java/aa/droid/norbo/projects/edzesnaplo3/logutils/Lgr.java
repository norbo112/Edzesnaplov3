package aa.droid.norbo.projects.edzesnaplo3.logutils;

import android.os.Environment;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * My Logger
 */
public class Lgr {
    private final static LogConfigurator mLogConfigrator = new LogConfigurator();

    static {
        configureLog4j();
    }

    private static void configureLog4j() {
        String fileName = Environment.getExternalStorageDirectory() + "/" + "edzesnaplov3.log";
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024;

        configure( fileName, filePattern, maxBackupSize, maxFileSize );
    }

    private static void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        mLogConfigrator.setFileName( fileName );
        mLogConfigrator.setMaxFileSize( maxFileSize );
        mLogConfigrator.setFilePattern(filePattern);
        mLogConfigrator.setMaxBackupSize(maxBackupSize);
        mLogConfigrator.setUseLogCatAppender(true);
        mLogConfigrator.configure();

    }

    public static org.apache.log4j.Logger getLogger( String name ) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( name );
        return logger;
    }
}
