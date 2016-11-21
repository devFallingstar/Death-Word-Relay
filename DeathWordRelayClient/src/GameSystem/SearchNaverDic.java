package GameSystem;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchNaverDic {

	public static boolean checkWordFromNaverDic(String _word) throws IOException {
		String encoded = URLEncoder.encode(_word.trim(), "UTF-8");

		Document doc = Jsoup.connect("http://krdic.naver.com/search.nhn?query=" + encoded).get();
		Elements means = doc.select("li p");
		Elements names = doc.select("div a.fnt15 strong");

		//Check if there's words that same with _word
		if(names.size() == 0){
			return false;
		}
		
		// Check if the word is noun or not.
		for (Element e : means) {
			if (e.text().startsWith("[Έν»η]")) {
				return true;
			}
		}
		return false;
	}

}
