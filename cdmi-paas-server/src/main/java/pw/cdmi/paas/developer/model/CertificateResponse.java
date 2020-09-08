package pw.cdmi.paas.developer.model;

import java.util.Date;

import lombok.Data;


@Data
public class CertificateResponse {
	private String id;	
	private String apiKey;
	private String secretKey;
	private String createTime;
	private String upDateTime;
}
