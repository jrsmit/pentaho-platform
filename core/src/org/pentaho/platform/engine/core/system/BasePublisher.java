/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License, version 2 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 * Copyright 2005 - 2008 Pentaho Corporation.  All rights reserved. 
 * 
 * @created Jul 18, 2005 
 * @author James Dixon
 * 
 */
package org.pentaho.platform.engine.core.system;

import org.pentaho.platform.api.engine.IPentahoPublisher;
import org.pentaho.platform.api.engine.IPentahoSession;

public abstract class BasePublisher extends PentahoBase implements IPentahoPublisher {

  private static final long serialVersionUID = -8079266498445883700L;
  public final static boolean debug = PentahoSystem.debug;

  abstract public String publish(IPentahoSession session);

  public String publish(final IPentahoSession session, final int pLoggingLevel) {

    setLoggingLevel(pLoggingLevel);
    return publish(session);
  }

}
