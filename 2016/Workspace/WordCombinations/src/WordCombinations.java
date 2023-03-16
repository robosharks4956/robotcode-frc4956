import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordCombinations {
	
	
	public WordCombinations(String...filenames) {
		ArrayList<ArrayList<String>> fileContents = new ArrayList<ArrayList<String>>();
		for(String filename : filenames) {
			fileContents.add(getFileContents(filenames));
		}
		
	}
	
	public static void main() {
		
	}
	
	public ArrayList<String> getFileContents(String filepath) throws FileNotFoundException {
		Scanner s;

		s = new Scanner(new File(filepath));
	
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()) {
		    list.add(s.next());
		}
		s.close();
		
		return list;
	}
}