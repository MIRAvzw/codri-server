/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

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
        Configuration tConfiguration = new Configuration(tIniReader);
        Repository.getInstance().addConfiguration(mDataIdentifier, tConfiguration);

        mLogger.debug("Successfully loaded configuration '" + mDataIdentifier + "'");
    }

}
