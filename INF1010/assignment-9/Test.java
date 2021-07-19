import java.util.*;
class Test{
	public static void main(String[] args) {

	String[] array1 = {"a", "s", "d"};
	String[] array2 = {"b", "g", "s"};
	String[] array3 = {"e", "e", "w"};

	String[] array [] = {array1, array2, array3};//= new String[3][3];
	//array[0][0] = array1;

	//{array}
		for(String[] array12: array){
			System.out.println(Arrays.toString(array12));
		}
	}
}
