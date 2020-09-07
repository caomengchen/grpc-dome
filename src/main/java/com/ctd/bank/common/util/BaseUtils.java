package com.ctd.bank.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctd.bank.common.exception.BusinessException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** 
 * 常用方法工具类
 * @author zhoujy
 * @date 2017-2-23
 * @version 1.0.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseUtils {


	/**
	 * searchbox参数转换
	 * @param request
	 * @param model
	 * @param searchKey
	 * @param searchValue
	 * @throws Exception
	 */
	public static void searchboxTransfer(HttpServletRequest request, Object model, String searchKey, String searchValue) throws Exception { //返回值为随意的类型   传入参数为request 和该随意类型
		// 根据属性名称，动态匹配set方法
		if (StringUtils.isNotBlank(request.getParameter("searchKey")) && StringUtils.isNotBlank(request.getParameter("searchValue"))) {
			Field field = null;
			try {
				field = model.getClass().getDeclaredField(request.getParameter("searchKey"));
			} catch (NoSuchFieldException e) {
				field = model.getClass().getSuperclass().getDeclaredField(request.getParameter("searchKey"));
			}
			field.setAccessible(true);

			String fieldType = field.getType().toString();

			if (fieldType.equals("class java.lang.String")) {
				field.set(model, request.getParameter("searchValue"));
			} else if (fieldType.equals("class java.lang.Integer")) {
				field.set(model, new Integer(request.getParameter("searchValue")));
			}
		}
	}
	
	public static Object transfer(Object obj) throws Exception{
		Field[] field = obj.getClass().getDeclaredFields();  
		for (int i = 0; i < field.length; i++) {
			String name = field[i].getName();  
            name = name.substring(0, 1).toUpperCase() + name.substring(1);  
            Method get = obj.getClass().getMethod("get" + name);  
            Object value = get.invoke(obj);  
            if(value instanceof String && value != null){
            	Method set = obj.getClass().getMethod("set" + name, String.class);  
            	set.invoke(obj, encodeUTF8((String)value));
            }
		}
		return obj;
	}

	/**
	 * 往配置文件里写单行内容
	 * @param confPath
	 * @param content
	 */
	public static void writeStr2File(String confPath, String content) {
		String absolutePath = Thread.currentThread().getContextClassLoader().getResource("").toString(); //tomcat绝对路径
		absolutePath = (absolutePath.trim() + confPath.trim()).substring(6).trim();
		if (absolutePath.indexOf(":") != 1) {
			absolutePath = File.separator + absolutePath;
		}
		BufferedWriter writer = null;
		OutputStreamWriter write = null;
		try {
			write = new OutputStreamWriter(new FileOutputStream(absolutePath), "UTF-8");
			writer = new BufferedWriter(write);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (write != null) {
				try {
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取配置文件里的单行内容
	 * @param confPath
	 * @return
	 */
	public static String readStrFromFile(String confPath) {
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			String absolutePath = Thread.currentThread().getContextClassLoader().getResource("").toString(); //tomcat绝对路径
			absolutePath = absolutePath.replaceAll("file:/", "");
			absolutePath = absolutePath.replaceAll("%20", " ");
			absolutePath = absolutePath.trim() + confPath.trim();
			if (absolutePath.indexOf(":") != 1) {
				absolutePath = File.separator + absolutePath;
			}
			String encoding = "UTF-8";
			File file = new File(absolutePath);
			if (file.isFile() && file.exists()) { //判断文件是否存在
				read = new InputStreamReader(new FileInputStream(file), encoding); //考虑到编码格式
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
			} else {
				System.err.println("未找到指定文件, 请检查文件路径:" + absolutePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("解析路径或读取文件出错！");
		} finally {
			if (read != null) {
				try {
					read.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 读取并解析配置文件
	 * @param filePath
	 * @return
	 */
	public static Map readProperties(String filePath) {
		Map reslutMap = new HashMap();
		FileInputStream in = null;
		try {
			String absolutePath = Thread.currentThread().getContextClassLoader().getResource("").toString(); //tomcat绝对路径
			absolutePath = absolutePath.replaceAll("file:/", "");
			absolutePath = absolutePath.replaceAll("%20", " ");
			absolutePath = absolutePath.trim() + filePath.trim();
			absolutePath = File.separator + absolutePath.trim(); //linux

			in = new FileInputStream(absolutePath);
			Properties properties = new Properties(); //实例化
			properties.load(in); //从filePath得到键值对

			Enumeration enmObject = properties.keys(); //得到所有的主键信息（这里的主键信息主要是简化的主键，也是信息的关键）

			while (enmObject.hasMoreElements()) { //对每一个主键信息进行检索处理，跟传入的返回值信息是否有相同的地方（如果有相同的地方，取出主键信息的属性，传回给返回信息）
				String curKey = ((String) enmObject.nextElement()).trim();
				String curMessage = new String(properties.getProperty(curKey).getBytes("ISO-8859-1"), "utf-8").trim();
				reslutMap.put(curKey, curMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					System.err.println(e1.getMessage());
				}
			}
		}
		return reslutMap;
	}

	/**
	 * map取出key只保留value转换成list
	 * @param map
	 * @return
	 */
	public static List map2list(Map map) {
		List valueList = new ArrayList();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			valueList.add(map.get(key));
		}
		//System.out.println("Convert Finished !");  
		return valueList;
	}


	/**
	 * URL UTF-8 转码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeUTF8(String str) {
		try {
			if (StringUtils.isBlank(str)) {
				return "";
			}
			if (str.equals(new String(str.getBytes("ISO8859-1"), "ISO8859-1"))) {
				return new String(str.toString().getBytes("ISO8859-1"), "UTF-8").trim();
			} else {
				return str;
			}
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			return "";
		}
	}

	/** 
	 * 生成文件 
	 * @param fileName 
	 * @return 
	 */
	public static boolean createFile(File fileName) throws Exception {
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 文本写入TXT??
	 * @param filePath
	 * @param content
	 */
	public static void content2Txt(String filePath, String content) {
		String str = new String(); //原有txt内容
		String s1 = new String();//内容更新
		BufferedWriter output = null;
		BufferedReader input = null;
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				f.createNewFile();// 不存在则创建
			}
			input = new BufferedReader(new FileReader(f));

			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			input.close();
			s1 += content;

			output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e1) {
					//e1.printStackTrace();
					System.err.println(e1.getMessage());
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	/** 
	 * 读TXT文件内容 
	 * @param filePath
	 * @return 
	 * @return 
	 */
	public static String readTxtFile(String filePath) {
		String result = "";
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { //判断文件是否存在
				read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					result = lineTxt;
				}
			} else {
				System.err.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.err.println("读取文件内容出错");
			e.printStackTrace();
		} finally {
			if (read != null) {
				try {
					read.close();
				} catch (IOException e1) {
					//e1.printStackTrace();
					System.err.println(e1.getMessage());
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * 删除文件
	 * @param path
	 * @throws Exception
	 */
	public static void deleteFile(String path) {
		if (StringUtils.isNotBlank(path)) {
			File file = new File(path);
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * 删除目录下所有文件及文件夹
	 * @param path
	 */
	public static void deleteDirectory(String path) {
		if (StringUtils.isNotBlank(path)) {
			File file = new File(path);
			//1級文件刪除
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				//2級文件列表
				String[] filelist = file.list();
				//获取新的二級路徑
				for (int j = 0; j < filelist.length; j++) {
					File filessFile = new File(path + "\\" + filelist[j]);
					if (!filessFile.isDirectory()) {
						filessFile.delete();
					} else if (filessFile.isDirectory()) {
						//递归調用
						deleteDirectory(path + "\\" + filelist[j]);
					}
				}
				file.delete();
			}
		}
	}

	/**
	 * XML转Map
	 * @param fileName
	 * @param node 节点内容
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getXml2Map(String fileName, String node) {
		Map<String, Object> nodeMap = new HashMap<String, Object>();
		try {
			// 读取报文通用配置文件
			XMLSerializer serializer = new XMLSerializer();
			serializer.setSkipNamespaces(true); //跳过命名空间

			String templatePath = Thread.currentThread().getContextClassLoader().getResource("").toString().replaceAll("file:/", "").replaceAll("%20", " ") + "template" + File.separator; //模板文件绝对路径
			String xmlPath = templatePath.trim() + fileName + ".xml";

			String xmlString = readXml(xmlPath);
			Map<String, Object> map = JSON.parseObject(serializer.read(xmlString).toString(), new TypeReference<Map<String, Object>>() {
			});
			nodeMap = (Map<String, Object>) map.get(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeMap;

	}
	
	//map转xml
    public static String map2Xmlstring(Map<String,Object> map){  
        StringBuffer sb = new StringBuffer(""); 
          
        Set<String> set = map.keySet();  
        for(Iterator<String> it=set.iterator(); it.hasNext();){  
            String key = it.next();  
            Object value = map.get(key);  
            sb.append("<").append(key).append(">");  
            sb.append(value);  
            sb.append("</").append(key).append(">");  
        }  
        return sb.toString();  
    }  
    
    /**  
     * Xml string转换成Map  
     * @param xmlStr  
     * @return  
     */  
    public static Map<String,Object> xmlString2Map(String xmlStr){  
        Map<String,Object> map = new HashMap<String,Object>();  
        Document doc;  
        try {  
            doc = DocumentHelper.parseText(xmlStr);  
            Element el = doc.getRootElement();  
            map = recGetXmlElementValue(el,map);  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        return map;  
    }
    
    /**  
     * 循环解析xml  
     * @param ele  
     * @param map  
     * @return  
     */  
    private static Map<String, Object> recGetXmlElementValue(Element ele, Map<String, Object> map){  
        List<Element> eleList = ele.elements();  
        if (eleList.size() == 0) {  
            map.put(ele.getName(), ele.getTextTrim());  
            return map;  
        } else {  
            for (Iterator<Element> iter = eleList.iterator(); iter.hasNext();) {  
                Element innerEle = iter.next();  
                recGetXmlElementValue(innerEle, map);  
            }  
            return map;  
        }  
    } 

	/**
	 * 指定路径读取xml文件
	 * @param xmlPath
	 * @return
	 */
	public static String readXml(String xmlPath) {
		String sReturn = ""; // 输出字符串
		try {
			SAXBuilder reader = new SAXBuilder();
			org.jdom.Document document = null;
			document = reader.build(xmlPath);
			Format format = Format.getPrettyFormat();
			format.setEncoding("UTF-8");// 设置编码格式

			StringWriter out = null; // 输出对象

			XMLOutputter outputter = new XMLOutputter();
			out = new StringWriter();
			outputter.output(document, out);
			sReturn = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sReturn;
	}


	
	public static Map<String, String> getMapFromReq(HttpServletRequest request, String[] field, String[] valField) throws Exception{
		Map map = new HashMap<>();
		if(valField == null){
			valField = new String[1];
		}
		List<String> asList = Arrays.asList(valField);
		for (int i = 0; i < field.length; i++) {
			String parameter = request.getParameter(field[i]);
			if(asList != null && asList.contains(field[i]) && StringUtils.isNotBlank(parameter)){
				map.put(field[i], parameter);
			}else{
				throw new BusinessException("参数[" + field[i] + "]不可为空.");
			}
		}
		return map;
	}


	/**
	 *计算投资占比
	 * @param money  投资额
	 * @param tolmoney 投资总额
	 * @param formatType 小数保留位数 .00
	 * @param fortol 计算后小数*多少
	 * @return
	 */
	public static String money4total(String money, String tolmoney,String formatType,Double fortol){
		Double dtolmoney = Double.parseDouble(tolmoney);
		Double dmoney = Double.parseDouble(money);
		Double dpre = dmoney/dtolmoney*fortol;
		DecimalFormat df = new DecimalFormat(formatType);
		return df.format(dpre);
	}

	/**
	 * 金额格式化
	 * @param money
	 * @param formatType
	 */
	public static String moneyFormat(String money,String formatType){
		Double dtolmoney = Double.parseDouble(money);
		DecimalFormat df = new DecimalFormat(formatType);
		return df.format(dtolmoney);
	}


	/**
	 * 数字转换为汉语中人民币的大写<br>
	 * 
	 * @author hongten
	 * @contact hongtenzone@foxmail.com
	 * @create 2013-08-13
	 */
	public static class NumberToCN {
	    /**
	     * 汉语中数字大写
	     */
	    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
	            "伍", "陆", "柒", "捌", "玖" };
	    /**
	     * 汉语中货币单位大写，这样的设计类似于占位符
	     */
	    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
	            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
	            "佰", "仟" };
	    /**
	     * 特殊字符：整
	     */
	    private static final String CN_FULL = "整";
	    /**
	     * 特殊字符：负
	     */
	    private static final String CN_NEGATIVE = "负";
	    /**
	     * 金额的精度，默认值为2
	     */
	    private static final int MONEY_PRECISION = 2;
	    /**
	     * 特殊字符：零元整
	     */
	    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
	    
	    
	    public static String number2CNMontrayUnit(String number){
	    	return number2CNMontrayUnit(new BigDecimal(number));
	    }

	    /**
	     * 把输入的金额转换为汉语中人民币的大写
	     * 
	     * @param numberOfMoney
	     *            输入的金额
	     * @return 对应的汉语大写
	     */
	    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
	        StringBuffer sb = new StringBuffer();
	        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
	        // positive.
	        int signum = numberOfMoney.signum();
	        // 零元整的情况
	        if (signum == 0) {
	            return CN_ZEOR_FULL;
	        }
	        //这里会进行金额的四舍五入
	        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
	                .setScale(0, 4).abs().longValue();
	        // 得到小数点后两位值
	        long scale = number % 100;
	        int numUnit = 0;
	        int numIndex = 0;
	        boolean getZero = false;
	        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
	        if (!(scale > 0)) {
	            numIndex = 2;
	            number = number / 100;
	            getZero = true;
	        }
	        if ((scale > 0) && (!(scale % 10 > 0))) {
	            numIndex = 1;
	            number = number / 10;
	            getZero = true;
	        }
	        int zeroSize = 0;
	        while (true) {
	            if (number <= 0) {
	                break;
	            }
	            // 每次获取到最后一个数
	            numUnit = (int) (number % 10);
	            if (numUnit > 0) {
	                if ((numIndex == 9) && (zeroSize >= 3)) {
	                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
	                }
	                if ((numIndex == 13) && (zeroSize >= 3)) {
	                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
	                }
	                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
	                getZero = false;
	                zeroSize = 0;
	            } else {
	                ++zeroSize;
	                if (!(getZero)) {
	                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
	                }
	                if (numIndex == 2) {
	                    if (number > 0) {
	                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                    }
	                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
	                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
	                }
	                getZero = true;
	            }
	            // 让number每次都去掉最后一个数
	            number = number / 10;
	            ++numIndex;
	        }
	        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
	        if (signum == -1) {
	            sb.insert(0, CN_NEGATIVE);
	        }
	        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
	        if (!(scale > 0)) {
	            sb.append(CN_FULL);
	        }
	        return sb.toString();
	    }

	}

	public static byte[] getNormalQRCode(String content, Integer QRCodeWidth, Integer QRCodeHeight) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);

        //调用去除白边方法
        bitMatrix = deleteWhite(bitMatrix);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        ByteArrayOutputStream out =new ByteArrayOutputStream();
        ImageIO.write(image,"png",out);//png 为要保存的图片格式
        return out.toByteArray();
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }


	/**
	 * 时间格式化
	 * @param format
	 * @param changeValue
	 * @return
	 */
    public static String getTimeFormat(String format, String changeValue){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		String time = sdf.format(cal.getTime());
		if (StringUtils.isNotBlank(changeValue)) {
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR) + Integer.parseInt(changeValue));
			time = sdf.format(cal.getTime());
		}
		return time;
	}

	public static String stringDateFormat(String arg, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		SimpleDateFormat sdf1 = new SimpleDateFormat(format);
		String res = "";
		try {
			res = sdf1.format(sdf.parse(arg));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
}
