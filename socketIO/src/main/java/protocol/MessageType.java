package protocol;

public enum MessageType {

    //SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1),
    //ONE_WAY((byte) 2), 
    
    //注册
    LOGIN_REQ((byte) 1), LOGIN_RESP((byte) 2),
    //请求所有信息
    ALL_REQ((byte) 3), ALL_RESP((byte) 4),
	//心跳
    HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6),
    //聊天
    CHAT_REQ((byte) 7), CHAT_RESP((byte) 8),
    //发送失败，del
	DEL_REQ((byte) 9);
	
    private byte value;

    private MessageType(byte value) {
	this.value = value;
    }

    public byte value() {
	return this.value;
    }
}
