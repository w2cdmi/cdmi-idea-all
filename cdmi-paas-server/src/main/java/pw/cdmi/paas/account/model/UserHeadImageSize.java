package pw.cdmi.paas.account.model;

import pw.cdmi.core.entities.model.ImageSize;

/****************************************************
 * 微信SDK，微信用户的头像图片大小。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 27, 2014
 ***************************************************/
public enum UserHeadImageSize implements ImageSize {

	SIZE16(16),

	SIZE24(24),

	SIZE32(32),

	SIZE48(48),

	SIZE64(64),

	SIZE96(96),

	SIZE132(132);

	private int height;

	private UserHeadImageSize(int size) {
		this.height = size;
	}

	public static UserHeadImageSize fromValue(int height) {
		for (UserHeadImageSize imagesize : UserHeadImageSize.values()) {
			if (imagesize.height == height) {
				return imagesize;
			}
		}
		return null;
	}
	
	/**
	 * 微信用户头像的形状是一个正方形，该方法获得微信用户头像图片的长度
	 */
	@Override
	public int getWidth() {
		return this.height;
	}

	/**
	 * 微信用户头像的形状是一个正方形，该方法获得微信用户头像图片的高度
	 */
	@Override
	public int getHeight() {
		return this.height;
	}
}
