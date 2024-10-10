import com.physmo.garnet.ColorUtils;
import org.junit.Test;

public class TestUtilsOld {

    @Test
    public void t1() {
        int i1 = ColorUtils.floatToRgb(new float[]{0.1f, 0.5f, 1f, 0.99f});
        System.out.println(i1);
        float[] f1 = ColorUtils.rgbToFloat(i1);
        printFloats(f1);

        int i2 = ColorUtils.floatToRgb(f1);
        float[] f2 = ColorUtils.rgbToFloat(i2);

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
