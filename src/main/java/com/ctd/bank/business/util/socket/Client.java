package com.ctd.bank.business.util.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import com.ctd.bank.business.util.SocketParamsUtil;
import com.ctd.bank.common.annotation.LogAspect;
import com.ctd.bank.common.util.HttpUtil;
import com.ctd.bank.config.BankConfig;



public class Client {

	public static String getSocketCon(String msg,String name, BankConfig bankConfig){
		String ret = getSocketCon(msg, bankConfig);
		HttpUtil.insertLog(bankConfig.getZhHsot()+":"+ bankConfig.getZhPort(), msg, ret, name, LogAspect.getLocalValue());
		return ret;
	}

	public static String getSocketCon(BankConfig bankConfig) {
		String host = bankConfig.getZhHsot();
		Integer port = Integer.parseInt(bankConfig.getZhPort());
		String str_zh = bankConfig.getZhStr();
		Socket socket = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();
		 // 与服务端建立连接
		try{
		    System.out.println("连接 -----------------------开始"+host+"   "+port);
		    socket = new Socket(host, port);
		    System.out.println("连接 -----------------------成功");
		    
		    outputStream = socket.getOutputStream();
		    str_zh = SocketParamsUtil.chinaBank(new HashMap());
		    System.out.println("连接 -----------------------参数["+str_zh+"]");
		    byte[] zh_byte = str_zh.getBytes("GBK");
		    System.out.println("连接 -----------------------参数["+zh_byte.length+"]");
		    outputStream.write(zh_byte);
		    outputStream.flush();
		    
		    inputStream = socket.getInputStream();
		    System.out.println("连接 -----------------------返回");
		    byte[] bytes = new byte[1024];
		    int len;
		    while ((len = inputStream.read(bytes)) != -1) {
		      sb.append(new String(bytes, 0, len,"GBK"));
		    }
		    System.out.println("连接 -----------------------返回数据[" + sb.toString()+"]");
		    
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			try {
				if(inputStream != null){
					 System.out.println("连接 -----------------------关闭输入流");
					 inputStream.close();
				}
				if(outputStream != null){
					 System.out.println("连接 -----------------------关闭输出流");
					 outputStream.close();	
				}
				if(socket != null){
					 System.out.println("连接 -----------------------关闭客户端");
					 socket.close();	
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				return "关闭出错了";
			}finally{
				return sb.toString();
			}
		}
	    
	}

	public static String getSocketCon(String msg, BankConfig bankConfig) {
		String host = bankConfig.getZhHsot();
		Integer port = Integer.parseInt(bankConfig.getZhPort());
		Socket socket = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();
		 // 与服务端建立连接
		try{
		    System.out.println("连接 -----------------------开始"+host+"   "+port);
		    
		    socket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, 10000);//10秒连接超时
            socket.setSoTimeout(10000);

		    System.out.println("连接 -----------------------成功");
		    
		    outputStream = socket.getOutputStream();
		    System.out.println("连接 -----------------------参数["+msg+"]");
		    
		    outputStream.write(msg.getBytes("GBK"));
		    outputStream.flush();
		    
		    inputStream = socket.getInputStream();
		    System.out.println("连接 -----------------------返回");
		    byte[] bytes = new byte[1024];
		    int len;
		    while ((len = inputStream.read(bytes)) != -1) {
		      sb.append(new String(bytes, 0, len,"GBK"));
		    }
		    System.out.println("连接 -----------------------返回数据[" + sb.toString()+"]");
		    
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			try {
				if(inputStream != null){
					 System.out.println("连接 -----------------------关闭输入流");
					 inputStream.close();
				}
				if(outputStream != null){
					 System.out.println("连接 -----------------------关闭输出流");
					 outputStream.close();	
				}
				if(socket != null){
					 System.out.println("连接 -----------------------关闭客户端");
					 socket.close();	
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
				return "关闭出错了";
			}finally{
				return sb.toString();
			}
		}
	    
	}

}
