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
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.*;

import org.biojava.bio.gui.sequence.*;
import org.biojava.bio.symbol.*;

/**
 * This class is a clone of {@link RulerRenderer} with modifications to the
 * rendering code.
 * 
 * Modifications include corrected ruler extent and enhancements to label
 * visibility. 
 * 
 * <p><code>BjRulerRenderer</code> renders numerical scales in sequence
 * coordinates. The tick direction may be set to point upwards (or
 * left when the scale is vertical) or downwards (right when the scale
 * is vertical).</p>
 *
 * <p>Note: The Compaq Java VMs 1.3.1 - 1.4.0 on Tru64 appear to have
 * a bug in font transformation which prevents a vertically oriented
 * ruler displaying correctly rotated text.</p>
 *
 * @author Matthew Pocock
 * @author Thomas Down
 * @author David Huen
 * @author Keith James
 * @author Kalle Näslund
 */
public class BjRulerRenderer implements SequenceRenderer
{
    /**
     * <code>TICKS_UP</code> indicates that the ticks will point
     * upwards from a baseline.
     */
    public static final int TICKS_UP = 0;
    /**
     * <code>TICKS_DOWN</code> indicates that the ticks will point
     * downwards from a baseline.
     */
    public static final int TICKS_DOWN = 1;

    private Line2D            line;
    private double            depth;
    private AffineTransform   antiQuarter;
    private int               tickDirection;
    private float             tickHeight;
    private float             horizLabelOffset;
    private float             vertLabelOffset;
    private final JComponent  parentComponent;
    
    /**
     * Creates a new <code>BjRulerRenderer</code> with the default
     * setting of ticks pointing downwards.
     * @param parent the parent component of this ruler.
     */
    public BjRulerRenderer(JComponent parent) throws IllegalArgumentException
    {
        this(TICKS_DOWN, parent);
    }

    /**
     * Creates a new <code>BjRulerRenderer</code> with the specified
     * tick direction.
     *
     * @param tickDirection an <code>int</code>.
     * @param parent the parent component of this ruler.
     * @exception IllegalArgumentException if an error occurs.
     */
    public BjRulerRenderer(int tickDirection, JComponent parent) throws IllegalArgumentException
    {
        this.parentComponent = parent;

        line   = new Line2D.Double();
        antiQuarter = AffineTransform.getRotateInstance(Math.toRadians(-90));

        if (tickDirection == TICKS_UP || tickDirection == TICKS_DOWN)
            this.tickDirection = tickDirection;
        else
            throw new IllegalArgumentException("Tick direction may only be set to BjRulerRenderer.TICKS_UP or BjRulerRenderer.TICKS_DOWN"); //$NON-NLS-1$

        depth      = 20.0;
        tickHeight = 4.0f;

        horizLabelOffset = ((float) depth) - tickHeight - 2.0f;
        vertLabelOffset  = ((float) depth) - ((tickHeight + 2.0f) * 2.0f);
    }

    public double getMinimumLeader(SequenceRenderContext context)
    {
        return 0.0;
    }

    public double getMinimumTrailer(SequenceRenderContext context)
    {
        return 0.0;
    }

    public double getDepth(SequenceRenderContext src)
    {
        return depth + 1.0;
    }

    public void paint(Graphics2D g2, SequenceRenderContext context)
    {
        AffineTransform prevTransform = g2.getTransform();

        // Commented out to allow the ruler to be rendered using the current
        // foreground colour rather than a hardcoded value:
        // g2.setPaint(Color.black);

        Location visible = GUITools.getVisibleRange(context, g2);
        if( visible == Location.empty ) {
            return;
        }
        
        int min = visible.getMin();
        int max = visible.getMax();
        double minX = context.sequenceToGraphics(min);
        double maxX = context.sequenceToGraphics(max + 1);
        double scale = context.getScale();

        if (context.getDirection() == SequenceRenderContext.HORIZONTAL)
        {
            if (tickDirection == TICKS_UP)
            {
                line.setLine(minX, depth,
                             maxX, depth);
            }
            else
            {
                line.setLine(minX, 0.0,
                             maxX, 0.0);
            }
        }
        else
        {
            if (tickDirection == TICKS_UP)
            {
                line.setLine(depth, minX,
                             depth, maxX);
            }
            else
            {
                line.setLine(0.0, minX,
                             0.0, maxX);
            }
        }

        g2.draw(line);

        FontMetrics fMetrics = g2.getFontMetrics();

        // The widest (== maxiumum) coordinate to draw
        int coordWidth = fMetrics.stringWidth(Integer.toString(max));

        // Minimum gap getween ticks
        double minGap = (double) Math.max(coordWidth, 40);

        // How many symbols does a gap represent?
        int realSymsPerGap = (int) Math.ceil(((minGap + 5.0) / context.getScale()));

        // We need to snap to a value beginning 1, 2 or 5.
        double exponent = Math.floor(Math.log(realSymsPerGap) / Math.log(10));
        double characteristic = realSymsPerGap / Math.pow(10.0, exponent);

        int snapSymsPerGap;
        if (characteristic > 5.0)
        {
            // Use unit ticks
            snapSymsPerGap = (int) Math.pow(10.0, exponent + 1.0);
        }
        else if (characteristic > 2.0)
        {
            // Use ticks of 5
            snapSymsPerGap = (int) (5.0 * Math.pow(10.0, exponent));
        }
        else
        {
            snapSymsPerGap = (int) (2.0 * Math.pow(10.0, exponent));
        }

        int minP = (min / snapSymsPerGap + 1) * (snapSymsPerGap);

        double halfScale = scale * 0.5;

        Rectangle viewRect = null;
        if (parentComponent != null)
        {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, parentComponent);
            if (viewPort != null)
            {
                viewRect = viewPort.getViewRect();                
            }
            else
            {
                viewRect = SwingUtilities.calculateInnerArea(parentComponent, null);
            }
        }

        for (int index = minP; index <= max; index += snapSymsPerGap)
        {
            double offset = context.sequenceToGraphics(index);
            String labelString = String.valueOf(index);
            float halfLabelWidth = fMetrics.stringWidth(labelString) / 2;

            final int LABEL_MARGIN = 2;
            float adjustedLabelCoord = (float) (offset + halfScale - halfLabelWidth);
            if (viewRect != null)
            {
                adjustedLabelCoord = (float) Math.max(viewRect.getMinX() + LABEL_MARGIN, adjustedLabelCoord);
                adjustedLabelCoord = (float) Math.min(viewRect.getMaxX() - 2 * halfLabelWidth - LABEL_MARGIN, adjustedLabelCoord);
            }
            
            // The ticks and tick labels are placed on the centre of the residue,
            // not between residues.
            if (context.getDirection() == SequenceRenderContext.HORIZONTAL)
            {
                if (tickDirection == TICKS_UP)
                {
                    line.setLine(offset + halfScale, depth - tickHeight,
                                 offset + halfScale, depth);
                    g2.drawString(String.valueOf(index),
                                  adjustedLabelCoord,
                                  horizLabelOffset);
                }
                else
                {
                    line.setLine(offset + halfScale, 0.0,
                                 offset + halfScale, tickHeight);
                    g2.drawString(String.valueOf(index),
                                  adjustedLabelCoord,
                                  horizLabelOffset);
                }
            }
            else
            {
                if (tickDirection == TICKS_UP)
                {
                    line.setLine(depth, offset + halfScale,
                                 depth - tickHeight, offset + halfScale);
                    g2.translate(vertLabelOffset,
                                 offset + halfScale + halfLabelWidth);
                    g2.transform(antiQuarter);
                    g2.drawString(String.valueOf(index), 0.0f, 0.0f);
                    g2.setTransform(prevTransform);
                }
                else
                {
                    line.setLine(0.0f, offset + halfScale,
                                 tickHeight, offset + halfScale);
                    g2.translate(vertLabelOffset,
                                 offset + halfScale + halfLabelWidth);
                    g2.transform(antiQuarter);
                    g2.drawString(String.valueOf(index), 0.0f, 0.0f);
                    g2.setTransform(prevTransform);
                }
            }
            g2.draw(line);
        }
    }

    public SequenceViewerEvent processMouseEvent(SequenceRenderContext context,
                                                 MouseEvent            me,
                                                 List                  path)
    {
        path.add(this);
        int sPos = context.graphicsToSequence(me.getPoint());
        return new SequenceViewerEvent(this, null, sPos, me, path);
    }
}
