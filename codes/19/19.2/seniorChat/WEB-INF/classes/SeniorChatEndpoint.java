package org.fkjava.web;

import java.io.*;
import java.nio.*;
import java.util.*;
import javax.websocket.*;
import javax.websocket.server.*;
/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2018, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
// @ServerEndpoint注解修饰的类将作为WebSocket的服务端
@ServerEndpoint(value="/seniorChat")
public class SeniorChatEndpoint
{
	static List<Session> clients = Collections
		.synchronizedList(new ArrayList<Session>());
	@OnOpen // 该注解修饰的方法将会客户端连接时被激发
	public void start(Session session)
	{
		// 每当有客户连接进来时，收集该客户对应的session
		clients.add(session);
	}
	@OnMessage // 该注解修饰的方法将会客户端消息到达时被激发
	public void message(String message, Session session)
		throws Exception
	{
		// 收到消息后，将消息向所有客户发送一次
		for (Session s : clients)
		{
			RemoteEndpoint.Basic remote = s.getBasicRemote();
			remote.sendText(message);
		}
	}

	@OnMessage // 该注解修饰的方法将会客户端消息到达时被激发
	public void message(ByteBuffer data, boolean last, Session session)
		throws Exception
	{
		// 收到消息后，将消息向所有客户发送一次
		for (Session s : clients)
		{
			RemoteEndpoint.Basic remote = s.getBasicRemote();
			remote.sendBinary(data, last);
		}
	}

	@OnClose // 该注解修饰的方法将会客户端连接关闭时被激发
	public void end(Session session, CloseReason closeReason)
	{
		// 每当有客户连接关闭时，删除该客户对应的session
		clients.remove(session);
	}

	@OnError // 该注解修饰的方法将会客户端出错时被激发
	public void error(Session session, Throwable throwable)
	{
		// 每当有客户连接出错时，删除该客户对应的session
		clients.remove(session);
	}
}
