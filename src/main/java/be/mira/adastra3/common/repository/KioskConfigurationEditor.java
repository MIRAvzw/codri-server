/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

import be.mira.adastra3.common.topology.Kiosk;
import org.apache.log4j.Logger;
import org.ini4j.Ini;

/**
 *
 * @author tim
 */
public class KioskConfigurationEditor extends ConfigurationEditor {
    //
    // Data members
    //

    private static Logger mLogger = Logger.getLogger(KioskConfigurationEditor.class);

    //
    // Construction and destruction
    //

    public KioskConfigurationEditor() {
        super();
        mRepositoryDirectory = "kiosks";
    }


    //
    // Auxiliary
    //

    @Override
    void pushConfiguration(Ini tIniReader) throws RepositoryException {
        KioskConfiguration tKiosk = new KioskConfiguration(tIniReader);
        Repository.getInstance().addKioskConfiguration(mDataIdentifier, tKiosk);

        mLogger.debug("Successfully loaded kiosk '" + mDataIdentifier + "'");
    }

}
