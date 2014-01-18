package lzw.campus.glass;

public class ChatMsgEntity{
	public String name;
	public String date;
	public String text;
	public boolean isComMsg;
	
	public ChatMsgEntity(){
		this.isComMsg=true;
	}
	
	public ChatMsgEntity(String name,String date,String 
			text,boolean isComMsg){
		super();
		this.name=name;
		this.date=date;
		this.text=text;
		this.isComMsg=isComMsg;
	}
	
	public boolean getMsgType(){
		return isComMsg;
	}
}
