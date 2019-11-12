/**
   This program demonstrates the QuickSort algorithm.
*/

public class QuickSortByID
{
   /**
      The quickSort method uses the QuickSort algorithm to
      sort array, from array[start] through array[end].
      
      @param array The array to sort.
      @param start The starting subscript of the region
                   to sort.
      @param end The ending subscript of the region
                 to sort.
   */
   
   public static void quickSort(DLinkedList<Inventory> array, int start,
                                int end)
   {
      int pivotPoint;
      
      if (start < end)
      {
         // Get the pivot point.
         pivotPoint = partition(array, start, end);
         
         // Sort the first sub list.
         quickSort(array, start, pivotPoint - 1);
         
         // Sort the second sub list.
         quickSort(array, pivotPoint + 1, end);
      }
   }

   /**
      The partition method selects the value in the middle
      of the array as the pivot. The list is rearranged so
      all the values less than the pivot are on its left
      and all the values greater than pivot are on its
      right.
      
      @param array The array being sorted.
      @param start The starting subscript of the region
                   to sort.
      @param end The ending subscript of the region
                 to sort.
   */
   
   private static int partition(DLinkedList<Inventory> array, int start,
                                int end)
   {
      int pivotValue, pivotIndex, mid;

      mid = (start + end) / 2;

      swap(array, start, mid);
      pivotIndex = start;
      pivotValue = Integer.parseInt(array.get(start).get_ID().substring(4));
      for (int scan = start + 1; scan <= end; scan++)
      {
         if (Integer.parseInt(array.get(scan).get_ID().substring(4)) < pivotValue)
         {
            pivotIndex++;
            swap(array, pivotIndex, scan);
         }
      }     
      swap(array, start, pivotIndex);
      
      return pivotIndex;
   }
   
   /**
      The swap method swaps the element at array[a] with
      the element at array[b].
      @param array The array containing the elements.
      @param The first element to swap.
      @param The second element to swap.
   */
   
   private static void swap(DLinkedList<Inventory> array, int a, int b)
   {
      Inventory temp;
      
      temp = array.get(a);
      array.set(a, array.get(b));
      array.set(b, temp);
   }
}
