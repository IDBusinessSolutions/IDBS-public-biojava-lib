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
 * This class is a clone of ByHierarchy with the following modifications:
 *
 * Usages of 'FeatureFilter' have been replaced with 'BjFeatureFilter'
 *
 * Copyright (C) 1993-2017 ID Business Solutions Limited
 */

package com.idbs.biojava.viewer.filter;

interface ByHierarchy extends BjFeatureFilter {
  BjFeatureFilter getFilter();
}

