import java.util.ArrayList;
public class ThreadW implements Runnable {
  private String[] a;
  private int lo;
  private int hi;
  private int id;


  /**
  *Constructor
  *@param a array to sort
  *@param lo lower index of the sorting part
  *@param hi higher index of the sorting part
  *@param id thread id
  */
  public ThreadW(String[] a, int lo, int hi, int id) {
    this.a = a;
    this.lo = lo;
    this.hi = hi;
    this.id=id;
  }

  public void run() {
    try{
      sorting();
    }
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }

  /**
  *Crunching array to the part that needs to be sorted
  * sorting it and then adding to the monitors queue.
  * And picking up a new pair to merge, then putting it back.
  */
  public void sorting() throws InterruptedException{
    //Taking a part that needs to be sorted and putting it in new array
    String[] arrayToSort = new String[hi-lo+1];
    int k = 0;
    for (int i = lo; i<=hi;i++) {
      arrayToSort[k]=a[i];
      k++;
    }

    //Sorting the array
    qsort(arrayToSort,0,arrayToSort.length-1);
    //Adding ready sorted array to the queue
    Monitor.queue.add(arrayToSort);
    //Getting a pair to merge
    ArrayList<String[]> pair = Monitor.getPair();
    //Merging it if it's not null
    if(pair!=null){
      Monitor.queue.add(merge(pair.get(0),pair.get(1)));
    }
  }




  /**
  *Sorting algorithm - Quicksort
  *https://en.wikipedia.org/wiki/Quicksort
  */
  public static void qsort(String[] words,int lo, int hi) {
    if (lo<hi) {
      int p = partition(words,lo,hi);
      qsort(words,lo,p-1);
      qsort(words,p+1,hi);
    }
  }

  //Finding the pivot (we take the one far right)
  //Then both j and i start in the beginning. If a word has less value than pivot
  //j and i swap with each other and move on. If a word has higher value than pivot,
  //i stays and j moves on, then they swap when j finds something smaller
  // than pivot. i always stays back with the first word that has value higher than pivot.
  //Then when the loop is done, pivot and i switch places.
  public static int partition(String[] words, int lo, int hi) {
    String pivot = words[hi];
    int i = lo;

    for (int j = lo; j<hi;j++) {
      if (words[j].compareTo(pivot)<=0) {
        String tmp = words[i];
        words[i] = words[j];
        words[j] = tmp;
        i++;
      }
    }
    String tmp = words[i];
    words[i] = words[hi];
    words[hi] = tmp;
    return i;
  }

  /**
  *Merging two sorted arrays into one. Returns merged array
  *@param firstArray first array to merge
  *@param secondArray second array to merge
  */
  public static String[] merge(String[] firstArray, String[] secondArray) {
    String[] readyMerged = new String[firstArray.length+secondArray.length];
    int i, j, k, m, n;
    i = 0;
    j = 0;
    k = 0;
    while (i < firstArray.length && j < secondArray.length) {
      if (firstArray[i].compareTo(secondArray[j])<=0) {
        readyMerged[k] = firstArray[i];
        i++;
      } else {
        readyMerged[k] = secondArray[j];
        j++;
      }
      k++;
    }
    if (i < firstArray.length) {
      for (int p = i; p < firstArray.length; p++) {
        readyMerged[k] = firstArray[p];
        k++;
      }
    } else {
      for (int p = j; p < secondArray.length; p++) {
        readyMerged[k] = secondArray[p];
        k++;
      }
    }
    return readyMerged;
  }

  /**
  *Returns id of thread
  */
  public int getid(){
    return id;
  }
}
