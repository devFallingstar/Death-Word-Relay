package GameSystem;

import java.io.IOException;

public class WordGame {
	private static int win, lose;
	private static int round;
	
	private static String prevWord;
	private static String curWord;
	
	public WordGame(){
		WordGame.win = 0;
		WordGame.lose = 0;
		WordGame.round = 0;
	}
	
	public void setPrevWord(String _prevWord){
		WordGame.prevWord = _prevWord;
	}
	public void setCurrentWord(String _curWord){
		WordGame.curWord = _curWord;
	}
	public static String getPrevWord() {
		return prevWord;
	}
	
	public void youWin(){
		round++;
		win++;
	}
	public void youLose(){
		round++;
		lose++;
	}
	public int isFinished(){
		if(win == 3){
			return 1;
		}else if(lose == 3){
			return -1;
		}
		return 0;
	}
	

	public static String getCurWord() {
		return curWord;
	}

	public boolean isCorrect() throws IOException{
		return checkLength() && checkWithOnline() && checkWithOffline();
	}
	
	public boolean checkLength(){
		if(curWord.length() > 4 || curWord.length() < 2){
			return false;
		}
		if(!curWord.matches("[°¡-Èþ]*")){
			return false;
		}
		return true;
	}
	
	public boolean checkWithOnline() throws IOException{		
		return SearchNaverDic.checkWordFromNaverDic(curWord);
	}
	
	public boolean checkWithOffline(){
		int lastWordIdx = prevWord.length()-1;
		
		if(curWord.startsWith(prevWord.substring(lastWordIdx))){
			return true;
		}else{
			String lastWord = prevWord.substring(lastWordIdx);
			if(lastWord.equals("³à") && curWord.startsWith("¿©"))
				return true;
			if(lastWord.equals("³å") && curWord.startsWith("¿±"))
				return true;
			if(lastWord.equals("³á") && curWord.startsWith("¿ª"))
				return true;
			if(lastWord.equals("†Ç") && curWord.startsWith("¿³"))
				return true;
			if(lastWord.equals("³ä") && curWord.startsWith("¿°"))
				return true;
			if(lastWord.equals("³â") && curWord.startsWith("¿¬"))
				return true;
			if(lastWord.equals("³ç") && curWord.startsWith("¿µ"))
				return true;
			if(lastWord.equals("³ã") && curWord.startsWith("¿­"))
				return true;
			
			if(lastWord.equals("´¢") && curWord.startsWith("¿ä"))
				return true;
			if(lastWord.equals("´¦") && curWord.startsWith("¿é"))
				return true;
			if(lastWord.equals("´£") && curWord.startsWith("¿å"))
				return true;
			if(lastWord.equals("´§") && curWord.startsWith("¿ê"))
				return true;
			if(lastWord.equals("‡œ") && curWord.startsWith("¿è"))
				return true;
			if(lastWord.equals("´¤") && curWord.startsWith("¿æ"))
				return true;
			if(lastWord.equals("´¨") && curWord.startsWith("¿ë"))
				return true;
			if(lastWord.equals("´¥") && curWord.startsWith("¿ç"))
				return true;
			
			if(lastWord.equals("´º") && curWord.startsWith("À¯"))
				return true;
			if(lastWord.equals("´¾") && curWord.startsWith("À´"))
				return true;
			if(lastWord.equals("´»") && curWord.startsWith("À°"))
				return true;
			if(lastWord.equals("ˆT") && curWord.startsWith("Àµ"))
				return true;
			if(lastWord.equals("´½") && curWord.startsWith("À³"))
				return true;
			if(lastWord.equals("ˆH") && curWord.startsWith("À±"))
				return true;
			if(lastWord.equals("´¿") && curWord.startsWith("À¶"))
				return true;
			if(lastWord.equals("´¼") && curWord.startsWith("À²"))
				return true;
			
			if(lastWord.equals("´Ï") && curWord.startsWith("ÀÌ"))
				return true;
			if(lastWord.equals("´Õ") && curWord.startsWith("ÀÔ"))
				return true;
			if(lastWord.equals("´Ö") && curWord.startsWith("ÀÕ"))
				return true;
			if(lastWord.equals("´Ô") && curWord.startsWith("ÀÓ"))
				return true;
			if(lastWord.equals("´Ñ") && curWord.startsWith("ÀÎ"))
				return true;
			if(lastWord.equals("´×") && curWord.startsWith("À×"))
				return true;
			if(lastWord.equals("´Ò") && curWord.startsWith("ÀÏ"))
				return true;
			if(lastWord.equals("´Ð") && curWord.startsWith("ÀÍ"))
				return true;
			
			if(lastWord.equals("·ª") && curWord.startsWith("¾ß"))
				return true;
			if(lastWord.equals("Žd") && curWord.startsWith("¾å"))
				return true;
			if(lastWord.equals("·«") && curWord.startsWith("¾à"))
				return true;
			if(lastWord.equals("·­") && curWord.startsWith("¾æ"))
				return true;
			if(lastWord.equals("Žc") && curWord.startsWith("¾ä"))
				return true;
			if(lastWord.equals("·¬") && curWord.startsWith("¾á"))
				return true;
			if(lastWord.equals("·®") && curWord.startsWith("¾ç"))
				return true;
			if(lastWord.equals("ŽU") && curWord.startsWith("¾â"))
				return true;
			
			if(lastWord.equals("·Á") && curWord.startsWith("¿©"))
				return true;
			if(lastWord.equals("·É") && curWord.startsWith("¿µ"))
				return true;
			if(lastWord.equals("·Ã") && curWord.startsWith("¿¬"))
				return true;
			if(lastWord.equals("·Æ") && curWord.startsWith("¿±"))
				return true;
			if(lastWord.equals("·Â") && curWord.startsWith("¿ª"))
				return true;
			if(lastWord.equals("·Ç") && curWord.startsWith("¿³"))
				return true;
			if(lastWord.equals("·Å") && curWord.startsWith("¿°"))
				return true;
			if(lastWord.equals("·Ä") && curWord.startsWith("¿­"))
				return true;
			if(lastWord.equals("ŽÆ") && curWord.startsWith("¿·"))
				return true;
			
			if(lastWord.equals("·Ê") && curWord.startsWith("¿¹"))
				return true;
			if(lastWord.equals("ŽÖ") && curWord.startsWith("¿¼"))
				return true;
			if(lastWord.equals("·Ë") && curWord.startsWith("¿º"))
				return true;
			if(lastWord.equals("ŽÙ") && curWord.startsWith("žŸ"))
				return true;
			if(lastWord.equals("ŽÎ") && curWord.startsWith("¿»"))
				return true;
			
			if(lastWord.equals("·á") && curWord.startsWith("¿ä"))
				return true;
			if(lastWord.equals("·ä") && curWord.startsWith("¿é"))
				return true;
			if(lastWord.equals("‹") && curWord.startsWith("¿å"))
				return true;
			if(lastWord.equals("·å") && curWord.startsWith("¿ê"))
				return true;
			if(lastWord.equals("˜") && curWord.startsWith("¿è"))
				return true;
			if(lastWord.equals("·â") && curWord.startsWith("¿æ"))
				return true;
			if(lastWord.equals("·æ") && curWord.startsWith("¿ë"))
				return true;
			if(lastWord.equals("·ã") && curWord.startsWith("¿ç"))
				return true;
			
			if(lastWord.equals("·ù") && curWord.startsWith("À¯"))
				return true;
			if(lastWord.equals("·ü") && curWord.startsWith("À²"))
				return true;
			if(lastWord.equals("·ú") && curWord.startsWith("À°"))
				return true;
			if(lastWord.equals("·ý") && curWord.startsWith("À³"))
				return true;
			if(lastWord.equals("·û") && curWord.startsWith("À±"))
				return true;
			if(lastWord.equals("¸¢") && curWord.startsWith("À¶"))
				return true;
			if(lastWord.equals("¸®") && curWord.startsWith("ÀÌ"))
				return true;
			if(lastWord.equals("¸³") && curWord.startsWith("ÀÔ"))
				return true;
			if(lastWord.equals("¸¯") && curWord.startsWith("ÀÍ"))
				return true;
			if(lastWord.equals("¸²") && curWord.startsWith("ÀÓ"))
				return true;
			if(lastWord.equals("¸°") && curWord.startsWith("ÀÎ"))
				return true;
			if(lastWord.equals("¸µ") && curWord.startsWith("À×"))
				return true;
			if(lastWord.equals("¸±") && curWord.startsWith("ÀÏ"))
				return true;
			
			if(lastWord.equals("¶ó") && curWord.startsWith("¾Æ"))
				return true;
			if(lastWord.equals("¶ø") && curWord.startsWith("¾Ð"))
				return true;
			if(lastWord.equals("¶ô") && curWord.startsWith("¾Ç"))
				return true;
			if(lastWord.equals("¶÷") && curWord.startsWith("¾Ï"))
				return true;
			if(lastWord.equals("¶õ") && curWord.startsWith("¾È"))
				return true;
			if(lastWord.equals("¶û") && curWord.startsWith("¾Ó"))
				return true;
			if(lastWord.equals("¶ö") && curWord.startsWith("¾Ë"))
				return true;
			
			if(lastWord.equals("·¡") && curWord.startsWith("¾Ö"))
				return true;
			if(lastWord.equals("·¦") && curWord.startsWith("¾Û"))
				return true;
			if(lastWord.equals("·¢") && curWord.startsWith("¾×"))
				return true;
			if(lastWord.equals("·§") && curWord.startsWith("·§"))
				return true;
			if(lastWord.equals("·¥") && curWord.startsWith("¾Ú"))
				return true;
			if(lastWord.equals("·£") && curWord.startsWith("¾Ø"))
				return true;
			if(lastWord.equals("·©") && curWord.startsWith("¾Þ"))
				return true;
			if(lastWord.equals("·¤") && curWord.startsWith("¾Ù"))
				return true;
			
			if(lastWord.equals("·Î") && curWord.startsWith("¿À"))
				return true;
			if(lastWord.equals("·Ó") && curWord.startsWith("¿É"))
				return true;
			if(lastWord.equals("·Ï") && curWord.startsWith("¿Á"))
				return true;
			if(lastWord.equals("·Ô") && curWord.startsWith("¿Ê"))
				return true;
			if(lastWord.equals("·Ò") && curWord.startsWith("¿È"))
				return true;
			if(lastWord.equals("·Õ") && curWord.startsWith("¿Ë"))
				return true;
			if(lastWord.equals("·Ð") && curWord.startsWith("¿Â"))
				return true;
			if(lastWord.equals("·Ñ") && curWord.startsWith("¿Ã"))
				return true;
			if(lastWord.equals("·Ú") && curWord.startsWith("¿Ü"))
				return true;
			
			if(lastWord.equals("·ç") && curWord.startsWith("¿ì"))
				return true;
			if(lastWord.equals("·ì") && curWord.startsWith("¿ó"))
				return true;
			if(lastWord.equals("·è") && curWord.startsWith("¿í"))
				return true;
			if(lastWord.equals("·í") && curWord.startsWith("¿ô"))
				return true;
			if(lastWord.equals("·ë") && curWord.startsWith("¿ò"))
				return true;
			if(lastWord.equals("·é") && curWord.startsWith("¿î"))
				return true;
			if(lastWord.equals("·î") && curWord.startsWith("¿õ"))
				return true;
			if(lastWord.equals("·ê") && curWord.startsWith("¿ï"))
				return true;
			
			if(lastWord.equals("¸£") && curWord.startsWith("À¸"))
				return true;
			if(lastWord.equals("¸¨") && curWord.startsWith("À¾"))
				return true;
			if(lastWord.equals("¸¤") && curWord.startsWith("À¹"))
				return true;
			if(lastWord.equals("¸©") && curWord.startsWith("À¿"))
				return true;
			if(lastWord.equals("¸§") && curWord.startsWith("À½"))
				return true;
			if(lastWord.equals("¸¥") && curWord.startsWith("Àº"))
				return true;
			if(lastWord.equals("¸ª") && curWord.startsWith("ÀÀ"))
				return true;
			if(lastWord.equals("¸¦") && curWord.startsWith("À»"))
				return true;
		}
		
		
		
		return false;
	}
	

}
