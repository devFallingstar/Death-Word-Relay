package GameSystem;

import java.io.IOException;

public class WordGame {
	private static int win, lose;
	private static int round;

	private static String prevWord;
	private static String curWord;

	public WordGame() {
		WordGame.win = 0;
		WordGame.lose = 0;
		WordGame.round = 0;
	}

	public void setPrevWord(String _prevWord) {
		WordGame.prevWord = _prevWord;
	}

	public void setCurrentWord(String _curWord) {
		WordGame.curWord = _curWord;
	}

	public static String getPrevWord() {
		return prevWord;
	}

	public void youWin() {
		round++;
		win++;
	}

	public void youLose() {
		round++;
		lose++;
	}

	public int isFinished() {
		if (win == 3) {
			return 1;
		} else if (lose == 3) {
			return -1;
		}
		return 0;
	}

	public static String getCurWord() {
		return curWord;
	}

	public boolean isCorrect() throws IOException {
		return checkLength() && checkWithOnline() && checkWithOffline();
	}

	public boolean checkLength() {
		if (curWord.length() > 4 || curWord.length() < 2) {
			return false;
		}
		if (!curWord.matches("[가-힣]*")) {
			return false;
		}
		return true;
	}

	public boolean checkWithOnline() throws IOException {
		return SearchNaverDic.checkWordFromNaverDic(curWord);
	}

	public boolean checkWithOffline() {
		int lastWordIdx = prevWord.length() - 1;

		if (curWord.startsWith(prevWord.substring(lastWordIdx))) {
			return true;
		} else {
			String lastWord = prevWord.substring(lastWordIdx);
			if (lastWord.equals("녀") && curWord.startsWith("여"))
				return true;
			if (lastWord.equals("녑") && curWord.startsWith("엽"))
				return true;
			if (lastWord.equals("녁") && curWord.startsWith("역"))
				return true;
			if (lastWord.equals("") && curWord.startsWith("엿"))
				return true;
			if (lastWord.equals("념") && curWord.startsWith("염"))
				return true;
			if (lastWord.equals("년") && curWord.startsWith("연"))
				return true;
			if (lastWord.equals("녕") && curWord.startsWith("영"))
				return true;
			if (lastWord.equals("녈") && curWord.startsWith("열"))
				return true;

			if (lastWord.equals("뇨") && curWord.startsWith("요"))
				return true;
			if (lastWord.equals("뇹") && curWord.startsWith("욥"))
				return true;
			if (lastWord.equals("뇩") && curWord.startsWith("욕"))
				return true;
			if (lastWord.equals("뇻") && curWord.startsWith("욧"))
				return true;
			if (lastWord.equals("뇸") && curWord.startsWith("욤"))
				return true;
			if (lastWord.equals("뇬") && curWord.startsWith("욘"))
				return true;
			if (lastWord.equals("뇽") && curWord.startsWith("용"))
				return true;
			if (lastWord.equals("뇰") && curWord.startsWith("욜"))
				return true;

			if (lastWord.equals("뉴") && curWord.startsWith("유"))
				return true;
			if (lastWord.equals("늅") && curWord.startsWith("윱"))
				return true;
			if (lastWord.equals("뉵") && curWord.startsWith("육"))
				return true;
			if (lastWord.equals("T") && curWord.startsWith("윳"))
				return true;
			if (lastWord.equals("늄") && curWord.startsWith("윰"))
				return true;
			if (lastWord.equals("H") && curWord.startsWith("윤"))
				return true;
			if (lastWord.equals("늉") && curWord.startsWith("융"))
				return true;
			if (lastWord.equals("뉼") && curWord.startsWith("율"))
				return true;

			if (lastWord.equals("니") && curWord.startsWith("이"))
				return true;
			if (lastWord.equals("닙") && curWord.startsWith("입"))
				return true;
			if (lastWord.equals("닛") && curWord.startsWith("잇"))
				return true;
			if (lastWord.equals("님") && curWord.startsWith("임"))
				return true;
			if (lastWord.equals("닌") && curWord.startsWith("인"))
				return true;
			if (lastWord.equals("닝") && curWord.startsWith("잉"))
				return true;
			if (lastWord.equals("닐") && curWord.startsWith("일"))
				return true;
			if (lastWord.equals("닉") && curWord.startsWith("익"))
				return true;

			if (lastWord.equals("랴") && curWord.startsWith("야"))
				return true;
			if (lastWord.equals("d") && curWord.startsWith("얍"))
				return true;
			if (lastWord.equals("략") && curWord.startsWith("약"))
				return true;
			if (lastWord.equals("럇") && curWord.startsWith("얏"))
				return true;
			if (lastWord.equals("c") && curWord.startsWith("얌"))
				return true;
			if (lastWord.equals("랸") && curWord.startsWith("얀"))
				return true;
			if (lastWord.equals("량") && curWord.startsWith("양"))
				return true;
			if (lastWord.equals("U") && curWord.startsWith("얄"))
				return true;

			if (lastWord.equals("려") && curWord.startsWith("여"))
				return true;
			if (lastWord.equals("령") && curWord.startsWith("영"))
				return true;
			if (lastWord.equals("련") && curWord.startsWith("연"))
				return true;
			if (lastWord.equals("렵") && curWord.startsWith("엽"))
				return true;
			if (lastWord.equals("력") && curWord.startsWith("역"))
				return true;
			if (lastWord.equals("렷") && curWord.startsWith("엿"))
				return true;
			if (lastWord.equals("렴") && curWord.startsWith("염"))
				return true;
			if (lastWord.equals("렬") && curWord.startsWith("열"))
				return true;
			if (lastWord.equals("") && curWord.startsWith("옆"))
				return true;

			if (lastWord.equals("례") && curWord.startsWith("예"))
				return true;
			if (lastWord.equals("") && curWord.startsWith("옘"))
				return true;
			if (lastWord.equals("롄") && curWord.startsWith("옌"))
				return true;
			if (lastWord.equals("") && curWord.startsWith(""))
				return true;
			if (lastWord.equals("") && curWord.startsWith("옐"))
				return true;

			if (lastWord.equals("료") && curWord.startsWith("요"))
				return true;
			if (lastWord.equals("룝") && curWord.startsWith("욥"))
				return true;
			if (lastWord.equals("") && curWord.startsWith("욕"))
				return true;
			if (lastWord.equals("룟") && curWord.startsWith("욧"))
				return true;
			if (lastWord.equals("") && curWord.startsWith("욤"))
				return true;
			if (lastWord.equals("룐") && curWord.startsWith("욘"))
				return true;
			if (lastWord.equals("룡") && curWord.startsWith("용"))
				return true;
			if (lastWord.equals("룔") && curWord.startsWith("욜"))
				return true;

			if (lastWord.equals("류") && curWord.startsWith("유"))
				return true;
			if (lastWord.equals("률") && curWord.startsWith("율"))
				return true;
			if (lastWord.equals("륙") && curWord.startsWith("육"))
				return true;
			if (lastWord.equals("륨") && curWord.startsWith("윰"))
				return true;
			if (lastWord.equals("륜") && curWord.startsWith("윤"))
				return true;
			if (lastWord.equals("륭") && curWord.startsWith("융"))
				return true;
			if (lastWord.equals("리") && curWord.startsWith("이"))
				return true;
			if (lastWord.equals("립") && curWord.startsWith("입"))
				return true;
			if (lastWord.equals("릭") && curWord.startsWith("익"))
				return true;
			if (lastWord.equals("림") && curWord.startsWith("임"))
				return true;
			if (lastWord.equals("린") && curWord.startsWith("인"))
				return true;
			if (lastWord.equals("링") && curWord.startsWith("잉"))
				return true;
			if (lastWord.equals("릴") && curWord.startsWith("일"))
				return true;

			if (lastWord.equals("라") && curWord.startsWith("아"))
				return true;
			if (lastWord.equals("랍") && curWord.startsWith("압"))
				return true;
			if (lastWord.equals("락") && curWord.startsWith("악"))
				return true;
			if (lastWord.equals("람") && curWord.startsWith("암"))
				return true;
			if (lastWord.equals("란") && curWord.startsWith("안"))
				return true;
			if (lastWord.equals("랑") && curWord.startsWith("앙"))
				return true;
			if (lastWord.equals("랄") && curWord.startsWith("알"))
				return true;

			if (lastWord.equals("래") && curWord.startsWith("애"))
				return true;
			if (lastWord.equals("랩") && curWord.startsWith("앱"))
				return true;
			if (lastWord.equals("랙") && curWord.startsWith("액"))
				return true;
			if (lastWord.equals("랫") && curWord.startsWith("랫"))
				return true;
			if (lastWord.equals("램") && curWord.startsWith("앰"))
				return true;
			if (lastWord.equals("랜") && curWord.startsWith("앤"))
				return true;
			if (lastWord.equals("랭") && curWord.startsWith("앵"))
				return true;
			if (lastWord.equals("랠") && curWord.startsWith("앨"))
				return true;

			if (lastWord.equals("로") && curWord.startsWith("오"))
				return true;
			if (lastWord.equals("롭") && curWord.startsWith("옵"))
				return true;
			if (lastWord.equals("록") && curWord.startsWith("옥"))
				return true;
			if (lastWord.equals("롯") && curWord.startsWith("옷"))
				return true;
			if (lastWord.equals("롬") && curWord.startsWith("옴"))
				return true;
			if (lastWord.equals("롱") && curWord.startsWith("옹"))
				return true;
			if (lastWord.equals("론") && curWord.startsWith("온"))
				return true;
			if (lastWord.equals("롤") && curWord.startsWith("올"))
				return true;
			if (lastWord.equals("뢰") && curWord.startsWith("외"))
				return true;

			if (lastWord.equals("루") && curWord.startsWith("우"))
				return true;
			if (lastWord.equals("룹") && curWord.startsWith("웁"))
				return true;
			if (lastWord.equals("룩") && curWord.startsWith("욱"))
				return true;
			if (lastWord.equals("룻") && curWord.startsWith("웃"))
				return true;
			if (lastWord.equals("룸") && curWord.startsWith("움"))
				return true;
			if (lastWord.equals("룬") && curWord.startsWith("운"))
				return true;
			if (lastWord.equals("룽") && curWord.startsWith("웅"))
				return true;
			if (lastWord.equals("룰") && curWord.startsWith("울"))
				return true;

			if (lastWord.equals("르") && curWord.startsWith("으"))
				return true;
			if (lastWord.equals("릅") && curWord.startsWith("읍"))
				return true;
			if (lastWord.equals("륵") && curWord.startsWith("윽"))
				return true;
			if (lastWord.equals("릇") && curWord.startsWith("읏"))
				return true;
			if (lastWord.equals("름") && curWord.startsWith("음"))
				return true;
			if (lastWord.equals("른") && curWord.startsWith("은"))
				return true;
			if (lastWord.equals("릉") && curWord.startsWith("응"))
				return true;
			if (lastWord.equals("를") && curWord.startsWith("을"))
				return true;
		}

		return false;
	}

}
