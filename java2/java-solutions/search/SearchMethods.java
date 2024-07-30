package search;

public class SearchMethods {

    //true
    protected int value = 0;
    //value == 0


    //((a.length > 0 && for i (1..l): a[i] <= a[i+1]) ||
    //a.length == 0) && l == -1 && r == a.length
    public int iterativeSearch(int[] a, int x, int l, int r) {
        int m;
        // I: l == -1 && r == a.length && l <= l' && r >= r' && -1 <= l' <= a.length - 1 && 0 <= r' <= a.length
        while (r - l > 1) {
            //(r' - l') > 1
            //(r' - l') > 1 && l' < (l + r) / 2 < r'
            m = (l + r) / 2;
            //(r' - l') > 1 && l' < m < r'
            if (a[m] <= x) {
                //r' - l' > 1 && l' < m < r' && a[m] <= x
                r = m;
                //r' == m && a[r'] <= x
            } else {
                //(r' - l') > 1 && l' < m < r' && a[m] > x
                l = m;
                //l' == m && a[l'] > x
            }
        }
        // I && r' - l' <= 1 && (a[r'] <= x || r == a.length)
        return r;
    }

    //Pred: for i (0..a.length): a[i] >= a[i+1]
    public void recursiveBin(int[] a, int x, int l, int r) {
        if (r - l > 1) {
            //(r' - l') == (r - l) > 1 && l' < (l + r) / 2 < r'
            int m = (l + r) / 2;
            //(r' - l') == (r - l) > 1 && l' < m < r'
            if (a[m] <= x) {
                //(r' - l') == (r - l) > 1 && m = (l' + r') / 2 && a[m] <= x
                recursiveBin(a, x, l, m);
                //r' == m && a[r'] <= x
            } else {
                //(r' - l') == (r - l) > 1 && m = (l' + r') / 2 && a[m] > x
                recursiveBin(a, x, m, r);
                //l' == m && a[l'] > x
            }
        } else {
            //((r' - l') == (r - l)) <= 1 && a[r'] <= x
            value = r;
            //value == r' && (a[value] <= x || value == a.length)
        }
    }

    //((a.length > 0 && for i (1..l): a[i] <= a[i+1]) ||
    //a.length == 0) && l == -1 && r == a.length
    public int modBin (int[] a, int x, int l, int r) {
        int m;
        // I: l == -1 && r == a.length && l <= l' && r >= r' && -1 <= l' <= a.length - 1 && 0 <= r' <= a.length
        while (r - l > 1) {
            //r' - l' > 1 && l' <= (l + r) / 2 <= r'
            m = (l + r) / 2;
            //r' - l' > 1 && l' <= m <= r'
            if (a[m] >= x) {
                //r' - l' > 1 && l' <= m <= r' && a[m] >= x
                r = m;
                //r' - l' > 1 && l' <= m <= r' && a[r'] >= x
            } else {
                //r' - l' > 1 && l' <= m <= r' && a[m] < x
                l = m;
                //r' - l' > 1 && l' <= m <= r' && a[l'] < x
            }
        }
        //r' - l' <= 1 && (a[r'] >= x || r == a.length)
        if (r < a.length) {
            //r' - l' <= 1 && r' < a.length
            if (a[r] == x) {
                //r' - l' <= 1 && r' < a.length && a[r'] == x
                return r;
            } else {
                //r' - l' <= 1 && r' < a.length && a[r'] != x
                return -r - 1;
            }
        } else {
            //r' - l' <= 1 && r' == r == a.length
            return -r -1;
        }
        //r' == i: (a[i] >= x) || r' == -r' - 1
    }

    //(a.length > 0 && for i (1..l): a[i] <= a[i+1]) ||
    //a.length == 0
    public void recursiveModBin (int[] a, int x, int l, int r) {
        //r == r' && l == l'
        if (r - l > 1) {
            //r == r' && l == l' && r' - l' > 1 && l' <= (r + l) / 2 <= r'
            int m = (r + l) / 2;
            //r == r' && l == l' && r' - l' > 1 && l' <= m <= r'
            if (a[m] >= x) {
                //r == r' && l == l' && r' - l' > 1 && l' <= m <= r' && a[m] >= x
                r = m;
                //l == l' && r' - l' > 1 && a[r'] >= x
                recursiveModBin(a, x, l, r);
            } else {
                //r == r' && l == l' && r' - l' > 1 && l' <= m <= r' && a[m] >= x
                l = m;
                //r == r' && r' - l' > 1 && a[l'] < x
                recursiveModBin(a, x, l, r);
            }
        } else {
            //r == r' && l == l' && r' - l' <= 1
            if (r < a.length) {
                //r == r' && l == l' && r' - l' <= 1 && r' < a.length
                if (a[r] == x) {
                    //r == r' && l == l' && r' - l' <= 1 && r' < a.length && a[r'] == x
                    value = r;
                    //value == r' && l == l' && value - l' <= 1 && value < a.length && a[value] == x
                } else {
                    //r == r' && l == l' && r' - l' <= 1 && r' < a.length && -r' - 1 <= 0 && a[r'] != x
                    value = -r - 1;
                    //value = -r - 1 && value <= 0
                }
            } else {
                //r == r' && l == l' && r' - l' <= 1 && r' >= a.length && -r' - 1 <= 0 && a[r'] != x
                value = -r - 1;
                ////value = -r - 1 && value <= 0
            }
        }
    }
}