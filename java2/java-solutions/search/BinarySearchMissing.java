package search;

public class BinarySearchMissing {
   public static void main(String[] args) {
       //Integer.MINVALUE <= Integer.parseInt(args[0]) <= Integer.MAXVALUE
       int x = Integer.parseInt(args[0]);
       //Integer.MINVALUE <= x <= Integer.MAXVALUE
       // args.length > 0
       int l = args.length;
       // l > 0
       int[] a = new int[l - 1];
       //a.length >= 0

       //Pred: a.length > 0 && args.length > 1
       for (int i = 1; i < l; i++) {
           //Integer.MINVALUE <= Integer.parseInt(args[i]) <= Integer.MAXVALUE
           a[i - 1] = Integer.parseInt(args[i]);
           //Integer.MINVALUE <= a[i - 1] <= Integer.MAXVALUE &&
           // for i (1..l): a[i] <= a[i+1]
       }
       //Post: a.length > 0 && for i (1..l): a[i - 1] = args[i] && for i (1..l): a[i] <= a[i+1] ||
       //a.length == 0

       SearchMethods s = new SearchMethods();
       //s = new SearchMethods()

       //a.length > 0 && for i (1..l): a[i - 1] = args[i] && for i (1..l): a[i] <= a[i+1] ||
       //a.length == 0 && l == -1 && r == a.length
       int result = s.modBin(a, x, -1, l - 1);
       //result == i: (a[i] >= x) || result == -r - 1

//       s.recursiveModBin(a, x, -1, l - 1);
//       int result = s.value;

       System.out.println(result);
   }
}
