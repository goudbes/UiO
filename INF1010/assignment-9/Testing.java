public class Testing {
  public static void main(String[] args) {
    String[] A = {"Echo","November","Oscar"};
    String[] B = {"Fox","Mike","Sierra"};
    String[] C = new String[6];
    C = merge(A,B,C);

    for (int i = 0; i<C.length;i++) {
      System.out.println(C[i]);
    }


  }
  public static String[] merge(String[] A, String[] B, String[] C) {
    int i, j, k, m, n;
    i = 0;
    j = 0;
    k = 0;
    m = A.length;
    n = B.length;
    while (i < m && j < n) {
      if (A[i].compareTo(B[j])<=0) {
        C[k] = A[i];
        i++;
      } else {
        C[k] = B[j];
        j++;
      }
      k++;
    }
    if (i < m) {
      for (int p = i; p < m; p++) {
        C[k] = A[p];
        k++;
      }
    } else {
      for (int p = j; p < n; p++) {
        C[k] = B[p];
        k++;
      }
    }
    return C;
  }
}
