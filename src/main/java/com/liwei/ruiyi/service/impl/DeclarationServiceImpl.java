package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.dao.CProductDao;
import com.liwei.ruiyi.service.DeclarationService;
import com.liwei.ruiyi.dao.CDeclarationDao;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository("declarationService")
public class DeclarationServiceImpl implements DeclarationService {
    @Autowired
    private CDeclarationDao declarationDao;

    @Autowired
    CProductDao cproductDao;

    @Override
    public PageUtils queryMyDeclaration(PageUtils pageUtils) {
        String imagePath = ReadProUtils.ReadProperties("imageServiceUrl");

        PageUtils page = declarationDao.queryMyDeclaration(pageUtils);
        List<CDeclaration> data = page.getData();
        List<JSONObject> declarationList = new ArrayList<>();
        for (CDeclaration declaration : data) {
            JSONObject j = new JSONObject();
            j.put("id", declaration.getId());
            j.put("user_id", declaration.getUserId());
            j.put("user_name", declaration.getUserName());
            j.put("user_phone", declaration.getUserPhone());
            j.put("pro_name", declaration.getProName());
            j.put("link", declaration.getLink());
            j.put("shc", declaration.getShc());
            j.put("asin", declaration.getAsin());
            if (StringUtils.isNotEmpty(declaration.getImagePath())) {
                j.put("image_path", imagePath + declaration.getImagePath());
            } else {
                j.put("image_path", "");
            }
            j.put("purchase_packages", declaration.getPurchasePackages());
            j.put("per_package_quantity", declaration.getPerPackageQuantity());
            j.put("plan_total_quantity", declaration.getPlanTotalQuantity());
            j.put("total_quantity", declaration.getTotalQuantity());
            j.put("other", declaration.getOther());
            j.put("shipped_quantity", declaration.getShippedQuantity());
            j.put("received_quantity", declaration.getReceivedQuantity());
            j.put("declare_time", declaration.getDeclareTime());
            j.put("status", declaration.getStatus());
            declarationList.add(j);
        }
        page.setData(declarationList);
        return page;
    }

    @Override
    public boolean deleteDeclarationById(String id) {
        //删除的时候把冗余图片也删除掉
        CDeclaration declaration = declarationDao.queryDeclarationById(id);
        boolean delete = declarationDao.delDdeclaration(id);
        if (delete) {
            String imageSavePath = ReadProUtils.ReadProperties("imageSavePath");
            String imagePath = declaration.getImagePath();
            File file = new File(imageSavePath, imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return delete;
    }

    @Override
    public boolean createDeclaration(CDeclaration declaration) {

        return declarationDao.createDeclaration(declaration);
    }

    @Override
    public boolean updateDeclaration(CDeclaration dec) {

        return declarationDao.updateDeclaration(dec);
    }

    @Override
    public CDeclaration queryDeclarationById(String id) {
        return declarationDao.queryDeclarationById(id);
    }

    @Override
    public boolean queren(String id) {
        return declarationDao.queren(id);
    }

    @Override
    public boolean buy(CDeclaration dec) {
        //数据库中的的采购信息
        CDeclaration db_dec = declarationDao.queryDeclarationById(dec.getId().toString());
        db_dec.setPlanShipTime(dec.getPlanShipTime());

        //数据库中的商品信息
        CProduct product = cproductDao.queryProductById(dec.getProId().toString());
        //获取商品采购价格
        BigDecimal costPrice = product.getCostPrice();

        //获取未发货商品数量
        int unshipQuantity = product.getUnshipQuantity();

        db_dec.setShc(dec.getShc());//收货仓

        db_dec.setKcsl(unshipQuantity);//库存数量
        db_dec.setKcyl(dec.getKcyl());//库存用量
        db_dec.setKcdj(costPrice);//库存单价

        BigDecimal ksjz = db_dec.getKcdj().multiply(new BigDecimal(db_dec.getKcyl()));
        db_dec.setKsjz(ksjz);//库存用量价值

        //如果商品的采购价格和本次的商品采购价格不相等的情况下，更新商品采购价格
        BigDecimal pageCostPrice = dec.getCostPrice();
        if (costPrice.compareTo(pageCostPrice) != 0) {
            product.setCostPrice(pageCostPrice);
            int newId = cproductDao.updateProduct(product);
            product = cproductDao.queryProductById(newId + "");
            db_dec.setProId(newId);
        } else {
            db_dec.setProId(dec.getProId());
        }

        //获取本次采购数量
        int buyQuantity = dec.getBuyQuantity();
        //本次采购数量 * 本次采购单价 = 本次采购总价值
        BigDecimal buyPrice = pageCostPrice.multiply(new BigDecimal(buyQuantity));
        //本次采购总价值 + 未发货商品总价值 = 本次发货总价值
        BigDecimal totalPrice = buyPrice.add(ksjz);

        //开始设置采购信息
        //本次商品采购单价
        db_dec.setCostPrice(pageCostPrice);
        //本次商品采购总价
        db_dec.setCostAllPrice(totalPrice);
        //本次商品总价值
        db_dec.setTotalPrice(totalPrice);
        //本次采购量
        db_dec.setBuyQuantity(buyQuantity);
        //如果未发货商品数量 + 本次采购数量 >= 采购总量，则说明此次是足额发货，按照需求量发货，否则就按照库存+采购量发货
        if (unshipQuantity + buyQuantity >= db_dec.getTotalQuantity()) {
            db_dec.setPlanTotalQuantity(db_dec.getTotalQuantity());
        } else {
            db_dec.setPlanTotalQuantity(unshipQuantity + buyQuantity);
        }
        //修改采购信息  purchase
        declarationDao.updatePurcacheMsg(db_dec);
        if (db_dec.getKcyl() > 0) {
            //商品库存扣减
            product.setHistory("扣减库存数量:" + db_dec.getKcyl() + "件");
            //TODO 这里逻辑先不处理
        }
        return true;
    }

    @Override
    public void uploadDeclaration(String id, String types, String src) {
        switch (types) {
            case "fapiao":
                declarationDao.uploadDeclarationFaPiao(id, src);
                break;
            case "tips":
                declarationDao.uploadDeclarationTips(id, src);
                break;
            default:
                break;
        }
    }

    @Override
    public JSONArray queryDeclarationLog(String id) {
        JSONArray array = new JSONArray();
        CDeclaration dec = declarationDao.queryDeclarationById(id);
        //已申报/已确认/已采购/已发货/已完成
        String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl");

        if (StringUtils.isNotEmpty(dec.getDeclareTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getDeclareTime());
            String msg = """
                    采购信息由%s于%s提交。</br>
                    需求商品:%s  %s件，每件数量：%s，总计需求：%s。
                    """.formatted(dec.getUserName(), dec.getDeclareTime(), dec.getProName(), dec.getPurchasePackages(), dec.getPerPackageQuantity(), dec.getTotalQuantity());
            j.put("msg", msg);
            array.add(j);
        }

        if (StringUtils.isNotEmpty(dec.getConfirmTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getConfirmTime());
            String msg = """
                    信息于%s由仓库确认。</br>
                    """.formatted(dec.getConfirmTime());
            j.put("msg", msg);
            array.add(j);
        }

        if (StringUtils.isNotEmpty(dec.getPurchaseTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getPurchaseTime());
            String msg = """
                    剩余未发货商品数量：%s。</br>
                    本次采购量：%s </br>
                    本次计划发货数量：%s（剩余未发货商品数量）+%s(本次采购量)=%s(本次计划发货数量)</br>
                    计划发货时间：%s
                    """.formatted(dec.getKcsl(),
                    dec.getBuyQuantity(),
                    dec.getKcsl(), dec.getBuyQuantity(), dec.getPlanTotalQuantity(),
                    dec.getPlanShipTime());
            j.put("msg", msg);
            array.add(j);
        }

        if (StringUtils.isNotEmpty(dec.getFapiaoTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getFapiaoTime());
            String msg = """
                    发票信息已上传,点击查看<a href="javascript:downloadFile('%s%s','%s——发票信息')" style='color:#1890ff;'>%s——发票信息</a>
                    """.formatted(imageServiceUrl, dec.getFapiao(), dec.getProName(), dec.getProName());
            j.put("msg", msg);
            array.add(j);
        }

        if (StringUtils.isNotEmpty(dec.getTipsTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getTipsTime());
            String msg = """
                    标签信息已上传,点击查看<a href="javascript:downloadFile('%s%s','%s——标签信息')" style='color:#1890ff;'>%s——标签信息</a>
                    """.formatted(imageServiceUrl, dec.getTips(), dec.getProName(), dec.getProName());
            j.put("msg", msg);
            array.add(j);
        }

        if (StringUtils.isNotEmpty(dec.getShipTime())) {
            JSONObject j = new JSONObject();
            j.put("time", dec.getShipTime());
            String msg = """
                    采购商品已到库，准备贴标发FBA仓。
                    """;
            j.put("msg", msg);
            array.add(j);
        }

        return array;
    }

    @Override
    public boolean arrival(String id) {
        return declarationDao.arrival(id);
    }
}
