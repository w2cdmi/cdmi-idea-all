package pw.cdmi.open.model;

import pw.cdmi.core.entities.model.ImageSize;

/****************************************************
 * 系统中图片的URL,包含图片的大小。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
public class ImageURL {
	public final static String imageSizeSeparator ="/";
	
	private String separator;
	private String imgUrl;
	private ImageSize size;
	
	/**
	 * 创建一个图片访问连接地址
	 * 注意：图片的大小一定是在图片URL的最后面
	 * 
	 * @param templet 这个在图片URL与后面跟随的图片大小之间的分隔符
	 */
	public ImageURL(String separator){
		this.separator = separator;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * 获得特定大小图片的URL访问地址
	 * 
	 * @param size 图片的大小，当前支持输入的是图片的长度
	 * @return
	 */
	public String getImgUrl(ImageSize size){
		int postion = this.imgUrl.lastIndexOf(separator);
		return imgUrl.substring(0, postion).concat(separator + size.getWidth());
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public ImageSize getSize() {
		return size;
	}
	public void setSize(ImageSize size) {
		this.size = size;
	}
	
	@Override
	public String toString(){
		return this.imgUrl;
	}
}
