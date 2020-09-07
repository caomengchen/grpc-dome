package com.ctd.bank.business.util;

import com.ctd.bank.common.annotation.LogAspect;
import com.ctd.bank.common.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class JHSocketUtil {
    
    public static String getJhSocketCon(String host,Integer port ,String msg, String name){
        String ret = getJhSocketCon(host, port , msg);
        HttpUtil.insertLog(host+":"+port, msg, ret, name, LogAspect.getLocalValue());
        return ret;
    }
   
	public static String getJhSocketCon(String host,Integer port ,String msg) {
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
		    System.out.println("连接 -----------------------参数["+msg+"]");
	        String msgLength = getCount(msg.getBytes("GB2312").length);
		    System.out.println("报文长度----------------------发送"+msgLength);
		    String totalMsg = msgLength+msg;
		    outputStream.write(totalMsg.getBytes("GB2312"));
		    System.out.println("总报文---------------------发送"+totalMsg);
		    outputStream.flush();
		    
		    inputStream = socket.getInputStream();
		    System.out.println("连接 -----------------------返回");
		    byte[] bytes = new byte[1024];
		    int len;
		    while ((len = inputStream.read(bytes)) != -1) {
		      sb.append(new String(bytes, 0, len,"GB2312"));
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
				return sb.toString();
			} catch (IOException e1) {
				e1.printStackTrace();
				return "关闭出错了";
			}
		}
	    
       }
    //位数不足自动左补全
	public static String getCount(Integer i){
		String s=i.toString();
		int l=s.length();
		if(l<6){
			for(int j=0;j<6-l;j++){
				s="0"+s;
			}
		}else{
			s= s.substring(0, 6);
		}
		return s;

	}
	
	
}
