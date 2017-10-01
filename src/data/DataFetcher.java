package data;
import java.io.*;
import java.util.ArrayList;

public class DataFetcher {

	public static ArrayList<String> readFromFile(String fileName, int approxNumOfLines) throws IOException {

		ArrayList<String> file_data = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				file_data.add(line);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File '" + fileName + "' not found.");
		}

		return file_data;
	}

}
