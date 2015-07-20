/*!
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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.reporting.engine.classic.core.layout.build;

import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.states.ReportStateKey;

public class ProxyLayoutModelBuilderContext implements LayoutModelBuilderContext {
  private LayoutModelBuilderContext context;

  public ProxyLayoutModelBuilderContext( final LayoutModelBuilderContext context ) {
    this.context = context;
  }

  public RenderBox getRenderBox() {
    return context.getRenderBox();
  }

  public void addChild( final RenderBox child ) {
    context.addChild( child );
  }

  public void removeChild( final RenderBox child ) {
    context.removeChild( child );
  }

  public LayoutModelBuilderContext getParent() {
    return context;
  }

  public boolean isEmpty() {
    return context.isEmpty();
  }

  public void setEmpty( final boolean empty ) {

  }

  public boolean isKeepWrapperBoxAlive() {
    return context.isKeepWrapperBoxAlive();
  }

  public void setKeepWrapperBoxAlive( final boolean keepWrapperBoxAlive ) {

  }

  public boolean isAutoGeneratedWrapperBox() {
    return context.isAutoGeneratedWrapperBox();
  }

  public void setAutoGeneratedWrapperBox( final boolean autoGeneratedWrapperBox ) {

  }

  public LayoutModelBuilderContext close() {
    return context;
  }

  public Object clone() {
    try {
      return super.clone();
    } catch ( CloneNotSupportedException e ) {
      throw new IllegalStateException( e );
    }
  }

  public LayoutModelBuilderContext deriveForPagebreak() {
    final ProxyLayoutModelBuilderContext clone = (ProxyLayoutModelBuilderContext) clone();
    clone.context = context.deriveForPagebreak();
    return clone;
  }

  public LayoutModelBuilderContext deriveForStorage( final RenderBox clonedRoot ) {
    if ( clonedRoot == null ) {
      throw new NullPointerException();
    }
    final ProxyLayoutModelBuilderContext clone = (ProxyLayoutModelBuilderContext) clone();
    clone.context = context.deriveForStorage( clonedRoot );
    return clone;
  }

  public boolean mergeSection( final ReportStateKey stateKey ) {
    return context.mergeSection( stateKey );
  }

  public void validateAfterCommit() {

  }

  public void performParanoidModelCheck() {

  }

  public void restoreStateAfterRollback() {

  }

  public void commitAsEmpty() {

  }

  public int getDepth() {
    if ( context == null ) {
      return 1;
    }
    return 1 + context.getDepth();
  }
}
