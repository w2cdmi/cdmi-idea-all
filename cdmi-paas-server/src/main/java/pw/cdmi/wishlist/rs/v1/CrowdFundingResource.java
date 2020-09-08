package pw.cdmi.wishlist.rs.v1;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.wishlist.model.OnlookerReponse;
import pw.cdmi.wishlist.model.ParticipantResponse;
import pw.cdmi.wishlist.model.ProductStatus;
import pw.cdmi.wishlist.model.WinProduct;
import pw.cdmi.wishlist.model.entities.Participant;
import pw.cdmi.wishlist.model.entities.Product;
import pw.cdmi.wishlist.model.entities.WxUser;
import pw.cdmi.wishlist.service.CrowdFundingService;
import pw.cdmi.wishlist.service.ProductService;
import pw.cdmi.wishlist.service.WxUserService;

@RestController
@RequestMapping("/wishlist/crowdfundings/v1")
public class CrowdFundingResource {

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private CrowdFundingService crowdFundingService;

	@Autowired
	private ProductService productService;
	
	/**
	 * 邀请我的好友进行围观支持
	 * 有token后，则ownerId不需要
	 * @return
	 */
	public long createMySupporter(String friendId, String ownerId, String productId) {
		// 检查参数ID是否存在，
		if (StringUtils.isBlank(friendId) || StringUtils.isBlank(ownerId) || StringUtils.isBlank(productId)) {
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}

		WxUser user = wxUserService.getById(friendId);
		if (user == null) {
			throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.NoFoundData);
		}

		// 检查ownerId是否是众筹商品活动参与人
		Participant participant = crowdFundingService.getParticipant(ownerId, productId);
		if (participant != null) {
			//邀请方是众筹参与人，将用户加入到我的心愿商品的围观者清单中。
			crowdFundingService.addOnlooker(user.getId(), ownerId, productId);
			return crowdFundingService.updateInviterNumberOfParticipant(participant);
		} else {
			//邀请方不是众筹参与人，则不会将用户加入到心愿，do nothing.
		}
		return 0l;
	}

	/**
	 * 查看我的心愿商品支持围观用户
	 * 
	 * @return
	 */
	public List<OnlookerReponse> listMyOnlooker(String productId) {
		return null;
	}

	/**
	 * 将众筹商品加入到我的心愿单，前置条件是需要支付1元后进行
	 * 有token后，则ownerId不需要
	 * @return
	 */
	public void createMyWishList(String productId, String ownerId) {
		// 检查参数ID是否存在。
		if (StringUtils.isBlank(productId) || StringUtils.isBlank(ownerId)) {
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		// 检查商品是否存在
		Product product= productService.getProductById(productId);
		if(product == null || product.getStatus() != ProductStatus.InProgress) {
			//商品不存在或众筹状态不满足加入心愿单的条件
			throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.DataConsistent);
		}
		if(product.getSalesValidity() !=null  && product.getSalesValidity().getTime() > new Date().getTime()) {
			// 众筹时间已过，不能加入到心愿单
			throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.DataConsistent);
		}
		// TODO 检查ownerId是否有资格参加，账号是否存在，是否已支付1元钱。
		
		//将商品加入到ownerId的心愿商品单中
		crowdFundingService.addParticipant(ownerId, productId);
		return;
	}

	/**
	 * 当前商品，我是否已加入到我的心愿单，主要是为了显示或禁止支付按钮
	 * 
	 * @return
	 */
	public boolean isMyProduct(String productId) {
		return false;
	}

	/**
	 * 获得指定心愿商品的参与人的获胜中奖概率列表
	 * 
	 * @param productId
	 *            指定的心愿商品的ID
	 * @param cursor
	 *            从符合条件的结果集中按顺序从cursor位置开始
	 * @param maxcount
	 *            最大获取的结果集数量
	 * @return
	 */
	public List<ParticipantResponse> listParticipantByProduct(String productId, int cursor, int maxcount) {
		return null;
	}

	/**
	 * 查看我的中奖记录
	 * 
	 * @return
	 */
	public List<WinProduct> listWinProduct() {
		return null;
	}
}
