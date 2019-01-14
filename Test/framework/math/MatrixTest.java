package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.math.Matrix;

import org.testng.annotations.Test;

public class MatrixTest
{
    @Test
    public void add()
    {
        Matrix test = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix expected = new Matrix(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32);
        Matrix result = new Matrix();
        Matrix.add(test, test, result);
        assertTrue(result.equals(expected));
        assertTrue(Matrix.add(test, test).equals(expected));
    }
}
