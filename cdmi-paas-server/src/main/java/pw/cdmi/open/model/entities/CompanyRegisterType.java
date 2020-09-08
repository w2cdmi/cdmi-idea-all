package pw.cdmi.open.model.entities;

public enum CompanyRegisterType {
	NZQY("内资企业",100),
	GYQY("国有企业",110),         
	JTQY("集体企业",120),         
	GFHZQY("股份合作企业",130),       
	LYQY("联营企业",140),         
	GYLYQY("国有联营企业",141),           
	JTLYQY("集体联营企业",142),     
	GYYJTLYQY("国有与集体联营企业",143),				
	QTLYQY("其他联营企业",149),          
	YXZRGS("有限责任公司",150),                
	GYDZGS("国有独资公司",151),
	QTYXZRGS("其他有限责任公司",159),
	GFYXGS("股份有限公司",160),
	SYQY("私营企业",170),
	SYDZQY("私营独资企业",171),
	SYHHQY("私营合伙企业",172),
	SYYXZRGS("私营有限责任公司",173),
	SYGFYXGS("私营股份有限公司",174),
	GRDZQY("个人独资企业",175),
	QTNZQY("其他内资企业",190),
	YGATHZJYQY("与港澳台商合资经营企业",210),
	YGATHzJYQY("与港澳台商合作经营企业",220),
	GATSDZJYQY("港澳台商独资经营企业",230),
	GATSTZGFYXQY("港澳台商投资股份有限公司",240),
	ZWHZJYQY("中外合资经营企业",310),
	ZWHzJYQY("中外合作经营企业",320),
	WZQY("外资企业",330),
	WSTZGFYXGS("外商投资股份有限公司",340),
	WGQY("外国企业",350),
	WGQYCZDBJG("外国企业常驻代表机构",351),
	LWCBQY("提供劳务、承办工作作业企业",352),
	JNYTSDSQY("缴纳预提所得税的企业",353),
	GJYSQY("国际运输企业",354),
	QTWGQY("其他外国企业",359),
	GTJY("个体经营",400),
	GTGSH("个体工商户",410),
	NZGT("内资个体",411),
	GATGT("港澳台个体",412),
	WZGT("外资个体",413),
	GRHH("个人合伙",420),
	NZHH("内资合伙",421),
	GATHH("港澳台合伙",422),
	WZHH("外资合伙",423),
	GR("个人",430),
	NZGR("内资个人",431),
	GATGR("港澳台个人",432),
	WZGR("外资个人",433),
	FQYDW("非企业单位",500),
	SYDW("事业单位",510),
	MBFQYDW("民办非企业单位",520),
	MBFR("民办非企业单位（法人）",521),
	MBHH("民办非企业单位（合伙）",522),
	MBGR("民办非企业单位（个人）",523),
	GJJG("国家机关",530),
	ZDJG("政党机关",540),
	SHTT("社会团体",550),
	QZZZZZ("基层群众自治组织",560);
	
	private String text;		//企业登记注册类型代码含义
	
	private int code;			//企业登记注册类型代码
	
	private CompanyRegisterType(String text,int code) {
		this.text = text;
		this.code = code;
	}
}
