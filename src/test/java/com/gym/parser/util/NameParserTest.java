package com.gym.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NameParserTest {

    @Test
    public void nameInputIsNull() {
        String[] result = NameParser.parse(null);
        assertNotNull(result, "Null input should still return array");
        assertEquals(2, result.length, "Array size");
        assertNull(result[0], "First (first name) element");
        assertNull(result[1], "Second (last name) element");
    }

    @Test
    public void nameInputIsEmpty() {
        String[] result = NameParser.parse("  ");
        assertNotNull(result, "Empty string should still return array");
        assertEquals(2, result.length, "Array size");
        assertNull(result[0], "First (first name) element");
        assertNull(result[1], "Second (last name) element");
    }

    @Test
    public void expectedFirstAndLastName() {
        String[] result = NameParser.parse("Jade Smith");
        assertEquals("Jade", result[0], "First name");
        assertEquals("Smith", result[1], "Last name");
    }

    @Test
    public void singleNameInput() {
        String[] result = NameParser.parse("Jade");
        assertEquals("Jade", result[0], "First name");
        assertNull(result[1], "Last name");
    }

    @Test
    public void nameWithMultipleWhiteSpaces() {
        String[] result = NameParser.parse("  Jade   Smith  ");
        assertEquals("Jade", result[0], "First name");
        assertEquals("Smith", result[1], "Last name");
    }

    @Test
    public void nameWithMultipleNames() {
        String[] result = NameParser.parse("Jade Cat Dog Smith");
        assertEquals("Jade Cat Dog", result[0], "First name");
        assertEquals("Smith", result[1], "Last name");
    }
}
