package Loginout;

public class MemberInfo {
	
	private String id;
	private String pwd;
	private String nickname;
	
	public MemberInfo(String id, String pwd, String nickname){
		this.id = id;
		this.pwd = pwd;
		this.nickname = nickname;
	}
	
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public String getpwd(){
		return pwd;
	}
	public void setPwd(String pwd){
		this.pwd = pwd;
	}


	public String getnickName(){
		return nickname;
	}
	public void setnickName(String nickname){
		this.nickname = nickname;
	}
	@Override
	public String toString(){
		return "MemberDTO [id="+id+",pwd="+pwd+",nickname"+nickname+"]";
	}



}
