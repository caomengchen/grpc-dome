package com.ctd.bank.business.util;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.ctd.bank.common.exception.BusinessException;
import sun.misc.BASE64Encoder;

public class KeyUtil {
 
	public static void main(String[] args) throws Exception {
		
		String cerPath = "D:/mypublickey.cer";		//证书文件路径
		String storePath = "D:/mykeystore.keystore";	//证书库文件路径
		
		String AESKey = "NFpRJCumeBZ500Mhlgwn6A==";	
		
		//私钥
		String pri = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCliIQ9fY9qE0MdrqkwnLbeh+dxftr0DvbdCHwfzhmUejwuts6WIZltTe6tu33B0eTWcWghdlCxnRbuVsSHKgfdfHWwCQszLhu6M8FhfHU/VCliwbL+/Jp7s+88aSTORcy4gebaO1yiMfByJ4YxdFflQJSyk8sq+wX8xbrhZxqYWjN7GQyahrc+ssCdonxLdr3pqIfEZcGQYK9xXDzhtuVy1WdMhej5PnaWuVjP1VxGoqV227LaHbeYHS/raofLBRErAxUtaIlMy5AchoNyQ1nKxuAhcSE4hnYb68pGmwdtj3NIB0NEUFfXrBprWHeZkELmxqMs0m36oR5EI6MVxAFHAgMBAAECggEBAIheLMMTe2Bhufrr9snrcONmho6DeRFyeoChCtCQ05X0FTxOsWYRC/p2J1jMis9vgNwA1U4r0FJiHY//vyuyvoQCklmROb7SD1mYZYlQJK8lQGrxM+rIYuPh2gakgAPGkN63dVz3ZlrgVTtVwEFhnT9pspZjFw6zeb1Bj7E9ZTV4UEstUMlgaZ5Ab9FZ58S6SciPi2CPdxBynRMIBQRanMPM6knKOyjal9QNOsOiBqNfmla/+pRQ6dOw/AtWRMP5ubCSPSLTVuVXvyqOJsm8ms9/TXj0+Sfbjbwh9g34mjSI/rc/xmEFzxqKdyfe6H/YL5BzqccY3XC9wGkfa9eUAtECgYEA+adlgeRF4x6lqGQCM8uQAArCDIMsS4viSJETwHzui4RWs8P89yfH81pewc2hmTJLzR2iUCDQ4asCxxdQeqmKz4mvLOBMu12jbiS7ovmCNRhOPZt1aHPfJ5e3fvHRZ12u6d+uPSyZ/6XgkiumK8fJ4ZhCy6NBmGu/rtbda/edUl0CgYEAqb22KM2/PEbFXPN9SOUYwPyWBqX9Rc4gvPnh+M1w/vKOJinSyTv61qggN3hqnwvGVNfB60MYD42OQgph4HkZsK1wLagUPPXxl5i57IaCDunmdKRpssVVPZ5obDgqAKcjdqh4mGMCH9zHpUGmU5oCntS3SZRE7/OsGwWatNUS7/MCgYEAx8YvUSi00BDnKP+WT5I2UgJJ069n07BFTCJjtuWCEby9kK5lj/WSaGbJarY8+RUpWSK8jvgjjmHFIEHE0dM30+ludy+p10kQDYRxbfHRQuGHQn9TDVmjmdArMvcYxWlPHbEzgZpeRRecw/sPxvrhzpeD4+ZpCCo4whUILGyBxLUCgYA05pxEAUONfJz8CCLOuh/J3a7AKbFStRg87UAA8744ps2olTZNYzUc+073Gr9YDCpmvMuCD+oKxtpEOMoiVHuuNY+VmyGbkicWURH45j7YeQtK5QbQIXIcfycaOzLT+0MxMvZKv+ikl4FdPqWgvon8iF0LQHtvsSi9TzTVEOcTAwKBgQC7NOS+yncA2Y1c5j4MxPIbVABCjjmCYIDxiNT4TDCpTkCOqpjX5dsmJzKue4M50R8Dkbgi2+XMpi5RrC5wc+lbfJmWb6+qdiwaBuzOs94r5eNgSt8N4vmN4sWm4zzqHTTvLUvQX2r/7h1+pCQ51cc3XPsFNID4QskHKoPDjjDORQ==";
		//公钥
		String pub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApYiEPX2PahNDHa6pMJy23ofncX7a9A723Qh8H84ZlHo8LrbOliGZbU3urbt9wdHk1nFoIXZQsZ0W7lbEhyoH3Xx1sAkLMy4bujPBYXx1P1QpYsGy/vyae7PvPGkkzkXMuIHm2jtcojHwcieGMXRX5UCUspPLKvsF/MW64WcamFozexkMmoa3PrLAnaJ8S3a96aiHxGXBkGCvcVw84bblctVnTIXo+T52lrlYz9VcRqKldtuy2h23mB0v62qHywURKwMVLWiJTMuQHIaDckNZysbgIXEhOIZ2G+vKRpsHbY9zSAdDRFBX16waa1h3mZBC5sajLNJt+qEeRCOjFcQBRwIDAQAB";
		String alias = "mykey";		//证书别名
		String storePw = "123456";	//证书库密码
		String keyPw = "123456";	//证书密码
 
		System.out.println("从证书获取的公钥为:" + getPublicKey(cerPath));
		System.out.println("从证书获取的私钥为:" + getPrivateKey(storePath, alias, storePw, keyPw));
 
	}
 
	
	private static String getPublicKey(String cerPath) throws Exception {
		CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
		FileInputStream fis = new FileInputStream(cerPath);
		X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(fis);
		PublicKey pk = Cert.getPublicKey();
		String publicKey = new BASE64Encoder().encode(pk.getEncoded());
		return publicKey;
	}
 
	private static String getPrivateKey(String storePath, String alias, String storePw, String keyPw) throws Exception {
		FileInputStream is = new FileInputStream(storePath);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(is, storePw.toCharArray());
		is.close();
		PrivateKey key = (PrivateKey) ks.getKey(alias, keyPw.toCharArray());
		System.out.println("privateKey:" + new BASE64Encoder().encode(key.getEncoded()));
		String privateKey = new BASE64Encoder().encode(key.getEncoded());
		return privateKey;
	}
	
	/**
	 * 全角符号转换半角
	 * @param input
	 * @return
	 */
	public static String QtoB(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 半角转全角
	 * @param input
	 * @return
	 */
	public static String BtoQ(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	
	public static class SerialNumber {

		public static Calendar CA = Calendar.getInstance();

		public static Integer NUM = 1;
		
		public static Integer NUM_ = 1;

		public synchronized static String getId() throws BusinessException {
			Calendar cal = Calendar.getInstance();
			if (!(cal.get(Calendar.YEAR) == CA.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == CA.get(Calendar.MONTH)
					&& cal.get(Calendar.DAY_OF_MONTH) == CA.get(Calendar.DAY_OF_MONTH)
					&& cal.get(Calendar.MINUTE) == CA.get(Calendar.MINUTE)
					&& cal.get(Calendar.SECOND) == CA.get(Calendar.SECOND))) {// 每秒重置到1
				CA = cal;
				NUM = 1;
			} else {
				if (NUM == 1000000) {
					throw new BusinessException("获取流水号失败");
				}
			}
			String id = new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime()) + String.format("%06d", NUM);
			NUM++;
			return id;
		}
		
		public synchronized static String getYSXId() throws BusinessException {
			Calendar cal = Calendar.getInstance();
			if (!(cal.get(Calendar.YEAR) == CA.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == CA.get(Calendar.MONTH)
					&& cal.get(Calendar.DAY_OF_MONTH) == CA.get(Calendar.DAY_OF_MONTH))) {// 每天重置到1
				CA = cal;
				NUM_ = 1;
			} else {
				if (NUM_ == 1000000) {
					throw new BusinessException("获取流水号失败");
				}
			}
			//YSX+渠道编号【0316】+YYYYMMDD+11位序列号
			String id = "YSX" + "0316" + new SimpleDateFormat("yyyyMMdd").format(cal.getTime()) + String.format("%011d", NUM_);
			NUM_++;
			return id;
		}
	}
 
}
