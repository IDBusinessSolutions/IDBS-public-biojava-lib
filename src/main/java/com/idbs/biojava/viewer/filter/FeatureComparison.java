/**
 * Copyright (C) 1993-2017 ID Business Solutions Limited
 * All rights reserved
 *  
 * Created by: rtuck
 * Created Date: Sep 29, 2011
 *  
 * Last changed: $Header: $ 
 */
package com.idbs.biojava.viewer.filter;

import org.biojava.bio.symbol.*;

/**
 * This class replaces the location comparison code in BioJava,
 * which is currently broken for circular sequences.
 *
 * <p><b>(C) Copyright 2011 IDBS Ltd</b></p>
 */
public class FeatureComparison
{
    /**
     * Determine if there is any overlap between the two locations.
     * @param l1 location 1
     * @param l2 location 2
     * @return true if the locations overlap
     */
    public static boolean overlaps(Location l1, Location l2)
    {
        int start1 = l1.getMin();
        int start2 = l2.getMin();
        int end1 = l1.getMax();
        int end2 = l2.getMax();

        return overlaps(start1, end1, start2, end2);
    }

    /**
     * If either start location is past the end, assume the location represents
     * circular wraparound and use recursion to solve
     * @return true if the locations overlap
     */
    public static boolean overlaps(int start1, int end1, int start2, int end2)
    {
        if (start1 > end1)
        {
            return overlaps(start1, Integer.MAX_VALUE, start2, end2) ||
                   overlaps(Integer.MIN_VALUE, end1, start2, end2);
        }
        if (start2 > end2)
        {
            return overlaps(start1, end1, start2, Integer.MAX_VALUE) ||
                   overlaps(start1, end1, Integer.MIN_VALUE, end2);
        }
        else
        {
            return start1 <= end2 && start2 <= end1;
        }
    }
}
