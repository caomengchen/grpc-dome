package com.ctd.bank.business.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.ctd.bank.business.model.ApplyInfoModel;
import com.ctd.bank.business.model.PeopleDutyModel;
import org.apache.commons.lang3.StringUtils;
public class SocketParamsUtil {


    //中行发号 007120|
    public static String chinaBankFh(Map map) throws Exception{
        //String str = headParam(map.get("jybh").toString());

        StringBuilder sb = new StringBuilder();

        //CSP报文头
        sb.append(map.get("jybh").toString());
        sb.append(getParm("1403",4));
        sb.append(getParm("4419",10));
        sb.append(getParm("GSZYT000",8));
        sb.append(getParm("E020840001",10));//a99907
        sb.append(getParm("MC",2));
        //String timestamp = Client.getSocketCon(SocketParamsUtil.chinaBankUUID());
        String timestamp = System.currentTimeMillis()+"";
        timestamp = timestamp.substring(1,timestamp.length());

        sb.append(getParm(map.get("wwlsh").toString(),12));
        sb.append(getParm("",88));
        sb.append(getParm("01268",5));

        //MCIS报文头
        sb.append(getParm("0",1));//报文类型
        sb.append(getParm("0",1));//同异步标志
        sb.append(getParm("E020840001",10));//交易码
        sb.append(getParm("01",2));//路由类型
        sb.append(getParm("0440",4));//路由字符串
        sb.append(getParm("",16));//16位空格
        sb.append(getParm("53",2));//渠道标识
        sb.append(getCurrentDate("yyyyMMdd"));//交易日期
        sb.append(getCurrentDate("HHmmss"));//交易时间
        sb.append(getParm("53",2));//前端流水号前2位
        sb.append(getParm("44"+timestamp,14));//前端流水号后14位
        sb.append(getParm("",30));//30位空格
        sb.append(getParm("",8));//后台返回流水号
        sb.append(getParm("",7));//后台返回码
        sb.append(getParm("",7));//柜员
        sb.append(getParm("",13));//FILLER
        sb.append(getParm("01132",5));//报文体长度

        sb.append(getParm("000000",6));//柜员使用的终端号码
        sb.append(getParm("33516",5));//分行号/即机构号00020  0715 确定的   正式环境用省行机构号  33524      测试机构号  33516
        sb.append(getParm("001",3));//工作站号码
        sb.append(getParm("9880800",7));//柜员号码
        sb.append(getParm("1",1));//柜员号码
        sb.append(getParm("X",1));//对账标识
        sb.append(getParm("4315187",7));//授权主管柜员号码
        sb.append(getParm("",120));//对账标识

        //CSP报文体
        sb.append(getParm(map.get("zh").toString(),17));//账号位"00000000000000000" getParm(" 0000074194792239",17)  0000074194792239
        sb.append(getParm("",6));
        sb.append(getParm(map.get("gnm").toString(),1));//"3"  功能码
        sb.append(getParm("",958));
        return sb.toString();
    }
    //中行对公账号开户 AP1001|
    public static String chinaBank(Map map) throws Exception{
        StringBuilder sb = new StringBuilder();
        //CSP报文头
        sb.append("AP1001|");
        sb.append(getParm("9912",4));
        sb.append(getParm("4405",10));
        sb.append(getParm("GSZYT000",8));
        sb.append(getParm("a99907",10));
        sb.append(getParm("MC",2));
        String timestamp = System.currentTimeMillis()+"";
        sb.append(getParm(timestamp.substring(1,timestamp.length()),12));
        sb.append(getParm("",88));
        sb.append(getParm("9778",4));

        //MCIS报文头
        sb.append(getParm("0",1));//报文类型
        sb.append(getParm("0",1));//同异步标志
        sb.append(getParm("E131000001",10));//交易码
        sb.append(getParm("01",2));//路由类型
        sb.append(getParm("0440",4));//路由字符串
        sb.append(getParm("",16));//16位空格
        sb.append(getParm("53",2));//渠道标识
        sb.append(getCurrentDate("yyyyMMdd"));//交易日期
        sb.append(getCurrentDate("HHmmss"));//交易时间
        sb.append(getParm("",2));//前端流水号前2位
        sb.append(getParm("",14));//前端流水号后14位
        sb.append(getParm("",30));//30位空格
        sb.append(getParm("",8));//后台返回流水号
        sb.append(getParm("",7));//后台返回码
        sb.append(getParm("",7));//柜员
        sb.append(getParm("",13));//FILLER
        sb.append(getParm("09642",5));//报文体长度

        //公共报文头
        sb.append(getParm("MCA001",6));//交易码
        sb.append(getParm("2",1));//表单类型
        sb.append(getParm("01",2));//证件类型
        sb.append(getParm("429006199312295131",32));//证件号码
        sb.append(getParm("黄宇",72));//客户姓名
        sb.append(getParm("13517240963",22));//手机号
        sb.append(getParm("",30));//电子邮件
        sb.append(getParm("15",2));//渠道标识
        sb.append(getParm("AP1001",6));//业务类型
        sb.append(getParm("",30));//个人核心客户号/对公账号
        sb.append(getParm("",16));//业务申请表编号
        sb.append(getParm("0",1));//是否代办
        sb.append(getParm("0",1));//是否指定预约办理机构
        sb.append(getParm("",5));//省行机构号
        sb.append(getParm("",5));//机构号
        sb.append(getParm("",32));//前端系统流水号
        sb.append(getParm("09374",5));//申请表数据长度

        //AP1001 对公客户综合开户
        sb.append(getParm("1",1));//开立账户种类
        sb.append(getParm("",1));//专用账户资金性质
        sb.append(getParm("",8));//临时账户有效期（到期日）
        sb.append(getParm("",18));//申请现金库存限额--------------
        sb.append(getParm("13265",5));//拟开户银行机构号-
        sb.append(getParm("58",2));//基本证明文件类型-
        sb.append(getParm("914419005517143412",32));//基本证明文件号码-
        sb.append(getParm("广东东莞侧视科技有限公司",72));//单位名称-
        sb.append(getParm("",120));//单位外文名称
        sb.append(getParm("",40));//单位简称
        sb.append(getParm("",40));//单位外文简称
        sb.append(getParm("黄江镇宝灵三街12号301",120));//单位地址1-   可能是118
        sb.append(getParm("东莞市",20));//单位地址2-
        sb.append(getParm("广东省",20));//单位地址3-
        sb.append(getParm("中国",20));//单位地址4-
        sb.append(getParm("",6));//单位地址1地址代码
        sb.append(getParm("",6));//单位地址2地址代码
        sb.append(getParm("",6));//单位地址3地址代码
        sb.append(getParm("",2));//单位地址4地址代码
        sb.append(getParm("523000",8));//单位电话国家/地区码-
        sb.append(getParm("",10));//单位电话地区码
        sb.append(getParm("0757",10));//单位电话地区码-
        sb.append(getParm("13517240963",22));//单位电话号码
        sb.append(getParm("黄江镇宝灵三街12号301",120));//注册地址1-   可能是118
        sb.append(getParm("东莞市",20));//注册地址2-
        sb.append(getParm("广东省",20));//注册地址3-
        sb.append(getParm("中国",20));//注册地址4-
        sb.append(getParm("",6));//注册地址1地址代码
        sb.append(getParm("",6));//注册地址2地址代码
        sb.append(getParm("",6));//注册地址3地址代码
        sb.append(getParm("",2));//注册地址4地址代码
        sb.append(getParm("523000",8));//注册地邮编-
        sb.append(getParm("0",1));//是否上市公司-
        sb.append(getParm("212",3));//存款人类别-
        sb.append(getParm("I",1));//行业分类-
        sb.append(getParm("H",1));//企业类型-
        sb.append(getParm("CNY",3));//注册币种-
        sb.append(getParm("",18));//注册金额-
        sb.append(getParm("科技",100));//经营范围-
        sb.append(getParm("",30));//其他业务
        sb.append(getParm("523000",6));//地区代码-
        sb.append(getParm("20190410",8));//成立日期-
        sb.append(getParm("20991231",8));//证件到期日-
        sb.append(getParm("",10));//传真国家/地区码
        sb.append(getParm("",10));//传真地区码
        sb.append(getParm("",22));//传真号码
        sb.append(getParm("",30));//邮箱
        sb.append(getParm("M0000",5));//行业代码-

        //其他证明文件
        sb.append(getParm("",2));//证明文件种类
        sb.append(getParm("",32));//证明文件编号
        sb.append(getParm("",8));//证明文件到期日
        //金融许可证
        sb.append(getParm("",32));//金融许可证或支付许可证编号
        sb.append(getParm("",8));//证件到期日
        //组织机构代码证
        sb.append(getParm("",32));//组织机构代码证-
        sb.append(getParm("",8));//组织机构代码证到期日-
        //税务登记证信息
        sb.append(getParm("",32));//国税登记证号码-
        sb.append(getParm("",8));//国税登记证号码到期日
        sb.append(getParm("",32));//地税登记证号码-
        sb.append(getParm("",8));//地税登记证号码到期日
        //法定代表人/单位负责人
        sb.append(getParm("0",1));//法人/单位负责人-
        sb.append(getParm("黄",40));//法定代表人/单位负责人姓-
        sb.append(getParm("宇",40));//法定代表人/单位负责人名-
        sb.append(getParm("",35));//法定代表人/单位负责人拼音姓
        sb.append(getParm("",35));//法定代表人/单位负责人拼音名
        sb.append(getParm("01",2));//法定代表人/单位负责人证件种类-
        sb.append(getParm("429006199312295131",32));//法定代表人/单位负责人证件号码-
        sb.append(getParm("20220202",8));//法定代表人/单位负责人证件到期日-
        sb.append(getParm("19931229",8));//法定代表人/单位负责人出生日期-
        sb.append(getParm("CN",2));//法定代表人/单位负责人国家/地区-

        //财务负责人信息
        sb.append(getParm("黄",40));//财务负责人姓-
        sb.append(getParm("旭",40));//财务负责人名-
        sb.append(getParm("",35));//财务负责人拼音姓
        sb.append(getParm("",35));//财务负责人拼音名
        sb.append(getParm("01",2));//财务负责人证件种类-
        sb.append(getParm("420114199007252538",32));//财务负责人证件号码-
        sb.append(getParm("20380106",8));//财务负责人证件到期日-
        sb.append(getParm("19900725",8));//财务负责人出生日期-
        sb.append(getParm("CN",2));//财务负责人国家/地区-
        sb.append(getParm("",22));//财务负责人固定电话
        sb.append(getParm("13517240963",22));//财务负责人手机-
        //控股股东信息
        sb.append(getParm("黄宇",72));//控股股东姓 名-
        sb.append(getParm("01",2));//控股股东证件种类-
        sb.append(getParm("429006199312295131",32));//控股股东证件号码-
        sb.append(getParm("20220202",8));//控股股东证件到期日-
        //实际控制人信息
        sb.append(getParm("",72));//实际控制人姓 名
        sb.append(getParm("",2));//实际控制人证件种类
        sb.append(getParm("",32));//实际控制人证件号码
        sb.append(getParm("",8));//实际控制人证件到期日

        //关联企业登记信息
        sb.append(getParm("000",3));//实际控制人证件到期日-
        //以下为循环体，不足10次循环时以空格进行填充补齐
        sb.append(getParm("",176*10));//空
        //循环体结束
        //上级法人信息或主管单位信息
        sb.append(getParm("",251));//空

        sb.append(getParm("1",2));//被授权人数-
        ////办理业务种类（单据中部分需勾选内容）
        sb.append(getParm("",1));//A办理单位银行结算账户-开户
        sb.append(getParm("",1));//A办理单位银行结算账户-变更
        sb.append(getParm("",1));//A办理单位银行结算账户-销户
        sb.append(getParm("",1));//B签署综合服务协议-账户管理与综合产品服务协议
        sb.append(getParm("",1));//B签署综合服务协议-综合产品服务申请书
        sb.append(getParm("",1));//B签署综合服务协议-银企对账协议或变更协议
        sb.append(getParm("",1));//B签署综合服务协议-综合产品服务章程
        sb.append(getParm("",1));//C预留签章式样-本单位公章
        sb.append(getParm("",1));//C预留签章式样-财务专用章
        sb.append(getParm("",1));//D变更预留签章式样-个人签章
        sb.append(getParm("",1));//D变更预留签章式样-本单位公章
        sb.append(getParm("",1));//D变更预留签章式样-财务专用章
        sb.append(getParm("",100));//I其他

        //以下位循环体 5次  895
        sb.append(getParm("黄宇",72));//被授权人姓名-
        sb.append(getParm("01",2));//被授权人证件类型-
        sb.append(getParm("429006199312295131",32));//被授权人证件号码-
        sb.append(getParm("20220202",8));//被授权人证件到期日-
        sb.append(getParm("",22));//被授权人办公电话
        sb.append(getParm("13517240963",22));//被授权人移动电话-
        sb.append(getParm("0",1));//A-办理单位银行结算账户
        sb.append(getParm("0",1));//B-签署综合服务协议
        sb.append(getParm("0",1));//C-预留签章样式
        sb.append(getParm("0",1));//D-变更预留签章样式
        sb.append(getParm("1",1));//E-办理日常结算及购买空白支付凭证业务
        sb.append(getParm("0",1));//F-取消日常结算及购买空白支付凭证业务
        sb.append(getParm("1",1));//G-作为我单位大额交易有权确认人员
        sb.append(getParm("0",1));//H-取消我单位大额交易有权确认人员
        sb.append(getParm("0",1));//I-其他
        sb.append(getParm("EG",10));//授权办理业务种类
        sb.append(getParm("2",2));//选中个数-

        for(int i=0;i<4;i++){
            sb.append(getParm("",72));//被授权人姓名-
            sb.append(getParm("",2));//被授权人证件类型-
            sb.append(getParm("",32));//被授权人证件号码-
            sb.append(getParm("",8));//被授权人证件到期日-
            sb.append(getParm("",22));//被授权人办公电话
            sb.append(getParm("",22));//被授权人移动电话-
            sb.append(getParm("",1));//A-办理单位银行结算账户
            sb.append(getParm("",1));//B-签署综合服务协议
            sb.append(getParm("",1));//C-预留签章样式
            sb.append(getParm("",1));//D-变更预留签章样式
            sb.append(getParm("",1));//E-办理日常结算及购买空白支付凭证业务
            sb.append(getParm("",1));//F-取消日常结算及购买空白支付凭证业务
            sb.append(getParm("",1));//G-作为我单位大额交易有权确认人员
            sb.append(getParm("",1));//H-取消我单位大额交易有权确认人员
            sb.append(getParm("",1));//I-其他
            sb.append(getParm("",10));//授权办理业务种类
            sb.append(getParm("",2));//选中个数-
        }
        //循环体结束

        sb.append(getParm("黄宇",72));//大额交易有权确认人1姓名-
        sb.append(getParm("01",2));//大额交易有权确认人1证件类型-
        sb.append(getParm("429006199312295131",32));//大额交易有权确认人1证件号码-
        sb.append(getParm("20220202",8));//大额交易有权确认人1证件到期日-
        sb.append(getParm("",22));//大额交易有权确认人1办公电话
        sb.append(getParm("13517240963",16));//大额交易有权确认人1移动电话-
        sb.append(getParm("黄旭",72));//大额交易有权确认人2姓名-
        sb.append(getParm("01",2));//大额交易有权确认人2证件类型-
        sb.append(getParm("420114199007252538",32));//大额交易有权确认人2证件号码-
        sb.append(getParm("20380106",8));//大额交易有权确认人2证件到期日-
        sb.append(getParm("",22));//大额交易有权确认人2办公电话
        sb.append(getParm("13517240963",16));//大额交易有权确认人2移动电话-

        //联系人
        sb.append(getParm("黄",40));//联系人姓-
        sb.append(getParm("宇",40));//联系人名-
        sb.append(getParm("",35));//联系人拼音姓
        sb.append(getParm("",35));//联系人拼音名
        sb.append(getParm("19931229",8));//联系人出生日期-
        sb.append(getParm("CN",2));//联系人国家/地区-
        sb.append(getParm("13517250963",22));//联系人电话-

        //以下为新增产品信息需填写，否则上送空
        sb.append(getParm("",22));//扣费账号

        //1.企业对账单
        sb.append(getParm("1",1));//是否订制企业对账单-
        sb.append(getParm("",1));//账单发送渠道
        sb.append(getParm("1",1));//账单发送渠道-网银-
        sb.append(getParm("",1));//账单发送渠道-Email
        sb.append(getParm("",1));//账单发送渠道-电子回单箱/自助设备
        sb.append(getParm("0",1));//账单发送渠道-邮寄-
        sb.append(getParm("1",1));//协议自动附属-
        sb.append(getParm("黄宇",60));//对账联系人1姓名
        sb.append(getParm("",20));//对账联系人1固定电话
        sb.append(getParm("13517240963",15));//对账联系人1手机-
        sb.append(getParm("",60));//对账联系人1电子邮箱
        sb.append(getParm("",150));//客户账单电子邮件
        sb.append(getParm("黄旭",60));//对账联系人2姓名-
        sb.append(getParm("",20));//对账联系人2固定电话
        sb.append(getParm("13517240963",15));//对账联系人2手机-
        sb.append(getParm("",60));//对账联系人2电子邮箱
        sb.append(getParm("0",1));//是否需要账单生成短信提示功能

        //2. 网上银行企业服务（WEB渠道）网银
        sb.append(getParm("1",1));//是否开通/关联到网上银行企业服务（WEB渠道
        sb.append(getParm("",1));//是否已开通网银
//    	//1．申请类型
//    	sb.append(getParm("",1));//网银新开户
//    	sb.append(getParm("",1));//账户注册
//    	sb.append(getParm("",1));//操作员注册 认证工具申请+绑定
//    	sb.append(getParm("",1));//USBKEY
//    	sb.append(getParm("",1));//E-TOKEN
//    	sb.append(getParm("",1));//短信验证码
//    	sb.append(getParm("",1));//客户服务维护
//    	sb.append(getParm("",1));//账户限额修改
//    	sb.append(getParm("",1));//操作员权限修改
//    	sb.append(getParm("",1));//客户信息修改
//    	sb.append(getParm("",1));//账户名称修改
//    	sb.append(getParm("",1));//操作员信息修改
//    	sb.append(getParm("",1));//账户删除
//    	sb.append(getParm("",1));//操作员删除
//    	sb.append(getParm("",100));//其他
//
//    	//2．服务选择
//    	//2.1．通用服务包
//    	sb.append(getParm("",1));//通用服务包
//    	//2.2．基础服务包
//    	sb.append(getParm("",1));//基础服务包
//    	sb.append(getParm("",1));//B转账汇划
//    	sb.append(getParm("",1));//QB跨行实时汇划
//    	sb.append(getParm("",1));//SA对私转账汇款
//    	sb.append(getParm("",1));//SC企业定期存款
//    	sb.append(getParm("",1));//SD企业通知存款
//    	sb.append(getParm("",1));//DA协定存款合同查询
//    	sb.append(getParm("",1));//D定向账户支付
//    	sb.append(getParm("",1));//AD单位结算卡
//    	//2.3．代收代付
//    	sb.append(getParm("",1));//代收代付
//    	sb.append(getParm("",1));//LT快捷代发(工资)
//    	sb.append(getParm("",1));//LU快捷代发(其他)
//    	sb.append(getParm("",1));//NF快捷代收
//    	sb.append(getParm("",1));//RX工资单服务
//    	sb.append(getParm("",1));//BPS商户号
//
//    	//2.4．W1超级管理员
//    	sb.append(getParm("",1));//W1超级管理员
//    	sb.append(getParm("",1));//OR操作记录查询
//    	sb.append(getParm("",1));//OS单位E-mail地址设置
//    	sb.append(getParm("",1));//OT单位LOGO设置
//    	sb.append(getParm("",1));//PH银企对账设置
//    	sb.append(getParm("",1));//OU操作员设置
//    	sb.append(getParm("",1));//PW账户别名设置
//    	sb.append(getParm("",1));//AE管理员
//
//    	//2.5．投资理财
//    	sb.append(getParm("",1));//投资理财
//    	sb.append(getParm("",1));//LW理财产品
//    	sb.append(getParm("",1));//K企业第三方存管
//    	sb.append(getParm("",1));//N企业银期转账
//    	sb.append(getParm("",1));//LO银商转账
//    	sb.append(getParm("",1));//VX大额存单查询
//    	sb.append(getParm("",1));//VY大额存单
//    	sb.append(getParm("",1));//X理财单据核对
//
//    	//2.6．国际结算
//    	sb.append(getParm("",1));// 国际结算
//    	sb.append(getParm("",1));// OA跨境汇款
//    	sb.append(getParm("",1));// OB外币结汇
//    	sb.append(getParm("",1));// OB5经常项目自动结汇
//    	sb.append(getParm("",1));// SE境内外币汇划
//    	sb.append(getParm("",1));// SF单证服务
//    	sb.append(getParm("",1));// NO汇入汇款性质确认与申报
//
//    	//2.7．汇票服务
//    	sb.append(getParm("",1));// 汇票服务
//    	sb.append(getParm("",1));// 电子银行承兑汇票
//    	sb.append(getParm("",1));// 电子商业承兑汇票
//
//    	//2.8．票据池
//    	sb.append(getParm("",1));// 票据池
//    	sb.append(getParm("",1));// 基础托管
//    	sb.append(getParm("",1));// 质押融资
//
//    	//2.9．电子商务
//    	sb.append(getParm("",1));// 电子商务
//    	sb.append(getParm("",1));// MR B2B支付服务(买家)
//    	sb.append(getParm("",1));// NS B2B预保留支付服务
//    	sb.append(getParm("",1));// M B2C商户服务
//    	sb.append(getParm("",1));// MQ B2B商户服务
//
//    	//2.10．其他
//    	sb.append(getParm("",1));// 其他
//    	sb.append(getParm("",100));// 其他描述
//
//    	//3．本企业转账交易控制
//    	sb.append(getParm("",18));// 日累计限额
//    	sb.append(getParm("",6));// 日累计笔数
//    	sb.append(getParm("",18));// 年累计限额
//
//    	//4．账户信息      以下循环，不足3次循环时以空格进行填充补齐
//    	sb.append(getParm("",1));// 序号
//    	sb.append(getParm("",32));// 账号
//    	sb.append(getParm("",3));// 币种
//    	sb.append(getParm("",100));// 账号申请的服务代码
//    	sb.append(getParm("",18));// 账户转账限额-单笔限额
//    	sb.append(getParm("",18));// 账户转账限额-每日累计限额

        //中间都为空
        sb.append(getParm("",922));

        //5．操作员信息   以下循环，不足4次循环时以空格进行填充补齐 716
        sb.append(getParm("黄宇",22));//姓名-
        sb.append(getParm("",1));//普通管理员-查询
        sb.append(getParm("",1));//普通管理员-经办
        sb.append(getParm("1",1));//普通管理员-授权-
        sb.append(getParm("",1));//超级管理员-查询
        sb.append(getParm("",1));//超级管理员-经办
        sb.append(getParm("",1));//超级管理员-授权
        sb.append(getParm("",100));//可操作的账户序号及权限
        sb.append(getParm("01",2));//证件类型-
        sb.append(getParm("429006199312295131",32));//证件号码-
        sb.append(getParm("13517240963",16));//移动电话-
        sb.append(getParm("1",1));//密码发送方式-

        for(int b=0;b<3;b++){
            sb.append(getParm("",22));//姓名-
            sb.append(getParm("",1));//普通管理员-查询
            sb.append(getParm("",1));//普通管理员-经办
            sb.append(getParm("",1));//普通管理员-授权-
            sb.append(getParm("",1));//超级管理员-查询
            sb.append(getParm("",1));//超级管理员-经办
            sb.append(getParm("",1));//超级管理员-授权
            sb.append(getParm("",100));//可操作的账户序号及权限
            sb.append(getParm("",2));//证件类型-
            sb.append(getParm("",32));//证件号码-
            sb.append(getParm("",16));//移动电话-
            sb.append(getParm("1",1));//密码发送方式-
        }
        //循环结束

        //中银单位结算卡
        sb.append(getParm("1",1));//开通中银单位结算卡-
        sb.append(getParm("",1));//申请主卡
        sb.append(getParm("黄宇",22));//指定持卡人姓名-
        sb.append(getParm("01",2));//证件类型-
        sb.append(getParm("429006199312295131",32));//证件号码-
        sb.append(getParm("13517240963",16));//手机号码-
        //以下循环，不足2次循环时以空格进行填充补齐  146空
        for(int c=0;c<2;c++){
            sb.append(getParm("",1));//申请子卡
            sb.append(getParm("",22));//指定持卡人姓名
            sb.append(getParm("",2));//证件类型
            sb.append(getParm("",32));//证件号码
            sb.append(getParm("",16));//手机号码
        }
        //循环结束

        //对公短信通
        sb.append(getParm("1",1));//开通对公短信通
        sb.append(getParm("",1));//接收动户通知
        sb.append(getParm("",1));//开通查询权限

        //以下循环，不足3次循环时以空格进行填充补齐   324
        sb.append(getParm("13517240963",16));//手机号码-
        sb.append(getParm("黄宇",22));//用户名称-
        sb.append(getParm("01",2));//证件类型-
        sb.append(getParm("429006199312295131",32));//证件号码-
        sb.append(getParm("",18));//金额区间从
        sb.append(getParm("",18));//金额区间至
        for(int d=0;d<2;d++){
            sb.append(getParm("",16));//手机号码-
            sb.append(getParm("",22));//用户名称-
            sb.append(getParm("",2));//证件类型-
            sb.append(getParm("",32));//证件号码-
            sb.append(getParm("",18));//金额区间从
            sb.append(getParm("",18));//金额区间至
        }
        //循环结束

        //回单服务
        sb.append(getParm("1",1));//开通回单服务-
        sb.append(getParm("1",1));//电子回单箱-
        sb.append(getParm("",1));//回单自助打印
        sb.append(getParm("",1));//	其他
        sb.append(getParm("",50));//其他描述
        //支付密码
        sb.append(getParm("0",1));//开通支付密码

        //后面都是空
        sb.append(getParm("",1201));

        System.out.println("["+sb.toString()+"]");
        return sb.toString();
    }
    //当前时间
    public static String getCurrentDate(String format){
        Calendar cl = Calendar.getInstance();
        return new SimpleDateFormat(format).format(cl.getTime());
    }
    //补空
    public static String getParm(String param,int len) throws Exception{
        if(StringUtils.isBlank(param)){
            param = "";
        }
        int l = param.getBytes("GBK").length;
        if(len<=l){
            return param;
        }
        int num = len-l;
        for(int i = 0;i<num;i++){
            param+=" ";
        }
        return param;
    }

    //补0
    public static String getParmStart(String param,int len) throws Exception{
        int l = param.getBytes("GBK").length;
        if(len<=l){
            return param;
        }
        int num = len-l;
        String str = "";
        for(int i = 0;i<num;i++){
            str+="0";
        }
        return str+param;
    }
    public static void main(String[] args) {
        try {
//    		System.out.println(getParmStart("999999",17));
//    		String res = "03580000交易成功                                                    99940693                                                                                                                                10E131000001010441                5220190221085923                                              125660845900000                    00022CA19022147477727AP1001";
//    		System.out.println(res.substring(res.length()-22,res.indexOf("AP1001")));
//    		String reszh ="     0000074194792239600000741942349463000007419467765360000074194120361600000741945630688000007419400577540000074194448482000000741948911893000007419433389600000074194776603800000741942193117000007419466201810000074194104725800000741945474322000007419499013950000074194432846300000741948755548000007419431826150000074194760968700000741942036754";
//    		String[] zh = reszh.substring(reszh.length()-340).split("000007");
//    		System.out.println(reszh.substring(reszh.length()-340));
            //chinaBank(new HashMap());
            System.out.println(getParm("黄江镇长龙宏安街3号301室",72)+"ssss");
            String zw = "供应链管理服务；国内货运代理服务；销售：汽车配件、汽车；代办机动车业务、道路普通货运。";
            String yw = "供应链管理服务;国内货运代理服务;销售:汽车配件、汽车;代办机动车业务、道路普通货运。";
            System.out.println(getParm(zw,100)+"zw");
            System.out.println(getParm(yw,100)+"yw");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String xm(String name){
        String[] bai={"欧阳","太史","上官","端木","司马","东方","独孤","南宫","万俟","闻人","夏侯","诸葛","尉迟","公羊","赫连","澹台","皇甫","宗政","濮阳","公冶","太叔","申屠","公孙","慕容","仲孙","钟离","长孙","宇文","司徒","鲜于","司空","闾丘","子车","亓官","司寇","巫马","公西","颛孙","壤驷","公良","漆雕","乐正","宰父","谷梁","拓跋","夹谷","轩辕","令狐","段干","百里","呼延","东郭","南门","羊舌","微生","公户","公玉","公仪","梁丘","公仲","公上","公门","公山","公坚","左丘","公伯","西门","公祖","第五","公乘","贯丘","公皙","南荣","东里","东宫","仲长","子书","子桑","即墨","达奚","褚师","吴铭"};
        String name1=name.substring(0,2);
        for (int i = 0; i < bai.length; i++) {
            if (name1.equals(bai[i])) {
                return name1+"|"+name.substring(2);
            }
        }
        return name.charAt(0)+"|"+name.substring(1);

    }

    //中行对公账号开户 AP1001|
    public static String chinaBankZs(Map map) throws Exception{
        StringBuilder sb = new StringBuilder();
        //CSP报文头
        sb.append(map.get("jybh"));
        sb.append(getParm("9912",4));
        sb.append(getParm("4405",10));
        sb.append(getParm("GSZYT000",8));
        sb.append(getParm("a99907",10));
        sb.append(getParm("MC",2));
        String timestamp = System.currentTimeMillis()+"";
        sb.append(getParm(timestamp.substring(1,timestamp.length()),12));
        sb.append(getParm("",88));
        sb.append(getParm("9778",4));

        //MCIS报文头
        sb.append(getParm("0",1));//报文类型
        sb.append(getParm("0",1));//同异步标志
        sb.append(getParm("E131000001",10));//交易码
        sb.append(getParm("01",2));//路由类型
        sb.append(getParm("0440",4));//路由字符串
        sb.append(getParm("",16));//16位空格
        sb.append(getParm("53",2));//渠道标识
        sb.append(getCurrentDate("yyyyMMdd"));//交易日期
        sb.append(getCurrentDate("HHmmss"));//交易时间
        sb.append(getParm("",2));//前端流水号前2位
        sb.append(getParm("",14));//前端流水号后14位
        sb.append(getParm("",30));//30位空格
        sb.append(getParm("",8));//后台返回流水号
        sb.append(getParm("",7));//后台返回码
        sb.append(getParm("",7));//柜员
        sb.append(getParm("",13));//FILLER
        sb.append(getParm("09642",5));//报文体长度

        //公司信息
        ApplyInfoModel company = (ApplyInfoModel)((Map)map.get("conpanyInfo")).get("applyInfo");
        Map openaccount = null;
        PeopleDutyModel legal = null;
        PeopleDutyModel link = null;
        List<PeopleDutyModel> shareholders = new ArrayList();
        PeopleDutyModel chief = null;
        PeopleDutyModel operator = null;
         if("9911".equals(company.getEntitytypeid())){
            //开户信息
            openaccount = (Map)map.get("openaccount");
            //公司经营者
            legal = (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("individual");
            //联系人
            link = legal;
            //股东信息
            shareholders.add(legal);
            //财务负责人
            chief = legal;
            //经办人
            operator = (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("operator");
        }else{ // if("1100".equals(company.getEntitytypeid()))
             //开户信息
             openaccount = (Map)map.get("openaccount");
             //法人信息
             legal = (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("legal");
             //联系人
             link = legal; // (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("link");
             //股东信息
             shareholders = (List)((Map)map.get("conpanyInfo")).get("shareholders");
             //财务负责人
             chief = (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("chief");
             //经办人
             operator = (PeopleDutyModel)((Map)map.get("conpanyInfo")).get("operator");
         }

        //授权业务
        List list = (List)map.get("sqyw");

        //公共报文头
        sb.append(getParm("MCA001",6));//交易码
        sb.append(getParm("2",1));//表单类型
        sb.append(getParm("01",2));//证件类型
        sb.append(getParm(operator.getIdcardNo(),32));//证件号码  "429006199312295131"
        sb.append(getParm(operator.getName(),72));//客户姓名 "黄宇"
        sb.append(getParm("13517240963",22));//手机号
        sb.append(getParm("",30));//电子邮件
        sb.append(getParm("15",2));//渠道标识
        sb.append(getParm("AP1001",6));//业务类型
        sb.append(getParm("",30));//个人核心客户号/对公账号
        sb.append(getParm("",16));//业务申请表编号
        sb.append(getParm("0",1));//是否代办
        sb.append(getParm("0",1));//是否指定预约办理机构
        sb.append(getParm("",5));//省行机构号
        sb.append(getParm("",5));//机构号
        sb.append(getParm("",32));//前端系统流水号
        sb.append(getParm("09374",5));//申请表数据长度

        //AP1001 对公客户综合开户
        sb.append(getParm("1",1));//开立账户种类
        sb.append(getParm("",1));//专用账户资金性质
        sb.append(getParm("",8));//临时账户有效期（到期日）
        sb.append(getParm("",18));//申请现金库存限额--------------
        sb.append(getParm(openaccount.get("LKHYHJGH")+"",5));//拟开户银行机构号-
        sb.append(getParm("58",2));//基本证明文件类型-
        sb.append(getParm(company.getUSID(),32));//基本证明文件号码-    914419005517143412
        sb.append(getParm(company.getCorpName(),72));//单位名称-
        sb.append(getParm("",120));//单位外文名称
        sb.append(getParm("",40));//单位简称
        sb.append(getParm("",40));//单位外文简称

        sb.append(getParm(openaccount.get("DWDZ1")+"",120));//单位地址1-   可能是118
        sb.append(getParm(null == openaccount.get("DWDZ2")?"":(openaccount.get("DWDZ2")+""),20));//单位地址2-
        sb.append(getParm(null == openaccount.get("DWDZ3")?"":(openaccount.get("DWDZ3")+""),20));//单位地址3-
        sb.append(getParm("中国",20));//单位地址4-
        sb.append(getParm("",6));//单位地址1地址代码
        sb.append(getParm("",6));//单位地址2地址代码
        sb.append(getParm("",6));//单位地址3地址代码
        sb.append(getParm("",2));//单位地址4地址代码
        sb.append(getParm(company.getPostcode(),8));//单位电话国家/地区码-
        sb.append(getParm("",10));//单位电话地区码
        sb.append(getParm("0769",10));//单位电话地区码-  0769 东莞
        sb.append(getParm(openaccount.get("DWDH")+"",22));//单位电话号码-
        sb.append(getParm(openaccount.get("DWDZ1")+"",120));//注册地址1-   可能是118
        sb.append(getParm(null == openaccount.get("DWDZ2")?"":(openaccount.get("DWDZ2")+""),20));//注册地址2-
        sb.append(getParm(null == openaccount.get("DWDZ3")?"":(openaccount.get("DWDZ3")+""),20));//注册地址3-
        sb.append(getParm("中国",20));//注册地址4-
        sb.append(getParm("",6));//注册地址1地址代码
        sb.append(getParm("",6));//注册地址2地址代码
        sb.append(getParm("",6));//注册地址3地址代码
        sb.append(getParm("",2));//注册地址4地址代码
        sb.append(getParm(company.getPostcode(),8));//注册地邮编-
        sb.append(getParm(openaccount.get("SFSSGS")+"",1));//是否上市公司-
        sb.append(getParm(openaccount.get("CKRLB")+"",3));//存款人类别-
        sb.append(getParm(openaccount.get("HYFL")+"",1));//行业分类-
        sb.append(getParm(openaccount.get("QYLX")+"",1));//企业类型-
        sb.append(getParm("CNY",3));//注册币种-

        BigDecimal regcapital = new BigDecimal(company.getRegcapital());
        sb.append(getParm(getParmStart(regcapital.multiply(new BigDecimal(10000)).setScale(0,BigDecimal.ROUND_HALF_DOWN).toString(),14)+"000+",18));//注册金额-
        sb.append(getParm(company.getBusinessscope(),100));//经营范围-
        sb.append(getParm("",30));//其他业务
        sb.append(getParm(company.getPostcode(),6));//地区代码-
        sb.append(getParm(getCurrentDate("yyyyMMdd"),8));//成立日期-
        sb.append(getParm("20991231",8));//证件到期日-
        sb.append(getParm("",10));//传真国家/地区码
        sb.append(getParm("",10));//传真地区码
        sb.append(getParm("",22));//传真号码
        sb.append(getParm("",30));//邮箱
        sb.append(getParm(openaccount.get("HYFL")+"0000",5));//行业代码-

        //其他证明文件
        sb.append(getParm("",2));//证明文件种类
        sb.append(getParm("",32));//证明文件编号
        sb.append(getParm("",8));//证明文件到期日
        //金融许可证
        sb.append(getParm("",32));//金融许可证或支付许可证编号
        sb.append(getParm("",8));//证件到期日
        //组织机构代码证
        sb.append(getParm("",32));//组织机构代码证-
        sb.append(getParm("",8));//组织机构代码证到期日-
        //税务登记证信息
        sb.append(getParm("",32));//国税登记证号码-
        sb.append(getParm("",8));//国税登记证号码到期日
        sb.append(getParm("",32));//地税登记证号码-
        sb.append(getParm("",8));//地税登记证号码到期日
        //法定代表人/单位负责人
        String[] xm = xm(legal.getName()).split("\\|");
        sb.append(getParm("0",1));//法人/单位负责人-
        sb.append(getParm(xm[0],40));//法定代表人/单位负责人姓-
        sb.append(getParm(xm[1],40));//法定代表人/单位负责人名-
        sb.append(getParm("",35));//法定代表人/单位负责人拼音姓
        sb.append(getParm("",35));//法定代表人/单位负责人拼音名
        sb.append(getParm("0"+legal.getIdcardType(),2));//法定代表人/单位负责人证件种类-
        sb.append(getParm(legal.getIdcardNo(),32));//法定代表人/单位负责人证件号码-
        sb.append(getParm(legal.getCardenddate()+"",8));//法定代表人/单位负责人证件到期日-
        sb.append(getParm(legal.getBirthDate(),8));//法定代表人/单位负责人出生日期-
        sb.append(getParm("CN",2));//法定代表人/单位负责人国家/地区-

        //财务负责人信息
        xm = xm(chief.getName()).split("\\|");
        sb.append(getParm(xm[0],40));//财务负责人姓-
        sb.append(getParm(xm[1],40));//财务负责人名-
        sb.append(getParm("",35));//财务负责人拼音姓
        sb.append(getParm("",35));//财务负责人拼音名
        sb.append(getParm("0"+chief.getIdcardType(),2));//财务负责人证件种类-
        sb.append(getParm(chief.getIdcardNo(),32));//财务负责人证件号码-
        sb.append(getParm(chief.getCardenddate()+"",8));//财务负责人证件到期日-
        sb.append(getParm(chief.getBirthDate(),8));//财务负责人出生日期-
        sb.append(getParm("CN",2));//财务负责人国家/地区-
        sb.append(getParm("",22));//财务负责人固定电话
        sb.append(getParm(chief.getPhone(),22));//财务负责人手机-
        //控股股东信息
        sb.append(getParm(shareholders.get(0).getName(),72));//控股股东姓 名-
        sb.append(getParm("0"+shareholders.get(0).getIdcardType(),2));//控股股东证件种类-
        sb.append(getParm(shareholders.get(0).getIdcardNo(),32));//控股股东证件号码-
        sb.append(getParm(shareholders.get(0).getCardenddate(),8));//控股股东证件到期日-
        //实际控制人信息
        sb.append(getParm("",72));//实际控制人姓 名
        sb.append(getParm("",2));//实际控制人证件种类
        sb.append(getParm("",32));//实际控制人证件号码
        sb.append(getParm("",8));//实际控制人证件到期日

        //关联企业登记信息
        sb.append(getParm("000",3));//实际控制人证件到期日-
        //以下为循环体，不足10次循环时以空格进行填充补齐
        sb.append(getParm("",176*10));//空
        //循环体结束
        //上级法人信息或主管单位信息
        sb.append(getParm("",251));//空

        sb.append(getParm("1",2));//被授权人数-
        ////办理业务种类（单据中部分需勾选内容）
        sb.append(getParm("",1));//A办理单位银行结算账户-开户
        sb.append(getParm("",1));//A办理单位银行结算账户-变更
        sb.append(getParm("",1));//A办理单位银行结算账户-销户
        sb.append(getParm("",1));//B签署综合服务协议-账户管理与综合产品服务协议
        sb.append(getParm("",1));//B签署综合服务协议-综合产品服务申请书
        sb.append(getParm("",1));//B签署综合服务协议-银企对账协议或变更协议
        sb.append(getParm("",1));//B签署综合服务协议-综合产品服务章程
        sb.append(getParm("",1));//C预留签章式样-本单位公章
        sb.append(getParm("",1));//C预留签章式样-财务专用章
        sb.append(getParm("",1));//D变更预留签章式样-个人签章
        sb.append(getParm("",1));//D变更预留签章式样-本单位公章
        sb.append(getParm("",1));//D变更预留签章式样-财务专用章
        sb.append(getParm("",100));//I其他

        //以下位循环体 5次  895
        int de = 0; //大额
        int qydz = 0;//企业对账
        List list_qy = new ArrayList();
        //int dx = 0;//短信
        int js = 0;//结算

        for(int i =0 ;i<list.size();i++){

            Map map_fl = ((Map)list.get(i));
            if("1".equals(map_fl.get("H")) && "1".equals(map_fl.get("G"))){
                de++;
            }
            if("1".equals(map_fl.get("WYDZR")) || "1".equals(map_fl.get("WYSQR"))){
                qydz++;
                list_qy.add(map_fl);
            }

            if("1".equals(map_fl.get("A"))){
                js++;
            }
        }
        for(int s =0 ;s<list.size();s++){
            Map map_sqr = ((Map)list.get(s));
            if("3".equals(map_sqr.get("SQRLX"))){
                sb.append(getParm(map_sqr.get("NAME")+"",72));//被授权人姓名-
                sb.append(getParm("0"+map_sqr.get("IDCARD_TYPE")+"",2));//被授权人证件类型-
                sb.append(getParm(map_sqr.get("IDCARD_NO")+"",32));//被授权人证件号码-
                sb.append(getParm(map_sqr.get("CARD_END_DATE")+"",8));//被授权人证件到期日-
                sb.append(getParm("",22));//被授权人办公电话
                sb.append(getParm(map_sqr.get("PHONE")+"",22));//被授权人移动电话-
                sb.append(getParm(map_sqr.get("A")+"",1));//A-办理单位银行结算账户
                sb.append(getParm(map_sqr.get("B")+"",1));//B-签署综合服务协议
                sb.append(getParm(map_sqr.get("C")+"",1));//C-预留签章样式
                sb.append(getParm(map_sqr.get("D")+"",1));//D-变更预留签章样式
                sb.append(getParm(map_sqr.get("E")+"",1));//E-办理日常结算及购买空白支付凭证业务
                sb.append(getParm(map_sqr.get("F")+"",1));//F-取消日常结算及购买空白支付凭证业务
                sb.append(getParm(map_sqr.get("G")+"",1));//G-作为我单位大额交易有权确认人员
                sb.append(getParm(map_sqr.get("H")+"",1));//H-取消我单位大额交易有权确认人员
                sb.append(getParm(map_sqr.get("I")+"",1));//I-其他
                sb.append(getParm(null==map_sqr.get("HZ")?"":map_sqr.get("HZ")+"",10));//授权办理业务种类
                sb.append(getParm(map_sqr.get("SUM")+"",2));//选中个数-
                break;
            }
        }
        for(int k=0;k<4;k++){
            sb.append(getParm("",72));//被授权人姓名-
            sb.append(getParm("",2));//被授权人证件类型-
            sb.append(getParm("",32));//被授权人证件号码-
            sb.append(getParm("",8));//被授权人证件到期日-
            sb.append(getParm("",22));//被授权人办公电话
            sb.append(getParm("",22));//被授权人移动电话-
            sb.append(getParm("",1));//A-办理单位银行结算账户
            sb.append(getParm("",1));//B-签署综合服务协议
            sb.append(getParm("",1));//C-预留签章样式
            sb.append(getParm("",1));//D-变更预留签章样式
            sb.append(getParm("",1));//E-办理日常结算及购买空白支付凭证业务
            sb.append(getParm("",1));//F-取消日常结算及购买空白支付凭证业务
            sb.append(getParm("",1));//G-作为我单位大额交易有权确认人员
            sb.append(getParm("",1));//H-取消我单位大额交易有权确认人员
            sb.append(getParm("",1));//I-其他
            sb.append(getParm("",10));//授权办理业务种类
            sb.append(getParm("",2));//选中个数-
        }
        de = de>=2?2:de;
        //循环体结束
        for(int d=0;d<de;d++){
            Map map_de = ((Map)list.get(d));
            sb.append(getParm(map_de.get("NAME")+"",72));//大额交易有权确认人1姓名-
            sb.append(getParm("0"+map_de.get("IDCARD_TYPE")+"",2));//大额交易有权确认人1证件类型-
            sb.append(getParm(map_de.get("IDCARD_NO")+"",32));//大额交易有权确认人1证件号码-
            sb.append(getParm(map_de.get("CARD_END_DATE")+"",8));//大额交易有权确认人1证件到期日-
            sb.append(getParm("",22));//大额交易有权确认人1办公电话
            sb.append(getParm(map_de.get("PHONE")+"",16));//大额交易有权确认人1移动电话-
        }
        for(int e=0;e<2-de;e++){
            sb.append(getParm("",72));//大额交易有权确认人2姓名-
            sb.append(getParm("",2));//大额交易有权确认人2证件类型-
            sb.append(getParm("",32));//大额交易有权确认人2证件号码-
            sb.append(getParm("",8));//大额交易有权确认人2证件到期日-
            sb.append(getParm("",22));//大额交易有权确认人2办公电话
            sb.append(getParm("",16));//大额交易有权确认人2移动电话-
        }

        //联系人
        xm = xm(link.getName()).split("\\|");
        sb.append(getParm(xm[0],40));//联系人姓-
        sb.append(getParm(xm[1],40));//联系人名-
        sb.append(getParm("",35));//联系人拼音姓
        sb.append(getParm("",35));//联系人拼音名
        sb.append(getParm(link.getBirthDate(),8));//联系人出生日期-
        sb.append(getParm("CN",2));//联系人国家/地区-
        sb.append(getParm(link.getPhone(),22));//联系人电话-

        //以下为新增产品信息需填写，否则上送空
        sb.append(getParm("",22));//扣费账号


        //1.企业对账单
        sb.append(getParm("1",1));//是否订制企业对账单-
        sb.append(getParm("",1));//账单发送渠道
        sb.append(getParm("1",1));//账单发送渠道-网银-
        sb.append(getParm("",1));//账单发送渠道-Email
        sb.append(getParm("",1));//账单发送渠道-电子回单箱/自助设备
        sb.append(getParm("0",1));//账单发送渠道-邮寄-
        sb.append(getParm("1",1));//协议自动附属-

        if(list_qy.size()>=2){
            Map map_qy1 = (Map) list_qy.get(0);
            Map map_qy2 = (Map) list_qy.get(1);
            sb.append(getParm(map_qy1.get("NAME")+"",60));//对账联系人1姓名
            sb.append(getParm("",20));//对账联系人1固定电话
            sb.append(getParm(map_qy1.get("PHONE")+"",15));//对账联系人1手机-
            sb.append(getParm("",60));//对账联系人1电子邮箱
            sb.append(getParm("",150));//客户账单电子邮件
            sb.append(getParm(map_qy1.get("NAME")+"",60));//对账联系人2姓名-
            sb.append(getParm("",20));//对账联系人2固定电话
            sb.append(getParm(map_qy1.get("PHONE")+"",15));//对账联系人2手机-
            sb.append(getParm("",60));//对账联系人2电子邮箱
        }else if(list_qy.size()==1){
            Map map_qy1 = (Map) list_qy.get(0);
            sb.append(getParm(map_qy1.get("NAME")+"",60));//对账联系人1姓名
            sb.append(getParm("",20));//对账联系人1固定电话
            sb.append(getParm(map_qy1.get("PHONE")+"",15));//对账联系人1手机-
            sb.append(getParm("",60));//对账联系人1电子邮箱
            sb.append(getParm("",150));//客户账单电子邮件
            sb.append(getParm("",60));//对账联系人2姓名-
            sb.append(getParm("",20));//对账联系人2固定电话
            sb.append(getParm("",15));//对账联系人2手机-
            sb.append(getParm("",60));//对账联系人2电子邮箱
        }else if(list_qy.size()==0){
            sb.append(getParm("",60));//对账联系人1姓名
            sb.append(getParm("",20));//对账联系人1固定电话
            sb.append(getParm("",15));//对账联系人1手机-
            sb.append(getParm("",60));//对账联系人1电子邮箱
            sb.append(getParm("",150));//客户账单电子邮件
            sb.append(getParm("",60));//对账联系人2姓名-
            sb.append(getParm("",20));//对账联系人2固定电话
            sb.append(getParm("",15));//对账联系人2手机-
            sb.append(getParm("",60));//对账联系人2电子邮箱
        }

        sb.append(getParm("0",1));//是否需要账单生成短信提示功能

        //2. 网上银行企业服务（WEB渠道）网银
        sb.append(getParm("1",1));//是否开通/关联到网上银行企业服务（WEB渠道
        sb.append(getParm("",1));//是否已开通网银

        //中间都为空
        sb.append(getParm("",922));


        //5．操作员信息   以下循环，不足4次循环时以空格进行填充补齐 716

        sb.append(getParm(operator.getName(),22));//姓名-
        sb.append(getParm("",1));//普通管理员-查询
        sb.append(getParm("",1));//普通管理员-经办
        sb.append(getParm("1",1));//普通管理员-授权-
        sb.append(getParm("",1));//超级管理员-查询
        sb.append(getParm("",1));//超级管理员-经办
        sb.append(getParm("",1));//超级管理员-授权
        sb.append(getParm("",100));//可操作的账户序号及权限
        sb.append(getParm("0"+operator.getIdcardType(),2));//证件类型-
        sb.append(getParm(operator.getIdcardNo(),32));//证件号码-
        sb.append(getParm(operator.getPhone(),16));//移动电话-
        sb.append(getParm("",1));//密码发送方式-

        for(int b=0;b<3;b++){
            sb.append(getParm("",22));//姓名-
            sb.append(getParm("",1));//普通管理员-查询
            sb.append(getParm("",1));//普通管理员-经办
            sb.append(getParm("",1));//普通管理员-授权-
            sb.append(getParm("",1));//超级管理员-查询
            sb.append(getParm("",1));//超级管理员-经办
            sb.append(getParm("",1));//超级管理员-授权
            sb.append(getParm("",100));//可操作的账户序号及权限
            sb.append(getParm("",2));//证件类型-
            sb.append(getParm("",32));//证件号码-
            sb.append(getParm("",16));//移动电话-
            sb.append(getParm("",1));//密码发送方式-
        }
        //循环结束

        for(int j=0;j<3;j++){
            Map map_de = ((Map)list.get(j));
            if("1".equals(map_de.get("A"))){
                sb.append(getParm("1",1));//申请子卡
                sb.append(getParm("",1));//申请主卡
                sb.append(getParm(map_de.get("NAME")+"",22));//指定持卡人姓名
                sb.append(getParm("0"+map_de.get("IDCARD_TYPE")+"",2));//证件类型
                sb.append(getParm(map_de.get("IDCARD_NO")+"",32));//证件号码
                sb.append(getParm(map_de.get("PHONE")+"",16));//手机号码
                break;
            }

        }
    	//中银单位结算卡

        //以下循环，不足2次循环时以空格进行填充补齐  146空
        for(int c=0;c<2;c++){
            sb.append(getParm("",1));//申请子卡
            sb.append(getParm("",22));//指定持卡人姓名
            sb.append(getParm("",2));//证件类型
            sb.append(getParm("",32));//证件号码
            sb.append(getParm("",16));//手机号码
        }
        //循环结束

        //对公短信通
        sb.append(getParm("1",1));//开通对公短信通
        sb.append(getParm("",1));//接收动户通知
        sb.append(getParm("",1));//开通查询权限

        for(int i =0 ;i<list.size();i++){
            Map map_sqr = ((Map)list.get(i));
            if("1".equals(map_sqr.get("DXJSR"))){
                sb.append(getParm(map_sqr.get("PHONE")+"",16));//手机号码-
                sb.append(getParm(map_sqr.get("NAME")+"",22));//用户名称-
                sb.append(getParm("0"+map_sqr.get("IDCARD_TYPE")+"",2));//证件类型-
                sb.append(getParm(map_sqr.get("IDCARD_NO")+"",32));//证件号码-
                sb.append(getParm("",18));//金额区间从
                sb.append(getParm("",18));//金额区间至
            }else{
                sb.append(getParm("",16));//手机号码-
                sb.append(getParm("",22));//用户名称-
                sb.append(getParm("",2));//证件类型-
                sb.append(getParm("",32));//证件号码-
                sb.append(getParm("",18));//金额区间从
                sb.append(getParm("",18));//金额区间至
            }
        }
        //以下循环，不足3次循环时以空格进行填充补齐   324

        //循环结束

        //回单服务
        sb.append(getParm("1",1));//开通回单服务-
        sb.append(getParm("1",1));//电子回单箱-
        sb.append(getParm("",1));//回单自助打印
        sb.append(getParm("",1));//	其他
        sb.append(getParm("",50));//其他描述
        //支付密码
        sb.append(getParm("0",1));//开通支付密码

        //后面都是空
        sb.append(getParm("",1201));

        return sb.toString();
    }

    public static String chinaBankUUID() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(getParm("GSUUID|",7));
        sb.append(getParm("5344c1",6));
        return sb.toString();
    }
}
