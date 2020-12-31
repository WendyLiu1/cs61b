public class RabinKarpAlgorithm {

    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if (input == null || pattern == null) {
            return -1;
        }
        int patternLen = pattern.length();
        int inputLen = input.length();

        if (patternLen > inputLen) {
            return -1;
        }

        RollingString patterStr = new RollingString(pattern, patternLen);
        RollingString inputStr = new RollingString(input.substring(0, patternLen), patternLen);

        int i = patternLen - 1;
        while (i < inputLen) {
            if (inputStr.hashCode() == patterStr.hashCode() && inputStr.equals(patterStr)) {
                return i - patternLen + 1;
            }
            i++;
            if (i < inputLen) {
                inputStr.addChar(input.charAt(i));
            }
        }

        return -1;
    }
}
