import org.junit.Test;
import static org.junit.Assert.*;

public class RabinKarpAlgorithmTests {
    @Test
    public void basic() {
        String input = "hello";
        String pattern = "ell";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input, pattern));

        input = "01234567890123456789stringextremelylongstringextremelylongstringextremelylongstring";
        pattern = "stringextreme";
        assertEquals(20, RabinKarpAlgorithm.rabinKarp(input, pattern));
    }
}
