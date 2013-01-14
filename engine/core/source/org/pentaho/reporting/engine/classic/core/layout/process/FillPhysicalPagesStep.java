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
 * Copyright (c) 2001 - 2009 Object Refinery Ltd, Pentaho Corporation and Contributors..  All rights reserved.
 */

package org.pentaho.reporting.engine.classic.core.layout.process;

import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.ParagraphRenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.table.TableSectionRenderBox;
import org.pentaho.reporting.engine.classic.core.layout.process.util.BoxShifter;


/**
 * This Step copies all content from the logical page into the page-grid. When done, it clears the content and replaces
 * the elements with dummy-nodes. These nodes have a fixed-size (the last known layouted size), and will not be
 * recomputed later.
 * <p/>
 * Adjoining dummy-nodes get unified into a single node, thus simplifying and pruning the document tree.
 *
 * @author Thomas Morgner
 */
public final class FillPhysicalPagesStep extends IterateVisualProcessStep
{
  private long contentEnd;
  private long contentStart;

  public FillPhysicalPagesStep()
  {
  }

  public LogicalPageBox compute(final LogicalPageBox pagebox,
                                final long pageStart,
                                final long pageEnd)
  {
    this.contentStart = pagebox.getHeaderArea().getHeight();
    this.contentEnd = (pageEnd - pageStart) + contentStart;

    // This is a simple strategy.
    // Copy and relocate, then prune. (I whished we could prune first, but
    // this does not work.)
    //
    // For the sake of efficiency, we do *not* create private copies for each
    // physical page. This would be an total overkill.
    final LogicalPageBox derived = (LogicalPageBox) pagebox.derive(true);

    // first, shift the normal-flow content downwards.
    // The start of the logical pagebox might be in the negative range now
    // The header-size has already been taken into account by the pagination
    // step.
    BoxShifter.shiftBoxUnchecked(derived, -pageStart + contentStart);

    // now remove all the content that will not be visible at all ..
    // not processing the header and footer area: they are 'out-of-context' bands
    processBoxChilds(derived);

    // Then add the header at the top - it starts at (0,0) and thus it is
    // ok to leave it unshifted.

    // finally, move the footer at the bottom (to the page's bottom, please!)
    final RenderBox footerArea = derived.getFooterArea();

    final RenderBox repeatFooterArea = derived.getRepeatFooterArea();
    final long repeatFooterPosition = pagebox.getPageHeight() -
        repeatFooterArea.getHeight() - footerArea.getHeight();
    BoxShifter.shiftBoxUnchecked(repeatFooterArea, repeatFooterPosition);

    final long footerPosition = pagebox.getPageHeight() - footerArea.getHeight();
    BoxShifter.shiftBoxUnchecked(footerArea, footerPosition);

    // the renderer is responsible for painting the page-header and footer ..

    derived.setPageOffset(0);
    derived.setPageEnd(contentEnd);
    return derived;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  /**
   * Invisible nodes may need special treatment here.
   *
   * @param box
   * @return
   */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    return processBox(box);
  }

  private boolean processBox(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      final long y = node.getY();
      final long height = node.getOverflowAreaHeight();
      if (isFiltered(y, height))
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else
      {
        node = node.getNext();
      }
    }
    return true;
  }

  private boolean isFiltered(final long y, final long height)
  {
      // Special treatment for lines, which have a height of zero.
    if (y == contentStart && height == 0)
    {
      return false;
    }
    else if ((y + height) <= contentStart)
    {
      return true;
    }
    else if (y >= contentEnd)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    return processBox(box);
  }

  protected boolean startRowLevelBox(final RenderBox box)
  {
    return processBox(box);
  }

  protected boolean startTableLevelBox(final RenderBox box)
  {
    if (box.getNodeType() == LayoutNodeTypes.TYPE_BOX_TABLE_SECTION)
    {
      final TableSectionRenderBox tsr = (TableSectionRenderBox) box;
      if (tsr.getDisplayRole() == TableSectionRenderBox.Role.BODY)
      {
        return processBox(box);
      }
    }
    return false;
  }

  protected boolean startTableSectionLevelBox(final RenderBox box)
  {
    return processBox(box);
  }

  protected boolean startTableRowLevelBox(final RenderBox box)
  {
    return processBox(box);
  }
  
  protected boolean startTableCellLevelBox(final RenderBox box)
  {
    return processBox(box);
  }
}