package com.socialcomputing.wordpress.utils;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class StringUtils
{
    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if a string is neither empty ("") nor <code>null</code>.
     *
     * @param  s   the string to check, may be <code>null</code>.
     *
     * @return <code>true</code> if the string is neither empty ("")
     *         nor <code>null</code>.
     */
    public static boolean isSet(String s) {
        return ((s != null) && (s.length() != 0));
    }

    /**
     * Check if a string is <code>null</code>, empty or contains only
     * whitespace characters.
     *
     * @param  s   the string to check, may be <code>null</code>.
     *
     * @return <code>true</code> if the string is <code>null<code>,
     *         empty ("") or contains only whitespaces characters.
     */
    public static boolean isBlank(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

    /**
     * Returns a copy of a string, with leading and trailing whitespace
     * characters removed, never <code>null</code>.
     *
     * @param  s   the string to trim, may be <code>null</code>.
     *
     * @return A copy of the string with leading and trailing white
     *         space removed, or the string itself if it has no leading
     *         or trailing white space. If the input string is
     *         <code>null</code>, an empty string is returned.
     */
    public static String trimToEmpty(String s) {
        return (s == null)? "": s.trim();
    }

    /**
     * Returns a copy of a string, with leading and trailing whitespace
     * characters removed, or <code>null</code> if the trim result is
     * an empty string.
     *
     * @param  s   the string to trim, may be <code>null</code>.
     *
     * @return A copy of the string with leading and trailing white
     *         space removed, <code>null</code> if the trim result is
     *         an empty string, or the string itself if it has no
     *         leading or trailing white space.
     */
    public static String trimToNull(String s) {
        s = trimToEmpty(s);
        return (s.length() == 0)? null: s;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * Join operation on a collection
     * 
     * @param c a collection
     * @param separator of the collection elements
     * @return 
     */
    public static String join(Collection<?> c, String separator) {
        if(!isSet(separator)) {
            throw new IllegalArgumentException("Separator is null or empty");
        }
        
        if(c == null || c.size() == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        
        for(Object element : c) {
            String elementValue = (element == null) ? "" : element.toString();
            sb.append(elementValue).append(separator);
        }
        
        // remove latest separator
        sb.setLength(sb.length() - separator.length());
        
        return sb.toString();
    }
    
    
    /**
     * Split a String into a collection
     * 
     * @param s the string to split
     * @param separator a separator
     * 
     * @return
     */
    public static Collection<String> split(String s, String separator) {
        if (s == null) {
            throw new IllegalArgumentException("s");
        }
        if (!isSet(separator)) {
            throw new IllegalArgumentException("Separator is null or empty");
        }
        String[] stringElements = s.split(separator);
        Set<String> elements = new HashSet<String>(stringElements.length);
        for(String e : stringElements) {
            elements.add(e);
        }
        return elements;
    }
    
    
    /**
     * Keep only the n first characters of the string
     * Replace the other with the char argument 
     * 
     * @param s input string 
     * @param size the number of characters to keep
     * @param c the replacement char
     * 
     * @return a modified String
     * 
     */
    public static String keepFirstChars(String s, int size, char c) {
       if(!isSet(s)) {
           throw new IllegalArgumentException("The given string is null or empty");
       }
       
       if(size < 0 || size > s.length()) {
           throw new IllegalArgumentException("The size argument must be > 0 and < to the string length");
       }
       
       StringBuilder sb = new StringBuilder();
       sb.append(s.substring(0, size));
       
       for(int i = size; i < s.length() ; i++) {
           sb.append(c);
       }
       
       return sb.toString();
    }

    /**
     * Reverse a String, i.e. &quot;le sac a sel&quot; -&gt;
     * &quot;les a cas el&quot;.
     * 
     * @param  s   the string to reverse.
     * @return the reversed string.
     * @throws IllegalArgumentException if the input string is
     *         <code>null</code>.
     */
    public static String reverse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s");
        }
        char[] c = s.toCharArray();
        for (int i=0, max=c.length/2, last=c.length-1; i<max; i++) {
            int j = last - i;
            char n = c[i];
            c[i] = c[j];
            c[j] = n;
        }
        return new String(c);
    }
}
