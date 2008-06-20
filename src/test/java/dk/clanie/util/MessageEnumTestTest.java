/**
 * Copyright (C) 2008, Claus Nielsen, cn@cn-consult.dk.dk
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
package dk.clanie.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.ListResourceBundle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import junit.framework.AssertionFailedError;

import org.junit.Test;

import dk.clanie.test.AbstractMessageEnumTest;

/**
 * Test that AbstractMessageEnumTest works.
 * 
 * @author Claus Nielsen
 */
public class MessageEnumTestTest extends AbstractMessageEnumTest {

	private static class MsgBundle extends ListResourceBundle {
		protected Object[][] getContents() {
			return new Object[][] {
					{ "M1", "Message One" },
					{ "M2", "Message Two" }, };
		}
	};
	private static final ResourceBundle bundle = new MsgBundle();

	// A Message Enumeration which exactly matches the ResourceBundle.
	// This should pass the test.
	private enum MsgEnum implements Message {
		M1, M2;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	// A Message Enumeration with one less entry than the
	// ResourceBundle. This should fail the test.
	private enum MsgEnumMissingAnEntry implements Message {
		M1;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	// A Message Enumeration with an entry which isn't defined in the
	// ResourceBundle. This should fail the test.
	private enum MsgEnumWithExtraEntry implements Message {
		M1, M2, M3;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	@Test
	public void testCorrectMessageEnumeration() {
		testMessageEnumeration(MsgEnum.class);
	}

	@Test(expected = MissingResourceException.class)
	public void testMsgEnumWithExtraEntry() {
		testMessageEnumeration(MsgEnumWithExtraEntry.class);
	}

	@Test
	public void testMsgEnumMissingAnEntry() {
		try {
			testMessageEnumeration(MsgEnumMissingAnEntry.class);
			fail("Test doesn't work - should have revealed that key M2 isn't in the Enum.");
		} catch (AssertionFailedError afe) {
			assertEquals("Unused message keys: M2", afe.getMessage());
		}
	}

}