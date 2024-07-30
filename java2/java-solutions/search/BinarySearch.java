package search;

public class BinarySearch {
    public static void main(String[] args) {
        //Integer.MINVALUE <= Integer.parseInt(args[0]) <= Integer.MAXVALUE
        int x = Integer.parseInt(args[0]);
        //Integer.MINVALUE <= x <= Integer.MAXVALUE
        int l = args.length;
        //1 <= l <= Integer.MAXVALUE
        int[] a = new int[l - 1];
        //a.length >= 0

        //Pred: a.length > 0 && args.length > 1
        for (int i = 1; i < l; i++) {
            //i < args.length && args[i].isDigit()
            a[i - 1] = Integer.parseInt(args[i]);
            //a[i - 1] = (int) args[i]
        }
        //Post: a.length >= 0 && for i (1..l): a[i - 1] = args[i]

        SearchMethods s = new SearchMethods();
        //s = new SearchMethods()
//        s.recursiveBin(a, x, -1, l - 1);
//        System.out.println(s.value);
        //a[s.value] <= x && a[s.value - 1] > x
        System.out.println(s.iterativeSearch(a, x, -1, l - 1));
    }
}