package pw.cdmi.wishlist.rs.v1;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.wishlist.model.ProductResponse;
import pw.cdmi.wishlist.model.WinnerResponse;
import pw.cdmi.wishlist.model.entities.Product;

@RestController
@RequestMapping("/wishlist/products/v1")
public class ProductResource {
	
	/**
	 * 获取当前正在进行的众筹商品列表
	 * @return
	 */
	public List<ProductResponse> listProductInProgress(){
		return null;
	}
	
	/**
	 * 获取指定的众筹商品信息
	 * @return
	 */
	public ProductResponse getProductDetail(String productId){
		return null;
	}
	
	/**
	 * 查看已取消众筹的商品列表
	 * @return
	 */
	public List<ProductResponse> listProductCanceled(){
		return null;
	}
	
	/**
	 * 查看已完成众筹抽奖的商品列表
	 * @return
	 */
	public List<ProductResponse> listProductFinished(){
		return null;
	}
	
	/**
	 * 查看指定已完成众筹抽奖的商品的获奖人名单
	 * @return
	 */
	public List<WinnerResponse> listWinnerByProductFinished(String productId){
		return null;
	}
	
	/**
	 * 创建一个众筹商品信息
	 * @return
	 */
	public void createProduct(Product product){
		return;
	}
	
	/**
	 * 复制一个众筹商品信息
	 * @return
	 */
	public void copyProduct(String productId){
		return;
	}
	
	/**
	 * 修改一个众筹商品信息
	 * @return
	 */
	public void modifyProduct(String productId, Product product){
		return;
	}
	
	/**
	 * 发布一个众筹商品信息
	 * @return
	 */
	public void publicProduct(String productId){
		return;
	}
}
