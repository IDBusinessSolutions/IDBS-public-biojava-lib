/**
 * Copyright (C) 1993-2011 ID Business Solutions Limited
 * All rights reserved
 *  
 * Created by: md'costa
 * Created Date: 06-Jul-2011
 *  
 * Last changed:
 */
package com.idbs.biojava.parser;

import java.io.*;
import java.util.regex.*;

import org.biojava.bio.seq.io.*;
import org.biojavax.bio.seq.*;
import org.biojavax.bio.seq.io.FastaFormat;

/**
 * This Class extends the {@link FastaFormat} to provide specific functionality.
 */
public class BjFastaFormat extends FastaFormat
{
    //Upper case = Blofeld          Original Version
    //    protected static final Pattern PROTEINS = Pattern.compile(".*[BDEFHIKLMNOPQRSVWYZX].*"); //$NON-NLS-1$
    //U+L case   = Blofeld          Update to include lower symbols for Yorkie
          protected static final Pattern PROTEINS = Pattern.compile(".*[BbDdEeFfHhIiKkLlMmNnOoPpQqRrSsVvWwYyZzXx].*"); //$NON-NLS-1$

    /**
     * (non-Javadoc)
     * 
     * @see org.biojavax.bio.seq.io.FastaFormat#guessSymbolTokenization(java.io.BufferedInputStream)
     */
    @Override
    public SymbolTokenization guessSymbolTokenization(final BufferedInputStream stream) throws IOException
    {
        final BufferedReader bufReader = new BufferedReader(new InputStreamReader(stream));
        getString(bufReader); // discard first line

        String string = getString(bufReader);
        final boolean line1 = PROTEINS.matcher(string).matches();
        String string2 = getString(bufReader);
        final boolean line2 = PROTEINS.matcher(string2).matches();
        // don't close the reader as it'll close the stream too.
        // br.close();
        SymbolTokenization tok = null;
        stream.reset();
        // (x!y) :This is done to ensure that for ambiguous sequences the nucleotide parser is detected first.
        tok = RichSequence.IOTools.getProteinParser();
        if (!line1)
        {
            if (!line2)
            {
                tok = RichSequence.IOTools.getNucleotideParser();
            }
        }

        return tok;

    }

    /**
     * @param bufReader
     * @return
     * @throws IOException
     */
    private String getString(final BufferedReader bufReader) throws IOException
    {
        String bufStr = bufReader.readLine();
        return (bufStr == null ? "" : bufStr); //$NON-NLS-1$
    }
}
