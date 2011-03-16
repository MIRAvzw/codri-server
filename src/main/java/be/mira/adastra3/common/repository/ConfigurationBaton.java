/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.common.repository;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

/**
 * Deze klasse wordt door de SVN repository gebruikt bij het updaten om te
 * bepalen wat de staat is van de working copy. Afhankelijk van wat deze
 * klasse teruggeeft, zal de server meer of minder data sturen om de working
 * copy up to date te brengen.
 *
 * Een eerste implementatie zal naïef teruggeven dat de lokale working copy
 * leeg is, zodat alle data binnengehaald wordt.
 *
 * Een verfijndere implementatie zal kijken naar de objecten lokaal opgeslaan
 * (en wat hun revisie daar van is) om zo selectief data op te halen.
 *
 * @author tim
 */
public class ConfigurationBaton implements ISVNReporterBaton {

    private long exportRevision;

    public ConfigurationBaton(long revision) {
        exportRevision = revision;
    }

    public void report(ISVNReporter reporter) throws SVNException {
        try {
            /*
             * Here empty working copy is reported.
             *
             * ISVNReporter includes methods that allows to report mixed-rev working copy
             * and even let server know that some files or directories are locally missing or
             * locked.
             */
            reporter.setPath("", null, exportRevision, SVNDepth.INFINITY, true);

            /*
             * Don't forget to finish the report!
             */
            reporter.finishReport();
        } catch (SVNException svne) {
            reporter.abortReport();
            System.out.println("Report failed.");
        }
    }
}
