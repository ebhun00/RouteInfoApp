package com.safeway.vpos.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompareTextFiles {
	public static void main(String[] args) throws IOException {

		File[] goldFiles = FileReaderUtil.finder("data/vpos/gold/");

		File[] newFiles = FileReaderUtil.finder("data/vpos/new/");

		Map<String, File> goldFileMap = new HashMap<String, File>();
		Map<String, File> newFileMap = new HashMap<String, File>();

		for (File file : goldFiles) {
			String inputFileName = file.getName();
			goldFileMap.put(inputFileName, file);
		}

		for (File file : newFiles) {
			String inputFileName = file.getName();
			newFileMap.put(inputFileName, file);
		}
		int fileCount = 1;
		StringBuilder comments = new StringBuilder();
		for (Map.Entry<String, File> map : goldFileMap.entrySet()) {

			int lineNum = 1;
			File gold = map.getValue();
			File newfile = newFileMap.get(map.getKey());
			comments.append("File"+fileCount +" : Start  \n");
			comments.append("File comparision between gold file : ").append(map.getKey()).append(" and ").append("new file : ")
					.append(newfile.getName()).append("\n");
			comments.append("--------------------------------------------------------------------------------------------- \n");
			BufferedReader reader1 = new BufferedReader(new FileReader(gold));

			BufferedReader reader2 = new BufferedReader(new FileReader(newfile));

			String line1 = reader1.readLine();

			String line2 = reader2.readLine();

			boolean areEqual = true;


			while (line1 != null || line2 != null) {
				if (line1 == null || line2 == null) {
					areEqual = false;

					break;
				} else if (!line1.equalsIgnoreCase(line2)) {
					areEqual = false;
					comments.append("line "+ lineNum + " in gold is different than line "+ lineNum + " in new file").append("\n");
					// break;
				}

				line1 = reader1.readLine();

				line2 = reader2.readLine();

				lineNum++;
			}

			if (areEqual) {
				comments.append("Files has no difference \n");
			} else {
			}
			comments.append("File "+fileCount +" : End ******************************************************************************** End \n \n");

			fileCount++;
			reader1.close();

			reader2.close();
		}

		System.out.println(comments.toString());
	}
}