package framework;

import static org.testng.Assert.*;
import jMono_Framework.Color;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ColorTest
{
    // Contains a test case for each constructor type
    @DataProvider
    private Object[][] ctorTestCases()
    {
        return new Object[][]
        {
         { new Color(new Color(64, 128, 192), 32), 64, 128, 192, 32 },
         { new Color(new Color(64, 128, 192), 256), 64, 128, 192, 255 },
         { new Color(new Color(64, 128, 192), 0.125f), 64, 128, 192, 32 },
         { new Color(new Color(64, 128, 192), 1.1f), 64, 128, 192, 255 },
         { new Color((short) 64, (short) 128, (short) 192, (short) 32), 64, 128, 192, 32 },
         { new Color(), 0, 0, 0, 0 },
         { new Color(64, 128, 192), 64, 128, 192, 255 },
         { new Color(256, 256, -1), 255, 255, 0, 255 },
         { new Color(64, 128, 192, 32), 64, 128, 192, 32 },
         { new Color(256, 256, -1, 256), 255, 255, 0, 255 },
         { new Color(0.25f, 0.5f, 0.75f), 64, 128, 192, 255 },
         { new Color(1.1f, 1.1f, -0.1f), 255, 255, 0, 255 },
         { new Color(0.25f, 0.5f, 0.75f, 0.125f), 64, 128, 192, 32 },
         { new Color(1.1f, 1.1f, -0.1f, -0.1f), 255, 255, 0, 0 },
         { new Color(new Vector3(0.25f, 0.5f, 0.75f)), 64, 128, 192, 255 },
         { new Color(new Vector3(1.1f, 1.1f, -0.1f)), 255, 255, 0, 255 },
         { new Color(new Vector4(0.25f, 0.5f, 0.75f, 0.125f)), 64, 128, 192, 32 },
         { new Color(new Vector4(1.1f, 1.1f, -0.1f, -0.1f)), 255, 255, 0, 0 }
        };
    }

    @Test(dataProvider = "ctorTestCases")
    public void ctor_Explicit(Color color, int expectedR, int expectedG, int expectedB, int expectedA)
    {
        // Account for rounding differences with float constructors
        assertEquals(color.getRed(), expectedR, 1);
        assertEquals(color.getGreen(), expectedG, 1);
        assertEquals(color.getBlue(), expectedB, 1);
        assertEquals(color.getAlpha(), expectedA, 1);
    }

    @Test
    public void ctor_Packed()
    {
        Color color = new Color(0x20C08040);

        assertEquals(color.getRed(), 64);
        assertEquals(color.getGreen(), 128);
        assertEquals(color.getBlue(), 192);
        assertEquals(color.getAlpha(), 32);
    }

    @Test
    public void fromNonPremultiplied_Int()
    {
        Color color = Color.fromNonPremultiplied(255, 128, 64, 128);
        assertEquals(color.getRed(), 128, 1);
        assertEquals(color.getGreen(), 64, 1);
        assertEquals(color.getBlue(), 32, 1);
        assertEquals(color.getAlpha(), 128, 0);

        Color overflow = Color.fromNonPremultiplied(280, 128, -10, 128);
        assertEquals(overflow.getRed(), 140, 1);
        assertEquals(overflow.getGreen(), 64, 1);
        assertEquals(overflow.getBlue(), 0, 1);
        assertEquals(overflow.getAlpha(), 128, 0);

        Color overflow2 = Color.fromNonPremultiplied(255, 128, 64, 280);
        assertEquals(overflow2.getRed(), 255, 1);
        assertEquals(overflow2.getGreen(), 140, 1);
        assertEquals(overflow2.getBlue(), 70, 1);
        assertEquals(overflow2.getAlpha(), 255, 0);
    }

    @Test
    public void fromNonPremultiplied_Float()
    {
        Color color = Color.fromNonPremultiplied(new Vector4(1.0f, 0.5f, 0.25f, 0.5f));
        assertEquals(color.getRed(), 128, 1);
        assertEquals(color.getGreen(), 64, 1);
        assertEquals(color.getBlue(), 32, 1);
        assertEquals(color.getAlpha(), 128, 1);

        Color overflow = Color.fromNonPremultiplied(new Vector4(1.1f, 0.5f, -0.1f, 0.5f));
        assertEquals(overflow.getRed(), 140, 1);
        assertEquals(overflow.getGreen(), 64, 1);
        assertEquals(overflow.getBlue(), 0, 1);
        assertEquals(overflow.getAlpha(), 128, 1);

        Color overflow2 = Color.fromNonPremultiplied(new Vector4(1f, 0.5f, 0.25f, 1.1f));
        assertEquals(overflow2.getRed(), 255, 1);
        assertEquals(overflow2.getGreen(), 140, 1);
        assertEquals(overflow2.getBlue(), 70, 1);
        assertEquals(overflow2.getAlpha(), 255, 1);
    }

    @Test
    public void multiply()
    {
        Color color = new Color(1, 2, 3, 4);

        // Test 1.0 scale.
        assertTrue(color.equals(Color.multiply(color, 1.0f)));

        // Test 0.999 scale.
        Color almostOne = new Color(0, 1, 2, 3);
        assertTrue(almostOne.equals(Color.multiply(color, 0.999f)));

        // Test 1.001 scale.
        assertTrue(color.equals(Color.multiply(color, 1.001f)));

        // Test 0.0 scale.
        assertTrue(Color.multiply(color, 0.0f).equals(Color.Transparent()));

        // Test 0.001 scale.
        assertTrue(Color.multiply(color, 0.001f).equals(Color.Transparent()));

        // Test -0.001 scale.
        assertTrue(Color.multiply(color, -0.001f).equals(Color.Transparent()));

        // Test for overflow.
        assertTrue(Color.multiply(color, 300.0f).equals(Color.White()));

        // Test for underflow.
        assertTrue(Color.multiply(color, -1.0f).equals(Color.Transparent()));
    }

    @Test
    public void lerp()
    {
        Color color1 = new Color(20, 40, 0, 0);
        Color color2 = new Color(41, 81, 255, 255);

        // Test zero and underflow.
        assertTrue(color1.equals(Color.lerp(color1, color2, 0.0f)));
        assertTrue(color1.equals(Color.lerp(color1, color2, 0.001f)));
        assertTrue(color1.equals(Color.lerp(color1, color2, -0.001f)));
        assertTrue(color1.equals(Color.lerp(color1, color2, -1.0f)));

        // Test one scale and overflows.
        assertTrue(color2.equals(Color.lerp(color1, color2, 1.0f)));
        assertTrue(color2.equals(Color.lerp(color1, color2, 1.001f)));
        assertTrue(new Color(254, 254, 254, 254).equals(Color.lerp(Color.Transparent(), Color.White(), 0.999f)));
        assertTrue(color2.equals(Color.lerp(color1, color2, 2.0f)));

        // Test half scale.
        Color half = new Color(30, 60, 127, 127);
        assertTrue(half.equals(Color.lerp(color1, color2, 0.5f)));
        assertTrue(half.equals(Color.lerp(color1, color2, 0.501f)));
        assertTrue(half.equals(Color.lerp(color1, color2, 0.499f)));

        // Test backwards lerp.
        assertTrue(color2.equals(Color.lerp(color2, color1, 0.0f)));
        assertTrue(color1.equals(Color.lerp(color2, color1, 1.0f)));
        assertTrue(half.equals(Color.lerp(color2, color1, 0.5f)));
    }
}
