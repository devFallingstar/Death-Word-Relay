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
		if(!curWord.matches("[��-��]*")){
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
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("�T") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("�H") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("�d") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("�c") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("�U") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
			if(lastWord.equals("��") && curWord.startsWith("��"))
				return true;
		}
		
		
		
		return false;
	}
	

}
