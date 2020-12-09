import es.datastructur.synthesizer.GuitarString;
import java.util.HashMap;
import java.util.Map;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        Map<Character, GuitarString> keyStringMap = new HashMap<>();
        for (int i = 0; i < keyboard.length(); i++) {
            char c = keyboard.charAt(i);
            GuitarString gString = new GuitarString(GuitarHero.getFrequency(i));
            keyStringMap.put(c, gString);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (keyStringMap.containsKey(input)) {
                    GuitarString gS = keyStringMap.get(input);
                    gS.pluck();
                }
            }
            /* compute the superposition of samples */
            double sample = 0.0;

            for (Map.Entry<Character, GuitarString> entry : keyStringMap.entrySet()) {
                sample += entry.getValue().sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString value : keyStringMap.values()) {
                value.tic();
            }
        }
    }

    /**
     * Return the correspond frequency of ith character
     * f = 440 * 2^((i - 24) / 12)
     * @param i index of the character
     * @return the corresponding frequency
     */
    private static double getFrequency(int i) {
        return GuitarHero.CONCERT_A * Math.pow(2, (i - 24.0) / 12.0);
    }
}
