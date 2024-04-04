package ua.klesaak.holoclear.util;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class UtilityMethods {

    /**
     * Equivalent to {@link String#replace(CharSequence, CharSequence)}, but uses a
     * {@link Supplier} for the replacement.
     *
     * @param pattern     the pattern for the replacement target
     * @param input       the input string
     * @param replacement the replacement
     * @return the input string with the replacements applied
     */
    public String replaceAll(Pattern pattern, String input, Supplier<Object> replacement) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) return matcher.replaceAll(Matcher.quoteReplacement(String.valueOf(replacement.get())));
        return input;
    }

    public  <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) {
        Preconditions.checkNotNull(token, "Search token cannot be null");
        Preconditions.checkNotNull(collection, "Collection cannot be null");
        Preconditions.checkNotNull(originals, "Originals cannot be null");
        originals.forEach(string -> { if (startsWithIgnoreCase(string, token)) collection.add(string);});
        return collection;
    }

    private boolean startsWithIgnoreCase(String string, String prefix) {
        Preconditions.checkNotNull(string, "Cannot check a null string for a match");
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
