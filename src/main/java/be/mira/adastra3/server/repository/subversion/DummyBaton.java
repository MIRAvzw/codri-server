/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.subversion;

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
public class DummyBaton implements ISVNReporterBaton {
    //
    // Data members
    //

    private long mExportRevision;


    //
    // Construction and destruction
    //

    public DummyBaton(final long iRevision) {
        mExportRevision = iRevision;
    }


    //
    // ISVNReporterBaton interface
    //
    
    @Override
    public final void report(final ISVNReporter iReporter) throws SVNException {
        /*
         * Here empty working copy is reported.
         *
         * ISVNReporter includes methods that allows to report mixed-rev working copy
         * and even let server know that some files or directories are locally missing or
         * locked.
         */
        iReporter.setPath("", null, mExportRevision, SVNDepth.INFINITY, true);

        /*
         * Don't forget to finish the report!
         */
        iReporter.finishReport();
    }
}
