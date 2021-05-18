package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NumberHelper
{
    public static List<List<Integer>> GeneratePossibleCombinations(int dim, int n)
    {
        int count = dim * dim - 1;

        List<List<Integer>> lists = new ArrayList<>();
        List<List<Integer>> finalLists = new ArrayList<>();

        for (int i = 1; i <= count; i++)
        {
            lists.add(new ArrayList<Integer>());
            for (int x = i; x <= count + i; x++)
            {
                int value = x < count ? x % count : x % (count + 1);
                if (value != 0)
                    lists.get(i - 1).add(value);
            }
        }

        int listCounter = 0;
        while (listCounter < n)
        {
            for (List<Integer> list : lists)
            {
                int finalCounter = 0;
                boolean added = false;

                while (!added)
                {
                    Collections.shuffle(list);

                    if (finalCounter < n)
                    {
                        List<Integer> lstNew = Helper.clone(list);

                        // Check also that this value is valid
                        // Counting all fields which are greater than the fields before (sum)
                        int sum = 0;
                        for (int s = 1; s < lstNew.size(); s++)
                        {
                            int value = lstNew.get(s);

                            for (int i = s; i >= 0; i--)
                            {
                                int valueBefore = lstNew.get(i);

                                if (valueBefore > value)
                                    sum++;
                            }
                        }

                        if (sum % 2 == 0)
                        {
                            if (finalLists.size() == 0 || finalLists.size() - 1 < listCounter)
                                finalLists.add(new ArrayList<Integer>());

                            finalLists.get(listCounter++).addAll(lstNew);
                            finalCounter++;
                            added = true;
                        }
                    }
                    else
                        return finalLists;
                }
            }
        }
        return finalLists;
    }
}
