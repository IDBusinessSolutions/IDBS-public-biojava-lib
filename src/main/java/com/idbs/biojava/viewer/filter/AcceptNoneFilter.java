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

package com.idbs.biojava.viewer.filter;

import org.biojava.bio.seq.*;

/**
 * The class that accepts no features.
 * <p>
 * Use the FeatureFilter.none member.
 *
 * @author Matthew Pocock
 * @since 1.2
 */
class AcceptNoneFilter implements OptimizableFilter {
  /**
	 * 
	 */
	private static final long serialVersionUID = 6055301571935960951L;

protected AcceptNoneFilter() {}
  
  public boolean accept(Feature f) { return false; }
  
  public boolean equals(Object o) {
    return o instanceof AcceptNoneFilter;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public boolean isProperSubset(BjFeatureFilter sup) {
    return true;
  }
  
  public boolean isDisjoint(BjFeatureFilter filt) {
    return true;
  }
  
  public String toString() {
    return "None";
  }
}

