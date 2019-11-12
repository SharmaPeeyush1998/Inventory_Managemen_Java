import java.util.Scanner;

/**
   This program demonstrates the binary search method in
   the ArrayTools class.
*/

public class BinarySearchByID
{
   /**
      The binarySearch method performs a binary search on an
      integer array. The array is searched for the number passed
      to value. If the number is found, its array subscript is
      returned. Otherwise, -1 is returned indicating the value was
      not found in the array.
      @param array The array to search.
      @param value The value to search for.
   */

   public static int binarySearch(DLinkedList<Inventory> array, String value)
   {
      int first;       // First array element
      int last;        // Last array element
      int middle;      // Midpoint of search
      int position;    // Position of search value
      boolean found;   // Flag

      // Set the inital values.
      first = 0;
      last = array.size() - 1;
      position = -1;
      found = false;

      // Search for the value.
      while (!found && first <= last)
      {
         // Calculate midpoint
         middle = (first + last) / 2;
         
         // If value is found at midpoint...
         if (array.get(middle).get_ID().equalsIgnoreCase(value))
         {
            found = true;
            position = middle;
         }
         // else if value is in lower half...
         else if (Integer.parseInt(array.get(middle).get_ID().substring(4)) >Integer.parseInt(value.substring(4)))
            last = middle - 1;
         // else if value is in upper half....
         else
            first = middle + 1;
      }

      // Return the position of the item, or -1
      // if it was not found.
      return position;
   }
}
