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

/**
 * This class is a clone of IsTopLevel with the following modifications:
 *
 * Usages of 'FeatureFilter' have been replaced with 'BjFeatureFilter'
 *
 * Copyright (C) 1993-2017 ID Business Solutions Limited
 */

package com.idbs.biojava.viewer.filter;

import org.biojava.bio.seq.*;

/**
 * Accept features which are top-level sequence features.  This is implemented
 * by the logic that the <code>parent</code> property of top-level features
 * will implement the <code>Sequence</code> interface.
 *
 * @author Thomas Down
 * @since 1.3
 */

final class IsTopLevel implements OptimizableFilter {
	private static final long serialVersionUID = 7059438664829531006L;

    public boolean accept(Feature f) {
        return f.getParent() instanceof Sequence;
    }

    public int hashCode() {
      return 42;
    }

    /**
    * All instances are equal (this should really be a singleton, but
    * that doesn't quite fit current </code>FeatureFilter</code>
    * patterns.
    */

    public boolean equals(Object o) {
        return (o instanceof IsTopLevel);
    }

    public boolean isProperSubset(BjFeatureFilter ff) {
        return (ff instanceof IsTopLevel) || (ff instanceof AcceptAllFilter);
    }

    public boolean isDisjoint(BjFeatureFilter ff) {
        return (ff instanceof ByParent) || (ff instanceof ByAncestor);
    }
}

