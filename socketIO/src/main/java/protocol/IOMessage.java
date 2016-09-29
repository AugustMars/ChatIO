package protocol;

import net.sf.json.JSONObject;

public class IOMessage {

	private Header header;
	
	private JSONObject body;
	
	public  Header getHeader() {
		return header;
	}
	
	public  void setHeader(Header header) {
		this.header = header;
	}
	
	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject body) {
		this.body = body;
	}

	@Override
	public String toString(){
		return "IOMessage [header=" + header + "] " 
	         + "[body=" + body.toString() +"]";
	}
}
