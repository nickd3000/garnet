import com.physmo.garnet.Utils;
import org.junit.Test;

public class TestUtils {

    @Test
    public void t1() {
        int i1 = Utils.floatToRgb(new float[]{0.1f, 0.5f, 1f, 0.99f});
        System.out.println(i1);
        float[] f1 = Utils.rgbToFloat(i1);
        printFloats(f1);

        int i2 = Utils.floatToRgb(f1);
        float[] f2 = Utils.rgbToFloat(i2);

        System.out.println(i2);
        printFloats(f2);
    }

    public static void printFloats(float[] floats) {
        for (float f : floats) {
            System.out.print(f + " ");
        }
        System.out.println();
    }
}
