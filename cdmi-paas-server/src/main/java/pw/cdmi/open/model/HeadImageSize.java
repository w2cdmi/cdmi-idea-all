package pw.cdmi.open.model;

import pw.cdmi.core.entities.model.ImageSize;

/****************************************************
 * 枚举类，用户头像图片的大小。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
public enum HeadImageSize implements ImageSize {
	
	SIZE16(16),

	SIZE24(24),

	SIZE32(32),

	SIZE48(48);

	private int height;

	private HeadImageSize(int size) {
		this.height = size;
	}

	public static HeadImageSize fromValue(int height) {
		for (HeadImageSize imagesize : HeadImageSize.values()) {
			if (imagesize.height == height) {
				return imagesize;
			}
		}
		return null;
	}
	
	/**
	 * 国旗的形状是一个矩形，该方法获得国旗图片的长度
	 */
	@Override
	public int getWidth() {
		return this.height * 2;
	}

	/**
	 * 国旗的形状是一个矩形，该方法获得国旗图片的高度
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

}
