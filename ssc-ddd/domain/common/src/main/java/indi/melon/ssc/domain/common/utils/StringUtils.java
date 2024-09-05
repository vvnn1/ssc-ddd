package indi.melon.ssc.domain.common.utils;

/**
 * @author vvnn1
 * @since 2024/4/7 20:36
 */
public class StringUtils {
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
}
