/*
 * Explanation from mhucka@caltech.edu:
 *
 * This file came from http://geosoft.no/software/, obtained on 2017-10-09.
 * Unfortunately, there is no author information on the site, but a whois
 * lookup returns "GEOTECHNICAL SOFTWARE SERVICES Jacob Dreyer" as the owner
 * of the domain.  I could find no other information, other than some job
 * listings that imply geosoft.no is, indeed, located in Norway.
 *
 * I made a minor change to this file, to change the Java package declaration.
 *
 * What follows is the original file header of this file:
 * ----------------------------------------------------------------------------
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */

package org.sbml.testsuite.ui;

import java.io.File;

/**
 * Interface for listening to disk file changes.
 * @see FileMonitor
 *
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public interface FileListener
{
    /**
     * Called when one of the monitored files are created, deleted
     * or modified.
     * 
     * @param file  File which has been changed.
     */
    void fileChanged (File file);
}
