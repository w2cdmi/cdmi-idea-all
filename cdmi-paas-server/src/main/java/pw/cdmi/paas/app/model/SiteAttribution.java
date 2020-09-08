package pw.cdmi.paas.app.model;

/**
 * 应用的归属，是平台自有应用，还是第三方合作应用
 * @author wuwei
 *
 */
public enum SiteAttribution {
	Self,			//自有的应用访问不计费
	Third;			//第三方应用
}
