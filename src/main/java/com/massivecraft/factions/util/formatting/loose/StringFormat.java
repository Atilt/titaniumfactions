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
package com.massivecraft.factions.util.formatting.loose;

import java.util.ArrayList;
import java.util.List;

public class StringFormat {

	public static final String DELIMITER = "[]";
    private static final char DELIMITER_START = '[';
    private static final char ESCAPE_CHAR = '\\';

    static final int OBJECT_LENGTH_GUESS = 20;
    
    public static String format(String messagePattern, Object... args) {
        if ((messagePattern == null) || (args == null) || (args.length == 0)) {
            return messagePattern;
        }

        StringBuilder builder = new StringBuilder(messagePattern.length() + (OBJECT_LENGTH_GUESS * args.length));
        int currentIndex = 0;
        for (int currentArgIndex = 0; currentArgIndex < args.length; currentArgIndex++) {
            final int delimiterIndex = messagePattern.indexOf(DELIMITER, currentIndex);

            if (delimiterIndex == -1) {
            	// no more delimiters found
                if (currentIndex == 0) {
                	// this is a simple string
                    return messagePattern;
                }
                // add the tail string which contains no variables and return the result.
                builder.append(messagePattern.substring(currentIndex));
                return builder.toString();
            }
            
			if (isDelimiterEscaped(messagePattern, delimiterIndex)) {
			    if (isDelimiterDoublyEscaped(messagePattern, delimiterIndex)) {
			        // The escape character preceding the delimiter start is itself escaped: "abc x:\\{}"
			        // we have to consume one backward slash
			        builder.append(messagePattern, currentIndex, delimiterIndex - 1);
			        ToStrings.deepAppendObject(builder, args[currentArgIndex]);
			        currentIndex = delimiterIndex + 2;
			    } else {
			        currentArgIndex--;	// DELIM_START was escaped, thus should not be incremented
			        builder.append(messagePattern, currentIndex, delimiterIndex - 1);
			        builder.append(DELIMITER_START);
			        currentIndex = delimiterIndex + 1;
			    }
			} else {
			    // normal case
			    builder.append(messagePattern, currentIndex, delimiterIndex);
			    ToStrings.deepAppendObject(builder, args[currentArgIndex]);
			    currentIndex = delimiterIndex + 2;
			}
        }
        
        // append the characters following the last {} pair.
        builder.append(messagePattern.substring(currentIndex));
		return builder.toString();
    }
    
    public static CompiledStringFormat compile(String messagePattern) {
    	List<String> messageParts = new ArrayList<>();
    	
    	int index = 0;
    	int searchFromIndex = 0;
    	while (index != -1) {
    		int delimiterIndex = messagePattern.indexOf(DELIMITER, searchFromIndex);
    		
            if (delimiterIndex == -1) {
                messageParts.add(messagePattern.substring(index));
        		index = searchFromIndex = delimiterIndex;
            } else {
    			if (isDelimiterEscaped(messagePattern, delimiterIndex)) {
    			    if (isDelimiterDoublyEscaped(messagePattern, delimiterIndex)) {
    			        // The escape character preceding the delimiter start is itself escaped: "abc x:\\{}"
    			        // we have to consume one backward slash
    	                messageParts.add(messagePattern.substring(index, delimiterIndex - 1));
    	                index = searchFromIndex = delimiterIndex + 2;
    			    } else {
    			    	messagePattern = new StringBuilder(messagePattern).deleteCharAt(delimiterIndex - 1).toString();
						searchFromIndex = delimiterIndex + 1;	// skip assigning index
    			    }
    			} else {
    			    // normal case
	                messageParts.add(messagePattern.substring(index, delimiterIndex));
	                index = searchFromIndex = delimiterIndex + 2;
    			}
            }
    	}
    	
    	return new CompiledStringFormat(messageParts.toArray(new String[0]), messagePattern.length());
    }
    
    private static boolean isDelimiterEscaped(String messagePattern, int delimiterStartIndex) {
        return isEscaped(messagePattern, delimiterStartIndex, 1);
    }
    
    private static boolean isDelimiterDoublyEscaped(String messagePattern, int delimiterStartIndex) {
        return isEscaped(messagePattern, delimiterStartIndex, 2);
    }

	private static boolean isEscaped(String messagePattern, int delimiterStartIndex, int offset) {
		if (delimiterStartIndex < offset) {
            return false;
        }
        return (messagePattern.charAt(delimiterStartIndex - offset) == ESCAPE_CHAR);
	}
}
