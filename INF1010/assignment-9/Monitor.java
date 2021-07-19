import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;

class Monitor{
	//LinkedBlockingQueue is thread safe, based on linked nodes. The queue orders elements FIFO.s

	public static LinkedBlockingQueue <String[]> queue = new LinkedBlockingQueue<String[]>();
	//Final sorted and merged array to return
	public static String[] sorted;
	//Number of words to know when threads are finished.
	private static int wordcnt;

	/**
	*Returns a pair of arrays to be further merged
	*Method take() Retrieves and removes the head of this queue, waiting if necessary
	* until an element becomes available.
	*/
	public static synchronized ArrayList<String[]> getPair() throws InterruptedException{
		//Those arrays are going to be returned in arrayList
		ArrayList <String[]> pair = new ArrayList<String[]>();
		//Taking first array
		pair.add(queue.take());

		//Making sure the first array isn't the final array, or it is :)
		if(pair.get(0).length == wordcnt){
			sorted = pair.get(0);
			return null;
		}
		//Taking the second array
		pair.add(queue.take());
		return pair;
	}

	/**
	*Sets the size of final array
	*@param n size of final array
	*/
	public static void setFinalSize(int n){
		wordcnt=n;
	}

	/**
	* Returns sorted and merged arrayList
	*/
	public String[] returnSorted(){
		return sorted;
	}
}
