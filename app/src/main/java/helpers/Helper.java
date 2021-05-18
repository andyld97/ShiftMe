package helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Helper
{
    public static int generateRandomNumber(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static <T> List<T> clone(List<T> original)
    {
        List<T> copy = new ArrayList<>(original);
        return copy;
    }

    public static <T> T[] convert(List<T> lst)
    {
        T[] arr = (T[])Array.newInstance(lst.get(0).getClass(), lst.size());

        for (int i = 0; i < lst.size(); i++)
            arr[i] = lst.get(i);

        return arr;
    }

    public static int calculateColoring(int[][] A)
    {
        if (A == null || A.length == 0  || A[0].length != A.length)
            return 0;

        int dim = A.length;
        int value = 1;
        boolean finish = false;

        for (int j = 0; j < dim; j++)
        {
            for (int i = 0; i < dim; i++)
            {
                if (A[j][i] != value) {

                    if (j == dim - 1 && i == dim - 1 && A[dim - 1][dim - 1] == 0)
                        value++;
                    else {
                        finish = true;
                        break;
                    }
                }
                else
                {
                    value++;
                }
            }

            if (finish) break;
        }

        return (int)(Math.floor((value - 1) / (double)dim) * dim);
    }

    public static boolean isMatrix123n(int[][] A)
    {
        // 1 + 2 + 3 + ... + n // = n(n + 1) / 2
        if (A == null || A.length == 0  || A[0].length != A.length)
            return false; // invalid matrix (nullable bool)

        int n = A.length;
        int value = 1;

        for (int j = 0; j < n; j++)
        {
            for (int i = 0; i < n; i++)
            {
                if (A[j][i] != value) {

                    if (j == n - 1 && i == n - 1 && A[n - 1][n - 1] == 0)
                        return true;

                    return false;
                }
                else
                    value++;
            }
        }

        return true;
    }

    public static int getValueOfMatrixByIndex(int[][] A, int index)
    {
        if (A == null || A.length == 0  || A[0].length != A.length)
            return -1;

        int counter = 0;
        int dim = A.length;

        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (counter == index)
                    return A[i][j];

                counter++;
            }
        }

        return - 1;
    }

    private static Tuple<Integer, Integer> getIndexOfValue(int index, int[][] A)
    {
        if (A == null || A.length == 0  || A[0].length != A.length)
            return null;

        int counter = 0;
        int dim = A.length;

        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (counter == index)
                    return new Tuple<>(i, j);

                counter++;
            }
        }

        return null;
    }

    public static <T> int indexOf(T[] arr, T value)
    {
        if (arr == null)
            return -1;

        for (int i = 0; i < arr.length; i++)
        {
            if (value == arr[i])
                return i;
        }

        return -1;
    }

    public static void swapMatrix(int i, int j, int[][] A)
    {
        // i and j are index like 30, 40, so they must be converted to real indexes
        Tuple<Integer, Integer> iTup = getIndexOfValue(i, A);
        Tuple<Integer, Integer> jTup = getIndexOfValue(j, A);

        if (iTup == null || jTup == null)
            return;

        int temp = A[iTup.getE()][iTup.getF()];
        A[iTup.getE()][iTup.getF()] = A[jTup.getE()][jTup.getF()];
        A[jTup.getE()][jTup.getF()] = temp;
    }

}
