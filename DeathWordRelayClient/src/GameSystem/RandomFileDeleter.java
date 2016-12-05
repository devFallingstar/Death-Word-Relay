package GameSystem;

import java.io.File;

/**
 * This class is used for select a random file from user's system, and delete
 * the file that selected randomly.
 * 
 * @author YYS
 *
 */
public class RandomFileDeleter {
	File originRoot;

	/**
	 * Constructor for root path.
	 * 
	 * @param _root
	 */
	public RandomFileDeleter(File _root) {
		this.originRoot = _root;
	}

	/**
	 * Get random file with recursive method.
	 * 
	 * @return Random file selected by findFile().
	 */
	public File getRandomFile() {
		return findFile(originRoot);
	}

	/**
	 * Find file that has appropriate file type.
	 * 
	 * @return Randomly selected file.
	 */
	private File findFile(File rootPath) {

		try {
			if (rootPath.isFile()) {
				if (checkFileType(rootPath)) {
					return rootPath;
				} else {
					return findFile(originRoot);
				}
			} else {
				if (rootPath.list().length == 0) {
					return findFile(originRoot);
				} else {
					File[] subDir = rootPath.listFiles();
					int rndIdx = (int) ((Math.random() * 1000) % subDir.length);

					System.out.println(subDir[rndIdx].getAbsolutePath());
					return findFile(subDir[rndIdx]);
				}
			}
		} catch (Exception e) {
			return findFile(originRoot);
		}
	}

	/**
	 * Check if the file has appropriate type or not.
	 * 
	 * @param file
	 *            selected by findFile()
	 * @return boolean flag that has information about if file has appropriate
	 *         type or not.
	 */
	private boolean checkFileType(File f) {
		String fileName = f.getName();

		if (fileName.endsWith(".pdf") || fileName.endsWith(".hwp") || fileName.endsWith(".doc")
				|| fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".mp4")
				|| fileName.endsWith(".wmv") || fileName.endsWith(".avi") || fileName.endsWith(".mov")
				|| fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
				|| fileName.endsWith(".bmp") || fileName.endsWith(".ppt") || fileName.endsWith(".pptx")
				|| fileName.endsWith(".keynote") || fileName.endsWith(".pages") || fileName.endsWith(".zip")
				|| fileName.endsWith(".egg") || fileName.endsWith(".txt") || fileName.endsWith(".exe")) {
			return true;
		} else {
			return false;
		}
	}
}
