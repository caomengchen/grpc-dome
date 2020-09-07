package com.ctd.bank.common.bean.model;

import com.ctd.bank.common.exception.BusinessException;

public class Result {

	// 响应业务状态
	private Integer code;//

	// 响应消息
	private String msg;

	// 响应中的数据
	private Object data;
	
	public static Result build(Integer code, String msg, Object data) {
		return new Result(code, msg, data);
	}

	public static Result ok(Object data) {
		return new Result(data);
	}

	public static Result ok() {
		return new Result(null);
	}

	
	public Result() {

	}

	public static Result build(Integer code, String msg) {
		return new Result(code, msg, null);
	}
	
	public static Result build(Exception e) {
		if(e instanceof BusinessException){
			return Result.build(-1, e.getMessage());
		}else{
			return Result.build(-2, e);
		}
	}
	
	public static Result build(Integer code, Exception e) {
		if(e instanceof NullPointerException){
			return new Result(code, "", null);
		}else{
			String message = e.getMessage();
			return new Result(code, message.length() > 200 ? message.substring(0, 200):message, null);
		}
	}

	public Result(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Result(Object data) {
		this.code = 0;
		this.msg = "OK";
		this.data = data;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}