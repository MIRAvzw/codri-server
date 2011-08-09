/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website;

import be.mira.adastra3.server.website.status.StatusApplication;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Status extends WtServlet {
    private static final long serialVersionUID = 1L;

    public Status() {
        super();
    }

    @Override
    public final WApplication createApplication(final WEnvironment iEnvironment) {
        /*
         * You could read information from the environment to decide whether the
         * user has permission to start a new application
         */
        return new StatusApplication(iEnvironment);
    }
}

