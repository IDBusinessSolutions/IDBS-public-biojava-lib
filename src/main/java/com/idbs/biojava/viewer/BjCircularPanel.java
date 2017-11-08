/*****************************************************************************\
 *  SEE NOTES ON EWB DEVELOPMENT BELOW LICENCE NOTICE.
 /*****************************************************************************/
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

/*****************************************************************************\
 *  In order to maintain the architecture for the EWB code, and to allow for 
 *  easy extension the original class 
 *  
 *  org.biojava.bio.gui.sequence.CircularRendererPanel
 *  
 *  has been copied here, and changes made. These changes are...
 *  
 *  1) The name has changed
 *  2) The type has changed from concrete to abstract
 *  
 *  The effect of these is to alow the BjCircularTrack component behave 
 *  identically to BaseBackbone and BaseMetadata.
 *  
 *  3) The CircularRendererContext is modified to protected
 *  
 *  This allows the child classes to resize based on the ctxt, which holds 
 *  the track depth.
 *  
 *  4) The paintComponent(Graphics) method has been removed
 *  
 *  This allows the child classes to to paint feature selection arcs 
 *  correctly. It may be relavant to reinstate this if the selection 
 *  arc is placed on an overlay panel.
 *  
/*****************************************************************************/
package com.idbs.biojava.viewer;

import javax.swing.*;

import org.biojava.bio.gui.sequence.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.symbol.*;


/**
 * Renders a sequence as a circle using a CircularRenderer.
 *
 * <p>
 * This component will first transform the graphic coordinates so that 0,0 is at
 * the centre of the circle. The size of the circle is estimated from the radius
 * property and the depth of the renderer.
 * </p>
 *
 * <p>
 * All angles are measured in radians. Some java gui classes use radians and
 * some use degrees. Be carefull to use the right one. Math has a couple
 * of methods for conversions.
 * </p>
 *
 * @author Matthew Pocock
 * @since 1.4
 */
public abstract class BjCircularPanel
extends JComponent {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5412717875694647789L;
protected final CircularRendererContext ctxt;
  {
    ctxt = new CTXT();
  }

  private SymbolList symList;
  private double radius;
  private CircularRenderer renderer;
  private double offset;

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }

  public SymbolList getSequence() {
    return symList;
  }

  public void setSequence(SymbolList symList) {
    this.symList = symList;
  }

  public double getOffset() {
    return offset;
  }

  public void setOffset(double offset) {
    this.offset = offset;
  }

  public CircularRenderer getRenderer() {
    return renderer;
  }

  public void setRenderer(CircularRenderer renderer) {
    this.renderer = renderer;
  }

/*
  public synchronized void paintComponent(Graphics g) {
    super.paintComponent(g);
    if(!isActive()) return;

    double depth = renderer.getDepth(ctxt);

    Graphics2D g2 = (Graphics2D) g;
    g2.translate((depth + radius), (depth + radius));

    renderer.paint(g2, ctxt);
  }
*/

  private boolean isActive() {
    return renderer != null;
  }

  private final class CTXT
  implements CircularRendererContext {
    public double getOffset() {
      return offset;
    }

    public double getAngle(int indx) {
      return ((double) indx) * 2.0 * Math.PI / ((double) symList.length()) + offset;
    }

    public int getIndex(double angle) {
      return (int) ( (angle - offset) * ((double) symList.length()) / (2.0 * Math.PI));
    }

    public double getRadius() {
      return radius;
    }

    public SymbolList getSymbols() {
      return symList;
    }

    public FeatureHolder getFeatures() {
      if(symList instanceof FeatureHolder) {
        return (FeatureHolder) symList;
      } else {
        return FeatureHolder.EMPTY_FEATURE_HOLDER;
      }
    }
  }
  
}
