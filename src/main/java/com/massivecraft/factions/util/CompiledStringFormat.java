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
package com.massivecraft.factions.util;

import java.util.Arrays;

public final class CompiledStringFormat {
	private static final int NUMBER_LENGTH_GUESS = 10;

	private static final int OBJECT_LENGTH_GUESS = 20;

	private final String[] messageParts;

	private final int length;

	CompiledStringFormat(String[] messageParts, int length) {
		this.messageParts = messageParts;
		this.length = length;
	}

	public String format(Object arg0) {
		StringBuilder builder = new StringBuilder(length + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0);
		return builder.toString();
	}

	public String format(int arg0) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0);
		return builder.toString();
	}

	public String format(long arg0) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0);
		return builder.toString();
	}

	public String format(double arg0) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(Object arg0, int arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(Object arg0, long arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(Object arg0, double arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(int arg0, Object arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(int arg0, int arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(int arg0, long arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(int arg0, double arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(long arg0, Object arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(long arg0, int arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(long arg0, long arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(long arg0, double arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(double arg0, Object arg1) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(double arg0, int arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(double arg0, long arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(double arg0, double arg1) {
		StringBuilder builder = new StringBuilder(length + 2 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, int arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, int arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, int arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, int arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, long arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, long arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, long arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, long arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, double arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, double arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, double arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(int arg0, double arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, int arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, int arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, int arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, int arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, long arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, long arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, long arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, long arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, double arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, double arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, double arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(long arg0, double arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, int arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, int arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, int arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, int arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, long arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, long arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, long arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, long arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, double arg1, Object arg2) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, double arg1, int arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, double arg1, long arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(double arg0, double arg1, double arg2) {
		StringBuilder builder = new StringBuilder(length + 3 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, Object arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, int arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, long arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(Object arg0, double arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, Object arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, int arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, long arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(int arg0, double arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, Object arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, int arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, long arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(long arg0, double arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + NUMBER_LENGTH_GUESS + 3*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, Object arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, int arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, long arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, Object arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 2*NUMBER_LENGTH_GUESS + 2*OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, Object arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, Object arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, Object arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, int arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, int arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, int arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, int arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, long arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, long arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, long arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, long arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, double arg2, Object arg3) {
		StringBuilder builder = new StringBuilder(length + 3*NUMBER_LENGTH_GUESS + OBJECT_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, double arg2, int arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, double arg2, long arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public String format(double arg0, double arg1, double arg2, double arg3) {
		StringBuilder builder = new StringBuilder(length + 4 * NUMBER_LENGTH_GUESS);
		appendTo(builder, arg0, arg1, arg2, arg3);
		return builder.toString();
	}

	public void appendTo(StringBuilder builder, Object arg0) {
		if (messageParts.length != 2) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
	}

	public void appendTo(StringBuilder builder, int arg0) {
		if (messageParts.length != 2) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
	}

	public void appendTo(StringBuilder builder, long arg0) {
		if (messageParts.length != 2) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
	}

	public void appendTo(StringBuilder builder, double arg0) {
		if (messageParts.length != 2) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1) {
		if (messageParts.length != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, Object arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, int arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, long arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, double arg2) {
		if (messageParts.length != 4) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, Object arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, int arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, long arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, Object arg0, double arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		ToStrings.deepAppendObject(builder, arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, Object arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, int arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, long arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, int arg0, double arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, Object arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, int arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, long arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, long arg0, double arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, Object arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		ToStrings.deepAppendObject(builder, arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, int arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, long arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, Object arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, Object arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, Object arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, Object arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		ToStrings.deepAppendObject(builder, arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, int arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, int arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, int arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, int arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, long arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, long arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, long arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, long arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, double arg2, Object arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		ToStrings.deepAppendObject(builder, arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, double arg2, int arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, double arg2, long arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public void appendTo(StringBuilder builder, double arg0, double arg1, double arg2, double arg3) {
		if (messageParts.length != 5) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: [" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + "]");
		}
		builder.append(messageParts[0]);
		builder.append(arg0);
		builder.append(messageParts[1]);
		builder.append(arg1);
		builder.append(messageParts[2]);
		builder.append(arg2);
		builder.append(messageParts[3]);
		builder.append(arg3);
		builder.append(messageParts[4]);
	}

	public String formatObjects(Object... args) {
		StringBuilder builder = new StringBuilder(length + args.length*OBJECT_LENGTH_GUESS);
		appendObjectsTo(builder, args);
		return builder.toString();
	}

	public String formatStrings(String... args) {
		StringBuilder builder = new StringBuilder(length + args.length*OBJECT_LENGTH_GUESS);
		appendStringsTo(builder, args);
		return builder.toString();
	}

	public String formatInts(int... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendIntsTo(builder, args);
		return builder.toString();
	}

	public String formatLongs(long... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendLongsTo(builder, args);
		return builder.toString();
	}

	public String formatDoubles(double... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendDoublesTo(builder, args);
		return builder.toString();
	}

	public String formatBytes(byte... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendBytesTo(builder, args);
		return builder.toString();
	}

	public String formatChars(char... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendCharsTo(builder, args);
		return builder.toString();
	}

	public String formatBooleans(boolean... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendBooleansTo(builder, args);
		return builder.toString();
	}

	public String formatFloats(float... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendFloatsTo(builder, args);
		return builder.toString();
	}

	public String formatShorts(short... args) {
		StringBuilder builder = new StringBuilder(length + args.length*NUMBER_LENGTH_GUESS);
		appendShortsTo(builder, args);
		return builder.toString();
	}

	public void appendObjectsTo(StringBuilder builder, Object... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			ToStrings.deepAppendObject(builder, args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendStringsTo(StringBuilder builder, String... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendIntsTo(StringBuilder builder, int... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendLongsTo(StringBuilder builder, long... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendDoublesTo(StringBuilder builder, double... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendBytesTo(StringBuilder builder, byte... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendCharsTo(StringBuilder builder, char... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendBooleansTo(StringBuilder builder, boolean... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendFloatsTo(StringBuilder builder, float... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}

	public void appendShortsTo(StringBuilder builder, short... args) {
		final int argCount = args.length;
		if (messageParts.length != (argCount + 1)) {
			throw new IllegalArgumentException("Incorrect number of arguments, message parts: " + Arrays.toString(messageParts) + ", args: " + Arrays.toString(args));
		}
		for (int i = 0; i < argCount; i++) {
			builder.append(messageParts[i]);
			builder.append(args[i]);
		}
		builder.append(messageParts[argCount]);
	}
}
