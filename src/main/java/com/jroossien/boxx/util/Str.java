/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Rojoss <http://jroossien.com>
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jroossien.boxx.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Common {@link String} utilities.
 */
public class Str {

    private static final String CLR_CHARS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final Pattern COLOR = Pattern.compile("&([" + CLR_CHARS + "])");
    private static final Pattern COLOR_REPLACE = Pattern.compile("§([" + CLR_CHARS + "])");
    private static final Pattern COLOR_STRIP = Pattern.compile("§[" + CLR_CHARS + "]|&[" + CLR_CHARS + "]");


    /**
     * Integrate ChatColor in a string based on color codes.
     * This replaces codes like &amp;a&amp;l with §a§l
     *
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String color(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        return COLOR.matcher(str).replaceAll("§$1");
    }

    /**
     * Integrate ChatColor in multiple strings based on color codes.
     * This replaces codes like &amp;a&amp;l with §a§l
     *
     * @param strings The strings to apply color to.
     * @return formatted strings
     */
    public static String[] color(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = color(strings[i]);
        }
        return strings;
    }

    /**
     * Integrate ChatColor in multiple strings based on color codes.
     * This replaces codes like &amp;a&amp;l with §a§l
     *
     * @param strings The strings to apply color to.
     * @return formatted strings
     */
    public static List<String> color(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, color(strings.get(i)));
        }
        return strings;
    }

    /**
     * Remove all color and put regular colors as the formatting codes like &amp;1.
     *
     * @param str The string to remove color from.
     * @return formatted string
     */
    public static String replaceColor(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        return COLOR_REPLACE.matcher(str).replaceAll("&$1");
    }

    /**
     * Remove all color and put regular colors as the formatting codes like &amp;1.
     *
     * @param strings The strings to remove color from.
     * @return formatted strings
     */
    public static String[] replaceColor(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = replaceColor(strings[i]);
        }
        return strings;
    }

    /**
     * Remove all color and put regular colors as the formatting codes like &amp;1.
     *
     * @param strings The strings to remove color from.
     * @return formatted strings
     */
    public static List<String> replaceColor(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, replaceColor(strings.get(i)));
        }
        return strings;
    }

    /**
     * Strips all coloring from the specified string.
     * For example a string like: '&amp;a&amp;ltest' becomes 'test' and '§a&ltest' becomes 'test'.
     *
     * @param str The string to remove color from.
     * @return String without any colors and without any color codes.
     */
    public static String stripColor(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        return COLOR_STRIP.matcher(str).replaceAll("");
    }

    /**
     * Strips all coloring from the specified strings.
     * For example a string like: '&amp;a&amp;ltest' becomes 'test' and '§a&ltest' becomes 'test'.
     *
     * @param strings The strings to remove color from.
     * @return Strings without any colors and without any color codes.
     */
    public static String[] stripColor(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = stripColor(strings[i]);
        }
        return strings;
    }

    /**
     * Strips all coloring from the specified strings.
     * For example a string like: '&amp;a&amp;ltest' becomes 'test' and '§a&ltest' becomes 'test'.
     *
     * @param strings The strings to remove color from.
     * @return Strings without any colors and without any color codes.
     */
    public static List<String> stripColor(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, stripColor(strings.get(i)));
        }
        return strings;
    }



    /**
     * Capitalize the first character of a string.
     *
     * @param str The string that needs to be capitalized.
     * @return Capitalized string
     */
    public static String capitalize(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Formats a string with underscores to CamelCase.
     * This can be used for displaying enum keys and such.
     *
     * @param str The string to format to camel case.
     * @return CamelCased string
     */
    public static String camelCase(String str) {
        return WordUtils.capitalizeFully(str, new char[] {'_'}).replaceAll("_", "");
    }



    /**
     * Get the best matching value for the specified input out of the array of values.
     * This uses the levenshtein distance from {@link StringUtils}
     * If an exact match is found that match will be returned.
     *
     * @param input The input string to find a match for.
     * @param values Array of values to match with input string.
     * @return The best match from the specified values. (May be empty when there are no values or no match)
     */
    public static String bestMatch(String input, String... values) {
        String bestMatch = "";
        int lowestDiff = input.length() - 1;
        for (String value : values) {
            int diff = StringUtils.getLevenshteinDistance(input, value);
            if (diff == 0) {
                return value;
            }
            if (diff < lowestDiff) {
                bestMatch = value;
                lowestDiff = diff;
            }
        }
        return bestMatch;
    }

    /**
     * @see Str#bestMatch(String, String...)
     */
    public static String bestMatch(String input, Collection<? extends String> values) {
        return bestMatch(input, values.toArray(new String[values.size()]));
    }



    /**
     * Wrap the specified string to multiple lines by adding a newline symbol '\n'
     * <p/>
     * <p>This does not break up words.
     * Which means, if there is a word that is longer than the wrap limit it will exceed the limit.
     *
     * @param string The string that needs to be wrapped.
     * @param length The maximum length for each line.
     * @return String with linebreaks.
     */
    public static String wrapString(String string, int length) {
        StringBuilder sb = new StringBuilder(string);
        int i = 0;
        while ((i = sb.indexOf(" ", i + length)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return sb.toString();
    }

    /**
     * Wrap the specified string to multiple lines by adding a newline symbol '\n'
     * <p/>
     * <p>The lines will always be exactly the length specified.
     * Words will be cut in half and a new line will be forced.
     * Use {@link #wrapString(String, int)} to not have this behaviour.
     *
     * @param string The string that needs to be wrapped.
     * @param length The maxmimum length for each line.
     * @return String with linebreaks.
     */
    public static String wrapStringExact(String string, int length) {
        return string.replaceAll("(.{" + length + "})", "$1\n");
    }



    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(Collection<?> args) {
        return implode(args, ", ", " & ");
    }

    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(Collection<?> args, String glue) {
        return implode(args, glue, glue);
    }

    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(Collection<?> args, String glue, String lastGlue) {
        return implode(args, glue, lastGlue, 0, args == null ? 0 : args.size());
    }

    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(Collection<?> args, String glue, String lastGlue, int start, int end) {
        if (args == null || args.isEmpty()) {
            return "";
        }
        return implode(args.toArray(new Object[args.size()]), glue, lastGlue, start, end);
    }

    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(String[] args) {
        return implode(args, ", ", " & ");
    }

    /** @see Str#implode(Object[], String, String, int, int) */
    public static String implode(String[] args, String glue) {
        return implode(args, glue, glue);
    }

    /**
     * @see Str#implode(Object[], String, String, int, int)
     */
    public static String implode(String[] args, String glue, String lastGlue) {
        return implode(args, glue, lastGlue, 0, args == null ? 0 : args.length);
    }

    /**
     * Combine the given list of objects in to a string by adding glue between the values.
     * It will use {@link Object#toString()} for the object values. (if the value is null it will be the string 'null')
     * <p/>
     * <p>The glue will be added between the values.
     * Between the last two values it will put the lastGlue.
     * By default it uses ', ' as glue and ' & ' as last glue to get something like [value1, value2, value3] 'value1, value2 & value3'
     * When only a glue is specified it will use the same character as the glue for the last glue.
     * <p/>
     * <p>A start and end can be specified to select which elements of the array should be converted to a String.
     * By default it starts and 0 and ends at the array length - 1 to do all values.
     *
     * @param arr The array with values to implode to a string.
     * @param glue The glue string which will be placed between values.
     * @param lastGlue The glue string which will be placed between the last two values.
     * @param start The index to start at. (0 based)
     * @param end The index to end at. (0 based (size-1))
     * @return String with imploded array values. (Empty string when objects array is empty or null)
     */
    public static String implode(Object[] arr, String glue, String lastGlue, int start, int end) {
        String result = "";
        if (arr == null || arr.length <= 0) {
            return result;
        }

        for (int i = start; i <= end && i < arr.length; i++) {
            result += arr[i] == null ? "null" : arr[i].toString();
            if (i >= end - 1 || i >= arr.length - 2) {
                result += lastGlue;
            } else {
                result += glue;
            }
        }

        if (result.isEmpty()) {
            return result;
        }
        return result.substring(0, result.length() - lastGlue.length());
    }


    /**
     * Split the specified string(s) by new lines.
     * <p/>
     * For example [Line1, Line2\nLine3, Line4] would become [Line1, Line2, Line3, Line4]
     *
     * @param strings The string(s) that need to be split.
     * @return List with strings split.
     */
    public static List<String> splitLines(String... strings) {
        return splitLines(Arrays.asList(strings));
    }

    /**
     * Split the specified list of strings by new lines.
     * <p/>
     * For example [Line1, Line2\nLine3, Line4] would become [Line1, Line2, Line3, Line4]
     *
     * @param strings The list of strings that need to be split.
     * @return List with strings split.
     */
    public static List<String> splitLines(List<String> strings) {
        List<String> splitList = new ArrayList<>();
        for (String string : strings) {
            String[] split = string.split("\\\\n|\\r?\\n");
            for (String str : split) {
                splitList.add(str);
            }
        }
        return splitList;
    }


    /**
     * Split a string by using a space as split character.
     *
     * @see Str#splitQuotes(String, char, boolean)
     */
    public static List<String> splitQuotes(String string) {
        return splitQuotes(string, ' ', false);
    }

    /**
     * Splits the specified string based on the specified character.
     *
     * @see Str#splitQuotes(String, char, boolean)
     */
    public static List<String> splitQuotes(String string, char split) {
        return splitQuotes(string, split, false);
    }

    /**
     * Splits the specified string based on the specified character.
     * The default is a space as split character and the examples below use that too.
     * <p/>
     * <p>Strings inside quotes will be placed together in sections.
     * For example 'This plugin is "super awesome"' will return [this, plugin, is, super awesome]
     * Works for both double and single quotes. When using double quotes you can use single quotes within and the other way around.
     * like <pre>test "You're awesome"</pre> Would turn in to: [test, You're awesome]
     * <p/>
     * <p>Text in front of the starting quote will be added to the section too.
     * <pre>test name:"That's awesome!" Yup!</pre> would be [test, name:That's awesome!, Yup!]
     *
     * @param string The string that needs to be split.
     * @param split The character to use for splitting the string.
     *              This should not be a quote or double quote!
     * @param keepQuotes When true quotes will remain in the slitted strings otherwise they will be removed.
     * @return List of strings split from the input string.
     */
    public static List<String> splitQuotes(String string, char split, boolean keepQuotes) {
        List<String> sections = new ArrayList<String>();
        char[] chars = string.toCharArray();

        StringBuilder section = new StringBuilder();
        char quote = 0;
        boolean escape = false;
        for (char ch : chars) {
            if (ch == '\\') {
                escape = true;
                continue;
            }
            if (escape) {
                escape = false;
                if (ch == 34 || ch == 39 || ch == split) {
                    if (keepQuotes) {
                        section.append("\\");
                    }
                    section.append(ch);
                    continue;
                } else {
                    section.append("\\");
                }
            }
            if (ch == split && quote == 0) {
                //Start new section for split char when not quoted.
                sections.add(section.toString());
                section.setLength(0);
                continue;
            }
            if (ch == 34 || ch == 39) {
                if (ch == quote) {
                    //End of quote
                    if (keepQuotes) {
                        section.append(ch);
                    }
                    sections.add(section.toString());
                    section.setLength(0);
                    quote = 0;
                    continue;
                } else if (quote == 0) {
                    //Start of quote
                    quote = ch;
                    if (keepQuotes) {
                        section.append(ch);
                    }
                } else {
                    //Quote within quote
                    section.append(ch);
                }
                continue;
            }
            //Regular character
            section.append(ch);
        }
        //Add last section
        sections.add(section.toString());

        return sections;
    }

    /**
     * Splits the specified string based on the specified character.
     * <p/>
     * If the split character is within single or double quotes it will be ignored.
     * For example <pre>name:"points:10"</pre> would result in [name, points:10] instead of [name, points, 10]
     * <p/>
     * Quotes can be nested too for example <pre>this:"isn't:a:example"</pre> would result in [this, isn't:a:example]
     *
     * @param string The string that needs to be split.
     * @param split The character to use for splitting the string.
     *              This should not be a quote or double quote!
     * @param keepQuotes When true quotes will remain in the slitted strings otherwise they will be removed.
     * @return List of strings split from the input string.
     */
    public static List<String> splitIgnoreQuoted(String string, char split, boolean keepQuotes) {
        List<String> sections = new ArrayList<String>();
        char[] chars = string.toCharArray();

        StringBuilder section = new StringBuilder();
        char quote = 0;
        boolean escape = false;
        for (char ch : chars) {
            if (ch == '\\') {
                escape = true;
                continue;
            }
            if (escape) {
                escape = false;
                if (ch == 34 || ch == 39 || ch == split) {
                    if (keepQuotes) {
                        section.append("\\");
                    }
                    section.append(ch);
                    continue;
                } else {
                    section.append("\\");
                }
            }
            if (ch == split && quote == 0) {
                //Start new section for split char when not quoted.
                sections.add(section.toString());
                section.setLength(0);
                continue;
            }
            if (ch == 34 || ch == 39) {
                if (ch == quote) {
                    //End of quote
                    quote = 0;
                    if (keepQuotes) {
                        section.append(ch);
                    }
                } else if (quote == 0) {
                    //Start of quote
                    quote = ch;
                    if (keepQuotes) {
                        section.append(ch);
                    }
                } else {
                    //Quote within quote
                    section.append(ch);
                }
                continue;
            }
            //Regular character
            section.append(ch);
        }
        //Add last section
        sections.add(section.toString());

        return sections;
    }

    /**
     * Put single or double quotes around the specified string if it contains spaces.
     * <p/>
     * If the string has double quotes single quotes will be used to surround it otherwise double quotes.
     * If the string doesn't have spaces it won't be escaped.
     *
     * @param value The string that needs to be escaped.
     * @return The specified string with quotes around it if it has spaces.
     */
    public static String escapeWords(String value) {
        if (value.contains(" ")) {
            if (!value.contains("'")) {
                value = "'" + value + "'";
            } else {
                value = "\"" + value + "\"";
            }
        }
        return value;
    }

    public static String escapeQuotes(String value) {
        if (value.contains("'")) {
            value = value.replaceAll("'", "\\\\'");
        }
        if (value.contains("\"")) {
            value = value.replaceAll("\"", "\\\\\"");
        }
        return value;
    }

    /**
     * Remove quotes from the beginning and the end of the string.
     * <p/>
     * It will only remove the quotes when the string starts with a quote and ends with a quote.
     * The quote on the end must be the same quote as the the quote in the beginning.
     *
     * @param string The string to remove the quotes from.
     * @return Modified string.
     */
    public static String removeQuotes(String string) {
        String[] quotes = new String[] {"'", "\"", "\\'", "\\\""};
        for (String quote : quotes) {
            if (string.startsWith(quote) && string.endsWith(quote)) {
                return string.substring(1, string.length()-1);
            }
        }
        return string;
    }

}
