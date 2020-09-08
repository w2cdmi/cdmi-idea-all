package pw.cdmi.wishlist.model;

public enum ProductStatus {
	NotStarted,			//尚未开始
	InProgress,			//正在进行中
	ChoiceWinner,			//开奖中
	WaitPay,				//抽奖结束，等待支付
	Finished,				//已结束
	Canceled;				//已取消
}
