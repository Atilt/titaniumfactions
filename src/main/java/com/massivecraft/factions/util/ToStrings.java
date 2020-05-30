// Copyright (c) 2004-2015 QOS.ch, Petr Janeček
// All rights reserved.
// 
// Permission is hereby granted, free  of charge, to any person obtaining
// a  copy  of this  software  and  associated  documentation files  (the
// "Software"), to  deal in  the Software without  restriction, including
// without limitation  the rights to  use, copy, modify,  merge, publish,
// distribute,  sublicense, and/or sell  copies of  the Software,  and to
// permit persons to whom the Software  is furnished to do so, subject to
// the following conditions:
// 
// The  above  copyright  notice  and  this permission  notice  shall  be
// included in all copies or substantial portions of the Software.
// 
// THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
// EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
// MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package com.massivecraft.factions.util;

import java.util.HashSet;
import java.util.Set;

class ToStrings {
	
	private ToStrings() {
		// a static utility class
	}
	
    public static void deepAppendObject(StringBuilder builder, Object messageArg) {
		deepAppendObject(builder, messageArg, null);
    }
    
    private static void deepAppendObject(StringBuilder builder, Object messageArg, Set<Object[]> seenSet) {
        if (messageArg == null) {
        	builder.append("null");
            return;
        }
        
        if (!messageArg.getClass().isArray()) {
        	builder.append(messageArg.toString());	// using toString() directly for speed, it's safe as we already handled null
        } else {
        	builder.append('[');
        	if (messageArg instanceof Object[]) {
        		// TODO measure whether this makes any observable difference
//        		appendObjectArray(builder, (Object[])messageArg,
//        				(seenSet != null) ? seenSet : Collections.newSetFromMap(new IdentityHashMap<>()));
        		appendObjectArray(builder, (Object[])messageArg, (seenSet != null) ? seenSet : new HashSet<>());
        	} else if (messageArg instanceof int[]) {
            	appendIntArray(builder, (int[])messageArg);
            } else if (messageArg instanceof long[]) {
            	appendLongArray(builder, (long[])messageArg);
            } else if (messageArg instanceof double[]) {
            	appendDoubleArray(builder, (double[])messageArg);
            } else if (messageArg instanceof byte[]) {
                appendByteArray(builder, (byte[])messageArg);
            } else if (messageArg instanceof char[]) {
                appendCharArray(builder, (char[])messageArg);
            } else if (messageArg instanceof boolean[]) {
            	appendBooleanArray(builder, (boolean[])messageArg);
            } else if (messageArg instanceof short[]) {
                appendShortArray(builder, (short[])messageArg);
            } else {
                appendFloatArray(builder, (float[])messageArg);
            }
        	builder.append(']');
        }
    }
    
    private static void appendObjectArray(StringBuilder builder, Object[] array, Set<Object[]> seenSet) {
    	if (array.length > 0) {
    		if (!seenSet.contains(array)) {
    			// an array used as a key in a hash-based collection is okay here, we'd need an IdentityHashSet anyway
            	seenSet.add(array);
            	final int max = array.length - 1;
            	for (int i = 0; i < max; i++) {
            		deepAppendObject(builder, array[i], seenSet);
            		builder.append(", ");
            	}
            	deepAppendObject(builder, array[max], seenSet);
            	seenSet.remove(array);	// allow repeats in siblings
	        } else {
	            builder.append("...");
	        }
    	}
    }

	private static void appendIntArray(StringBuilder builder, int[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendLongArray(StringBuilder builder, long[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendDoubleArray(StringBuilder builder, double[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendByteArray(StringBuilder builder, byte[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendCharArray(StringBuilder builder, char[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendBooleanArray(StringBuilder builder, boolean[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendShortArray(StringBuilder builder, short[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}

	private static void appendFloatArray(StringBuilder builder, float[] array) {
	    if (array.length > 0) {
	    	final int max = array.length - 1;
	    	for (int i = 0; i < max; i++) {
	    		builder.append(array[i]);
	    		builder.append(", ");
	    	}
	    	builder.append(array[max]);
	    }
	}
}
