package com.ctd.bank.business.util.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * socket服务类
 */
public class Server {

	public static void main(String[] args) throws Exception {
		socketServer();
	  }
	
	public static void localSocket() throws IOException{
	    // 监听指定的端口
	    int port = 8903;
	    ServerSocket server = new ServerSocket(port);
	    
	    // server将一直等待连接的到来
	    System.out.println("server将一直等待连接的到来");
	    Socket socket = server.accept();
	    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
	    InputStream inputStream = socket.getInputStream();
	    byte[] bytes = new byte[1024];
	    int len;
	    StringBuilder sb = new StringBuilder();
	    //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
	    while ((len = inputStream.read(bytes)) != -1) {
	      // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
	      sb.append(new String(bytes, 0, len, "UTF-8"));
	    }
	    System.out.println("get message from client: " + sb);

	    OutputStream outputStream = socket.getOutputStream();
	    outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

	    inputStream.close();
	    outputStream.close();
	    socket.close();
	    server.close();
	}
	
	public static void startSocketServer(){
	    // 监听指定的端口
	    ServerSocket server;
		try {
			server = new ServerSocket(9001);

		    // server将一直等待连接的到来
		    System.out.println("server将一直等待连接的到来");
		    Socket socket = server.accept();
		    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
		    InputStream inputStream = socket.getInputStream();
		    byte[] bytes = new byte[1024];
		    int len;
		    StringBuilder sb = new StringBuilder();
		    //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
		    while ((len = inputStream.read(bytes)) != -1) {
		      // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
		      sb.append(new String(bytes, 0, len, "UTF-8"));
		    }
		    System.out.println("get message from client: " + sb);

		    OutputStream outputStream = socket.getOutputStream();
		    outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

		    inputStream.close();
		    outputStream.close();
		    socket.close();
		    server.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	public static void socketServer() throws IOException{
	    ServerSocket server = new ServerSocket(9001);
	    DataOutputStream dos = null;
	    DataInputStream dis = null;
	    
	    // server将一直等待连接的到来
	    System.out.println("server将一直等待连接的到来");
	    Socket socket = server.accept();
	    dis = new DataInputStream(socket.getInputStream());
	    byte[] in = socketRead(dis);
	    String msg = new String(in,"GBK");
	    System.out.println("结果打印["+msg+"]");
	    
	    
	    socket.close();
	    server.close();
	}
	
	public static byte[] socketRead(DataInputStream in){
		int iRecvLen = 0;
		int iRecvBufNum = 0;
		int iReadNum = 0;
		byte abRecv[] = new byte[1024];
		byte abRecvBuf[] = new byte[1024*20];
		byte abRecvTemp[] = new byte[1024];
		try {
			abRecv = new byte[1024];
			iRecvLen = in.read(abRecv);
			if(iRecvLen>0){
				System.out.println("开始1024"+new String(abRecv,"GBK"));
				iReadNum +=iRecvLen;
			}
			if(iRecvLen<0){
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		while (iRecvLen>0){
			for(int i =0;i<iRecvLen;i++){
				abRecvBuf[i+iRecvBufNum]=abRecv[i];
			}
			iRecvBufNum +=iRecvLen;
			
			try {
				String msg = new String(abRecvBuf,"GBK");
				System.out.println("GBK后打印"+msg);
				iRecvLen = 0;
				abRecv = new byte[in.available()];
				iRecvLen = in.read(abRecv);
				if(iRecvLen>0){
					iReadNum +=iRecvLen;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		}
		if(iReadNum>0){
			abRecvTemp = new byte[iReadNum];
			for(int i=0;i<iReadNum;i++){
				abRecvTemp[i] = abRecvBuf[i];
			}
			return abRecvTemp;
		}else{
			return null;
		}
		
	}
}	
