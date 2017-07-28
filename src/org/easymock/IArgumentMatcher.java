/*
 * Copyright (c) 2001-2006 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

/**
 * Decides whether an actual argument is accepted.
 */
public interface IArgumentMatcher {
    /**
     * Returns whether this matcher accepts the given argument.
     * @param argument the argument
     * @return whether this matcher accepts the given argument.
     */
    boolean matches(Object argument);

    /**
     * Appends a string representation of this matcher to the given buffer.
     * @param buffer the buffer to which the string representation is appended.
     */
    void appendTo(StringBuffer buffer);
}
