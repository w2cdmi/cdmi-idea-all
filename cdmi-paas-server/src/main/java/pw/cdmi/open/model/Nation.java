package pw.cdmi.open.model;

import lombok.Getter;

/************************************************************
 * 枚举类，用于展示中国56个民族的枚举信息
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-6-15
 ************************************************************/
@Getter
public enum Nation {

	Han("汉族",0),
	Mongol("蒙古族",1),
	Hui("回族",2),
	Tibetan("藏族",3),
	Uighur("维吾尔族",4),
	Miao("苗族",5),
	Yi("彝族",6),
	Zhuang("壮族",7),
	Buyi("布依族",8),
	Korean("朝鲜族",9),
	Manchu("满族",10),
	Dong("侗族",11),
	Yao("瑶族",12),
	Bai("白族",13),
	Tujia("土家族",14),
	Hani("哈尼族",15),
	Kazakh("哈萨克族",16),
	Dai("傣族",17),
	Li("黎族",18),
	Lisu("僳僳族",19),
	Wa("佤族",20),
	She("畲族",21),
	Gaoshan("高山族",22),
	Lahu("拉祜族",23),
	Shui("水族",24),
	Dongxiang("东乡族",25),
	Naxi("纳西族",26),
	Jingpo("景颇族",27),
	Kirghiz("柯尔克孜族",28),
	Tu("土族",29),
	Daur("达斡尔族",30),
	Mulam("仫佬族",31),
	Qiang("羌族",32),
	Blang("布朗族",33),
	Salar("撒拉族",34),
	Maonan("毛南族",35),
	Gelao("仡佬族",36),
	Xibe("锡伯族",37),
	Achang("阿昌族",38),
	Pumi("普米族",39),
	Tajik("塔吉克族",40),
	Nu("怒族",41),
	Uzbek("乌孜别克族",42),
	Russian("俄罗斯族",43),
	Evenki("鄂温克族",44),
	DeAng("德昂(崩龙)族",45),
	Bonan("保安族",46),
	Yugur("裕固族",47),
	Gin("京族",48),
	Tatar("塔塔尔族",49),
	Drung("独龙族",50),
	Oroqin("鄂伦春族",51),
	Hezhen("赫哲族",52),
	Menba("门巴族",53),
	Lhoba("珞巴族",54),
	Jino("基诺族",55),
	Other("其它",56),
	Foreigner("外国血统",57);

	private int value;

	private String text;

	private Nation(String text, int value) {
		this.text = text;
		this.value = value;
	}

}
