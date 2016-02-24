package astappev.extractPersons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * A class to extract pairs of persons.
 */
public class ExtractPersons {
	
	private static String hashFilePath = "E:\\fr-project\\hashTableNames.txt";
	
	HashMap<String, String> nameMap;

	/**
	 * A class to extract pairs of persons.
	 */
	public ExtractPersons() {
		nameMap = new HashMap<String, String>();
		String names;
		try {
			names = FileUtils.readFileToString(new File(hashFilePath));
			for (String name : names.split("\n")) {
				String[] idname = name.split("\t");
				nameMap.put(idname[1], idname[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gives all pairs in content. If you just have a String and want to find
	 * pairs in it, use this function.
	 * 
	 * @param content
	 * @return
	 */
	public List<PosEntity> findPersons(String... contents) {
		List<PosEntity> pers = new ArrayList<PosEntity>();
		for (String content : contents) {
			if (content != null) {
				String[] words = content.replace(",", " ").split("\\s+");
				pers.addAll(extractPersons(words));
			}
		}
		
		return pers;
	}

	/**
	 * Extracts persons from an array of strings. The function goes over the
	 * words and checks if words[i+0] is a name, then words[i+0] + words[i+1]
	 * and so on. If a name contains another name, it will take the longer name.
	 * 
	 * @param words
	 * @return
	 */
	public List<PosEntity> extractPersons(String[] words) {

		int MAX_WORDS_PER_NAME = 3;

		// now extract persons from word list
		List<PosEntity> persons = new LinkedList<PosEntity>();

		for (int i = 0; i < words.length; i++) {
			String candidate = words[i];
			PosEntity foundPerson = null;

			for (int j = 1; j < MAX_WORDS_PER_NAME && i + j < words.length; j++) {
				String word = words[i + j];

				candidate += " " + word;

				String id = nameMap.get(candidate);
				if (id != null) {
					foundPerson = new PosEntity(candidate, i, 0);
					foundPerson.setEndWord(i + j);
					foundPerson.setPersonId(Integer.valueOf(id));
					foundPerson.setStartWord(i);
				}
			}

			if (foundPerson != null) // found a person name
			{

				persons.add(foundPerson);

				i += foundPerson.getLength() - 1; // jump text cursor to end of
												  // found name
			}
		}
		return persons;
	}

	/**
	 * main function for debugging.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ExtractPersons ep = new ExtractPersons();

		// String content = "British Prime Minister David Cameron (L) and his
		// wife Samantha greet German Chancellor Angela Merkel and her husband
		// Joachim Sauer following the arrival of the ...";
		String content = "Angela Merkel Johann Sebastian Bach";
		List<PosEntity> pairlist = ep.findPersons(content);
		
		System.out.println(pairlist);
	}

}
