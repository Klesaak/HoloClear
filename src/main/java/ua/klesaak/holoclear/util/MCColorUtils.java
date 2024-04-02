package ua.klesaak.holoclear.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sgtcaze
 */

@UtilityClass
public class MCColorUtils {
    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public String color(String text) {
        if (text == null) return "";

        text = ChatColor.translateAlternateColorCodes('&', text);
        char colorChar = ChatColor.COLOR_CHAR;

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer(text.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        text = matcher.appendTail(buffer).toString();

        return text;
    }
}
