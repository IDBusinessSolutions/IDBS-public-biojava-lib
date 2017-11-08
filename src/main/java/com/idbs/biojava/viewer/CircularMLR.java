/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

package com.idbs.biojava.viewer;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.biojava.bio.gui.sequence.*;


/**
 * Renders multiple renderers, each in their own concentric rings.
 *
 * @author Matthew Pocock
 * @since 1.4
 */
public class CircularMLR
implements CircularRenderer {
  private List renderers = new ArrayList();

  public void addRenderer(CircularRenderer renderer) {
    renderers.add(renderer);
  }

  public void removeRenderer(CircularRenderer renderer) {
    renderers.remove(renderer);
  }

  public double getDepth(CircularRendererContext crc) {
    double depth = 0;

    for(Iterator i = renderers.iterator(); i.hasNext(); ) {
      CircularRenderer rend = (CircularRenderer) i.next();
      CircularRendererContext subCtxt = new SubCircularRendererContext(
              crc,
              null,
              null,
              crc.getRadius() + depth);
      depth += rend.getDepth(subCtxt);
    }

    return depth;
  }

  public void paint(Graphics2D g2, CircularRendererContext crc) {
    double depth = 0.0;

    for(Iterator i = renderers.iterator(); i.hasNext(); ) {
      CircularRenderer rend = (CircularRenderer) i.next();
      CircularRendererContext subCtxt = new SubCircularRendererContext(
              crc,
              null,
              null,
              crc.getRadius() + depth);
      rend.paint(g2, subCtxt);
      depth += rend.getDepth(crc);
    }
  }
/*
    public SequenceViewerEvent processMouseEvent(CircularRendererContext src,
                                                 MouseEvent me,
                                                 List path) {
        path.add(this);
        SequenceViewerEvent sve =
            null;
            LayeredRenderer.INSTANCE.processMouseEvent(Collections.nCopies(renderers.size(), src),
                                                       me,
                                                       path,
                                                       renderers);

        if (sve == null) {
            sve = new SequenceViewerEvent(this,
                                          null,
                                          src.graphicsToSequence(me.getPoint()),
                                          me,
                                          path);
        }
        return sve;
    }
*/
}
