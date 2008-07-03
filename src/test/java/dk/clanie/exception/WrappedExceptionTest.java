/**
 * Copyright (C) 2007, Claus Nielsen, cn@cn-consult.dk
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package dk.clanie.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WrappedExceptionTest {

    @Test
    public void testWrappedException() {
        WrappedException ex = new WrappedException(new RuntimeException("TEST."));
        assertEquals("java.lang.RuntimeException: TEST.", ex.getMessage());
        assertEquals("java.lang.RuntimeException", ex.getWrappedExceptionClassName());
        assertEquals("TEST.", ex.getWrappedMessage());
        String trace = ex.getWrappedStacktrace();
        assertTrue(trace.contains("java.lang.RuntimeException: TEST."));
        assertTrue(trace.contains(getClass().getName() + ".testWrappedException"));
    }

}
