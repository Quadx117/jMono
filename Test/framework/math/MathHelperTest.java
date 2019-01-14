package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.math.MathHelper;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MathHelperTest
{
    @Test
    public void clampFloatTest()
    {
        assertTrue(MathHelper.clamp(1f, 0f, 2f) == 1f, "Failed boundary test, clamp [0,2] on 1 should be 1");
        assertTrue(MathHelper.clamp(1f, 0f, 1f) == 1f, "Failed boundary test, clamp [0,1] on 1 should be 1");
        assertTrue(MathHelper.clamp(1f, 2f, 5f) == 2f, "Failed boundary test, clamp [2,5] on 1 should be 2");
        assertTrue(MathHelper.clamp(1f, -50f, -10f) == -10f, "Failed boundary test, clamp [-50f, -10f] on 1 should be -10");
        assertTrue(MathHelper.clamp(1f, -50f, 25f) == 1f, "Failed boundary test, clamp [-50, 25] on 1 should be 1");
        assertTrue(MathHelper.clamp(0f, 1f, 1f) == 1f, "Failed boundary test, clamp [1,1] on 0 should be 1");
        assertTrue(MathHelper.clamp(0f, -1f, -1f) == -1f, "Failed boundary test, clamp [-1,-1] on 0 should be -1");
    }

    @Test
    public void clampIntTest()
    {
        assertTrue(MathHelper.clamp(1, 0, 2) == 1, "Failed boundary test, clamp [0,2] on 1 should be 1");
        assertTrue(MathHelper.clamp(1, 0, 1) == 1, "Failed boundary test, clamp [0,1] on 1 should be 1");
        assertTrue(MathHelper.clamp(1, 2, 5) == 2, "Failed boundary test, clamp [2,5] on 1 should be 2");
        assertTrue(MathHelper.clamp(1, -50, -10) == -10, "Failed boundary test, clamp [-50f, -10f] on 1 should be -10");
        assertTrue(MathHelper.clamp(1, -50, 25) == 1, "Failed boundary test, clamp [-50, 25] on 1 should be 1");
        assertTrue(MathHelper.clamp(0, 1, 1) == 1, "Failed boundary test, clamp [1,1] on 0 should be 1");
        assertTrue(MathHelper.clamp(0, -1, -1) == -1, "Failed boundary test, clamp [-1,-1] on 0 should be -1");
    }

    @Test
    public void distanceTest()
    {
        assertEquals(MathHelper.distance(0, 5f), 5f, "Distance test failed on [0,5]");
        assertEquals(MathHelper.distance(-5f, 5f), 10f, "Distance test failed on [-5,5]");
        assertEquals(MathHelper.distance(0f, 0f), 0f, "Distance test failed on [0,0]");
        assertEquals(MathHelper.distance(-5f, -1f), 4f, "Distance test failed on [-5,-1]");
    }

    @Test
    public void lerpTest()
    {
        assertEquals(MathHelper.lerp(0f, 5f, .5f), 2.5f, "Lerp test failed on [0,5,.5]");
        assertEquals(MathHelper.lerp(-5f, 5f, 0.5f), 0f, "Lerp test failed on [-5,5,0.5]");
        assertEquals(MathHelper.lerp(0f, 0f, 0.5f), 0f, "Lerp test failed on [0,0,0.5]");
        assertEquals(MathHelper.lerp(-5f, -1f, 0.5f), -3f, "Lerp test failed on [-5,-1, 0.5]");
        // The following test checks for XNA compatibility.
        // Even though the calculation itself should return "1", the XNA implementation returns 0
        // (presumably due to a efficiency/precision tradeoff).
        assertEquals(MathHelper.lerp(10000000000000000f, 1f, 1f), 0f, "Lerp test failed on [10000000000000000,1,1]");
    }

    @Test
    public void lerpPreciseTest()
    {
        assertEquals(MathHelper.lerpPrecise(0f, 5f, .5f), 2.5f, "LerpPrecise test failed on [0,5,.5]");
        assertEquals(MathHelper.lerpPrecise(-5f, 5f, 0.5f), 0f, "LerpPrecise test failed on [-5,5,0.5]");
        assertEquals(MathHelper.lerpPrecise(0f, 0f, 0.5f), 0f, "LerpPrecise test failed on [0,0,0.5]");
        assertEquals(MathHelper.lerpPrecise(-5f, -1f, 0.5f), -3f, "LerpPrecise test failed on [-5,-1, 0.5]");
        assertEquals(MathHelper.lerpPrecise(10000000000000000f, 1f, 1f), 1f, "LerpPrecise test failed on [10000000000000000,1,1]");
    }

    @Test
    public void min()
    {
        assertEquals(-0.5f, MathHelper.min(-0.5f, -0.5f));
        assertEquals(-0.5f, MathHelper.min(-0.5f, 0.0f));
        assertEquals(-0.5f, MathHelper.min(0.0f, -0.5f));
        assertEquals(0, MathHelper.min(0, 0));
        assertEquals(-5, MathHelper.min(-5, 5));
        assertEquals(-5, MathHelper.min(5, -5));
    }

    @Test
    public void max()
    {
        assertEquals(-0.5f, MathHelper.max(-0.5f, -0.5f));
        assertEquals(0.0f, MathHelper.max(-0.5f, 0.0f));
        assertEquals(0.0f, MathHelper.max(0.0f, -0.5f));
        assertEquals(0, MathHelper.max(0, 0));
        assertEquals(5, MathHelper.max(-5, 5));
        assertEquals(5, MathHelper.max(5, -5));
    }

    @DataProvider
    private Object[][] PiConstantsValuesTestCases()
    {
        return new Object[][]
        {
         { MathHelper.PiOver4, 0.7853982f },
         { MathHelper.PiOver2, 1.5707964f },
         { MathHelper.Pi, 3.1415927f },
         { MathHelper.TwoPi, 6.2831855f },
        };
    }

    @Test(dataProvider = "PiConstantsValuesTestCases")
    public void PiConstantsAreExpectedValues(float actualValue, float expectedValue)
    {
        assertEquals(expectedValue, actualValue);
    }

    @DataProvider
    private Object[][] wrapAngleReturnsExpectedValuesTestCases()
    {
        return new Object[][]
        {
         { 0f, 0f },
         { MathHelper.PiOver4, MathHelper.PiOver4 },
         { -MathHelper.PiOver4, -MathHelper.PiOver4 },
         { MathHelper.PiOver2, MathHelper.PiOver2 },
         { -MathHelper.PiOver2, -MathHelper.PiOver2 },
         { MathHelper.Pi, MathHelper.Pi },
         { -MathHelper.Pi, MathHelper.Pi },
         { MathHelper.TwoPi, 0f },
         { -MathHelper.TwoPi, 0f },
         { 10f, -2.566371f },
         { -10f, 2.566371f },
         // Pi boundaries
         { 3.1415927f, 3.1415927f },
         { 3.141593f, -3.1415925f },
         { -3.1415925f, -3.1415925f },
         { -3.1415927f, 3.1415927f },
         // 2 * Pi boundaries
         { 6.283185f, -4.7683716E-7f },
         { 6.2831855f, 0f },
         { 6.283186f, 4.7683716E-7f },
         { -6.283185f, 4.7683716E-7f },
         { -6.2831855f, 0f },
         { -6.283186f, -4.7683716E-7f },
         // 3 * Pi boundaries
         { 9.424778f, 3.1415925f },
         { 9.424779f, -3.141592f },
         { -9.424778f, -3.1415925f },
         { -9.424779f, 3.141592f },
         // 4 * Pi boundaries
         { 12.56637f, -9.536743E-7f },
         { 12.566371f, 0f },
         { 12.566372f, 9.536743E-7f },
         { -12.56637f, 9.536743E-7f },
         { -12.566371f, 0f },
         { -12.566372f, -9.536743E-7f },
         // 5 * Pi boundaries
         { 15.707963f, 3.141592f },
         { 15.707964f, -3.1415925f },
         { -15.707963f, -3.141592f },
         { -15.707964f, 3.1415925f },
         // 10 * Pi boundaries
         { 31.415926f, -1.4305115E-6f },
         { 31.415928f, 4.7683716E-7f },
         { -31.415926f, 1.4305115E-6f },
         { -31.415928f, -4.7683716E-7f },
         // 20 * Pi boundaries
         { 62.831852f, -2.861023E-6f },
         { 62.831856f, 9.536743E-7f },
         { -62.831852f, 2.861023E-6f },
         { -62.831856f, -9.536743E-7f },
         // 20000000 * Pi boundaries
         { 6.2831852E7f, -2.8202515f },
         { 6.2831856E7f, 1.1797485f },
         { -6.2831852E7f, 2.8202515f },
         { -6.2831856E7f, -1.1797485f },
        };
    }

    @Test(dataProvider = "wrapAngleReturnsExpectedValuesTestCases")
    public void wrapAngleReturnsExpectedValues(float angle, float expectedValue)
    {
        float actualValue = MathHelper.wrapAngle(angle);
        assertEquals(expectedValue, actualValue);
    }
}
