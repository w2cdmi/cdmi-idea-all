package com.example.demo.wishlist.rs.v1;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.upload.UploadConfig;
import com.example.demo.common.upload.UploadUtil;
import com.example.demo.oauth.server.UserTokenHelper;
import com.example.demo.weixin.rs.v1.domain.UserToken;
import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.SaleRule;
import com.example.demo.wishlist.model.entities.Product;
import com.example.demo.wishlist.service.ProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wishlist/products/v1")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserTokenHelper userTokenHelper;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private UploadUtil uploadUtil;

    /**
     * 管理员创建一件商品
     *
     * @param productParam
     * @return 商品
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Product> createProduct(@RequestBody Product productParam, @RequestHeader("Authorization") String token) {
        productParam.checkRequestPram();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        //数据库存放价格单位为分
        productParam.setOriginalPrice(productParam.getOriginalPrice() * 100);
        productParam.setActualPrice(productParam.getActualPrice() * 100);
        productParam.setStatus(ProductStatus.InProgress);
        productParam.setSalerule(SaleRule.HalfPriceCrowdFunding);
        Product product = productService.createProduct(productParam);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }


    @RequestMapping(value = "uploadImage/{productId}",method = RequestMethod.POST)
    public ResponseEntity<?> uploadImage(HttpServletRequest request, @PathVariable String productId){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("image");
        MultipartFile multipartFile = files.get(0);
        String fileName = new Date().getTime() + ".png";
        if(multipartFile.isEmpty()){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        Product product = productService.getProductById(productId);
        if(product == null){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        try {
            uploadUtil.saveFile(multipartFile, fileName);

            String photos = product.getPhotoList();
            if(photos == null || "".equals(photos)){
                photos = fileName;
            } else {
                photos = photos + "," + fileName;
            }
            product.setPhotoList(photos);
            productService.updateProduct(product);
        }catch (IOException e){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{imageName}", method = RequestMethod.GET)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response){
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + imageName);

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(uploadConfig.getFilePath() + File.separator + imageName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取正在众筹的商品列表
     *
     * @return
     */
    @RequestMapping(value = "list/inProgress", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getInProgressProductList() {
        List<Product> products = new ArrayList<>();
        products = productService.getInProgressProductList();
        if(!products.isEmpty()){
            for (Product product: products){
                product.setActualPrice(product.getActualPrice()/100);
                product.setOriginalPrice(product.getOriginalPrice()/100);
            }
        }
        return products;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Product product = new Product();
        product = productService.getProductById(id);
        if(product != null){
            product.setOriginalPrice(product.getOriginalPrice()/100);
            product.setActualPrice(product.getActualPrice()/100);
        }
        return product;
    }

    @RequestMapping(value = "/{id}/notpay", method = RequestMethod.GET)
    @ResponseBody
    public Product getNotPayProductById(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Product product = new Product();
        product = productService.getProductByIdAndStatus(id, ProductStatus.WaitPay);
        if(product != null){
            product.setOriginalPrice(product.getOriginalPrice()/100);
            product.setActualPrice(product.getActualPrice()/100);
        }
        return product;
    }
}
