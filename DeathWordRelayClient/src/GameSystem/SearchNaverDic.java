package GameSystem;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class is used for search the word that user entered in Naver dictionary.
 * With that searching program can get if the word is available or not.
 * 
 * @author YYS
 *
 */
public class SearchNaverDic {

	/**
	 * Check the word is available in online dictionary system(based on Naver
	 * dictionary).
	 * 
	 * @param _word
	 * @return boolean flag that has information of correct or not.
	 * @throws IOException
	 */
	public static boolean checkWordFromNaverDic(String _word) throws IOException {
		/* Send html request to Naver dictionary */
		String encoded = URLEncoder.encode(_word.trim(), "UTF-8");

		Document doc = Jsoup.connect("http://krdic.naver.com/search.nhn?query=" + encoded).get();
		Elements means = doc.select("li p");
		Elements names = doc.select("div a.fnt15");

		/*
		 * If there's no words, return false
		 */
		if (names.size() == 0) {
			return false;
		}

		/*
		 * If there's some words but not matched with parameter's, return false
		 */
		for (Element e : names) {
			if (!e.text().contains(_word)) {
				return false;
			} else {
				break;
			}
		}

		/*
		 * If there's some words that matched with parameter's, but it's not
		 * noun, return false
		 */
		for (Element e : means) {
			if (e.text().startsWith("[명사]")) {
				return true;
			}
		}
		return false;
	}
}
