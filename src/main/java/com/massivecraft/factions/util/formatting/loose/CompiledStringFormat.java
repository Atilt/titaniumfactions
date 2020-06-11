// Copyright (c) 2004-2015 QOS.ch, Petr Janeček
// All rights reserved.
// 
// Permission is hereby granted, free  of charge, to any person obtaining
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

import java.util.Arrays;

public final class CompiledStringFormat {

	private static final int OBJECT_LENGTH_GUESS = 20;

	private final String[] messageParts;
	private final int length;

	CompiledStringFormat(String[] messageParts, int length) {
		this.messageParts = messageParts;
		this.length = length;
	}

	public String format(String... args) {
		return appendStringsTo(new StringBuilder(this.length + args.length * OBJECT_LENGTH_GUESS), args).toString();
	}

	private StringBuilder appendStringsTo(StringBuilder builder, String... args) {
		int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		return builder.append(messageParts[argCount]);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CompiledStringFormat that = (CompiledStringFormat) o;

		if (length != that.length) return false;
		return Arrays.equals(messageParts, that.messageParts);
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(messageParts);
		result = 31 * result + length;
		return result;
	}
}
