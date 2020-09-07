package com.ctd.bank.common.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WebServiceCXFFunction {
	
	public void main1(String wsdlUrl, String methodName, String inputXml) throws Exception{
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsdlUrl);
		HTTPConduit http = (HTTPConduit) client.getConduit();
		HTTPClientPolicy hcp = new HTTPClientPolicy();
		
		//超时设置
		hcp.setConnectionTimeout(2000);
		hcp.setReceiveTimeout(200000);
		http.setClient(hcp);
		
		/*代理设置
		hcp.setProxyServer("192.168.12.2");
		hcp.setProxyServerPort(808);*/
		
		/*String userid="test";//账号
		String password="1";//密码
		String jybh = "testsql001";//交易编号
		//添加拦截器，在拦截器中加入账号密码
		client.getOutInterceptors().add(new AddSoapHeader("http://yinhai.com",userid,password,"","",jybh));*/
		
		//XML格式参数字符串
		Object[] obj= client.invoke(methodName, inputXml);
		System.out.println(obj[0].toString());
	}
	
	//public static String url = "http://59.37.20.97/PdfFormSystem/services/pdfService?wsdl";
	public final static String url = "http://10.1.1.142:8082/businessLicenceService/services/AppService?wsdl";

	public static void main(String[] args)  throws Exception{
		Client client = createClient(url);	
		Object[] obj2;
		String xml = "<fileType></fileType><xmlData></xmlData>";
		
		String  BusinessFlowNumber="44190021900000038";
		//个体户
		String infoXML = "<?xml version='1.0' encoding='utf-8'?><licence><firm><type>8</type><operType>1</operType><licenceType>0</licenceType>"
				       +"<attribute904>"+BusinessFlowNumber+"</attribute904>"			//工商业务流水号，此字段和attribute900不能同时使用；只能有一个有效
				       +"<attribute905>1</attribute905>"								//电子营业执照申请数量；与attribute904同时使用有效
				       +"<attribute500>广东省</attribute500>"								//注册信息所属省名称
				       +"<attribute501>东莞市</attribute501>"                             //注册信息所属市名称
				       +"<attribute504>441900</attribute504>"                           //登记机关代码 
				       +"<attribute13>91441900MA4UMTFH60</attribute13>"       //统一社会信用代码
				       +"<attribute16>360730199011150659</attribute16>"     //法人身份证号码
				       +"<attribute18>东莞市茂荣五金有限公司</attribute18>"   //公司名称
				       +"<attribute19>个体工商</attribute19>"                              //执照类型，根据执照类型文字提供
				       +"<attribute23>曾艳梅</attribute23>" //法人姓名
				       +"<attribute28>个人经营</attribute28>"								//组成形式
				       +"<attribute31>东莞市长安镇锦厦S358省道390号长安商贸城五金广场E3幢壹层27号</attribute31>"					//经营场所
				       +"<attribute33>2016年03月23日</attribute33>"					//成立日期
				       +"<attribute34>2016年03月23日</attribute34>"					//开业日期
				       +"<attribute39>批发、零售：五金制品、塑胶制品、五金刀具、五金配件、电线电缆、家用电器、仪器仪表、橡塑制品</attribute39>"					//经营范围
				       +"<attribute42>东莞市市场监督管理局</attribute42>"						//登记机关
				       +"<attribute43>2016年03月23日</attribute43>"					//登记日期
				       +"<attribute52>18971037141</attribute52>"					//领照人电话号码（法人电话号码）
				       +"</firm>"
				       +"</licence>";
	String infoX = "<?xml version='1.0' encoding='utf-8'?><licence><firm><type>1</type><operType>1</operType><licenceType>0</licenceType><attribute904>44190021927164528</attribute904><attribute905>1</attribute905><attribute909></attribute909><attribute910></attribute910><attribute500>广东省</attribute500><attribute501>东莞市</attribute501><attribute504>441900</attribute504><attribute13>91441900MA4UMKWH9K</attribute13><attribute16>420104198201080875</attribute16><attribute18>东莞市虎门商事测试伍尔巴科技发展有限责任公司</attribute18><attribute19>有限责任公司</attribute19><attribute20>马欣</attribute20><attribute25>壹拾万元整</attribute25><attribute29>广东省东莞市厚街镇大迳岗头七巷18号</attribute29><attribute33>2019年05月28日</attribute33><attribute35>长期 </attribute35><attribute38>长期</attribute38><attribute39>科学信息咨询（依法须经批准的项目，经相关部门批准后方可开展经营活动。）</attribute39><attribute41>[B@e8ad96d</attribute41><attribute42>东莞市市场监督管理局</attribute42><attribute43>2019年05月28日</attribute43><attribute44>http://www.gsxt.gov.cn</attribute44><attribute52>17771781788</attribute52><attribute56>420104198201080875</attribute56></firm></licence>";
	

		Object[] parameters = new Object[] { infoX, "SAIC_441900_03" };
		obj2 = client.invoke("businessLicenceRegister", parameters);
		
		for (Object object : obj2) {
			System.out.println(object);
		}
		
	}
	
	
	public static Client createClient(String url) {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(url);
		HTTPConduit http = (HTTPConduit) client.getConduit();
		HTTPClientPolicy hcp = new HTTPClientPolicy();
		hcp.setConnectionTimeout(20000);// 超时设置
		hcp.setReceiveTimeout(200000);
		http.setClient(hcp);
		return client;
	}
	public static void main1(String[] args) {
		String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		 System.out.println(yearLast);
	}
}
