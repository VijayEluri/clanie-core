/**
 * Copyright (C) 2008, Claus Nielsen, cn@cn-consult.dk
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
package dk.clanie.test.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import org.junit.Test;

import dk.clanie.util.MessageDefinition;


/**
 * Test MessageEnumTest.
 * 
 * @author Claus Nielsen
 */
public class MessageEnumTestTest extends MessageEnumTest {

	private static class MsgBundle extends ListResourceBundle {
		@Override
		protected Object[][] getContents() {
			return new Object[][] {
					{ "M1", "Message One" },
					{ "M2", "Message Two" }, };
		}
	};
	private static final ResourceBundle bundle = new MsgBundle();

	// A MessageDefinition Enumeration which exactly matches the ResourceBundle.
	// This should pass the test.
	private enum MsgEnum implements MessageDefinition {
		M1, M2;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	// A MessageDefinition Enumeration with one less entry than the
	// ResourceBundle. This should fail the test.
	private enum MsgEnumMissingAnEntry implements MessageDefinition {
		M1;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	// A MessageDefinition Enumeration with an entry which isn't defined in the
	// ResourceBundle. This should fail the test.
	private enum MsgEnumWithExtraEntry implements MessageDefinition {
		M1, M2, M3;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	// A MessageDefinition Enumeration with both entries not defined in the ResourceBundle
	// and missing an entry which IS defined in the Resource Bundle.
	// This should fail the test.
	private enum MsgEnumWithErrors implements MessageDefinition {
		M1, M3, M4;
		@Override public ResourceBundle getBundle() { return bundle; }
		@Override public String key() { return name(); }
		@Override public String text(Object... args) { return bundle.getString(name()); }
	}

	@Test
	public void testCorrectMessageEnumeration() {
		testMessageEnumeration(MsgEnum.class);
	}

	@Test
	public void testMsgEnumWithExtraEntry() {
		try {
			testMessageEnumeration(MsgEnumWithExtraEntry.class);
			fail("Test doesn't work - should have revealed that key M3 is missing.");
		} catch (AssertionError afe) {
			assertThat(afe.getMessage(), equalTo("Missing keys: M3"));
		}
	}

	@Test
	public void testMsgEnumMissingAnEntry() {
		try {
			testMessageEnumeration(MsgEnumMissingAnEntry.class);
			fail("Test doesn't work - should have revealed that key M2 isn't used.");
		} catch (AssertionError afe) {
			assertThat(afe.getMessage(), equalTo("Unused keys: M2"));
		}
	}

	@Test
	public void testMsgEnumWithErrors() {
		try {
			testMessageEnumeration(MsgEnumWithErrors.class);
			fail("Test doesn't work - should have revealed that key M2 isn't used" +
					" and that M# and M4 are missing.");
		} catch (AssertionError afe) {
			assertThat(afe.getMessage(), equalTo("Missing keys: M3, M4; Unused keys: M2"));
		}
	}

}
