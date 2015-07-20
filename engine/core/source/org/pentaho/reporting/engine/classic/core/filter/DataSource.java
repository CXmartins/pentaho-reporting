/*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2001 - 2013 Object Refinery Ltd, Pentaho Corporation and Contributors..  All rights reserved.
*/

package org.pentaho.reporting.engine.classic.core.filter;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

import java.io.Serializable;

/**
 * A DataSource is a producer in the data chain. Common Sources are StaticSources (predefined data), ReportDataSources
 * (data filled from the reports data set) or FunctionDataSource (the data is filled by querying an assigned function).
 * <p/>
 * All DataSources have to support the Cloneable interface so that a report can be completley cloned with all assigned
 * filters and DataSources. Reports are cloned before they are processed to remove the side effect when having multiple
 * report processors working on the same object.
 *
 * @author Thomas Morgner
 */
public interface DataSource extends Serializable, Cloneable {
  /**
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @param element the element for which the data is computed.
   * @return the value.
   */
  public Object getValue( ExpressionRuntime runtime, final ReportElement element );

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public DataSource clone()
    throws CloneNotSupportedException;

}
