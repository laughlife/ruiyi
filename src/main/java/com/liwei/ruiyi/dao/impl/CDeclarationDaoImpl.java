package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CFbaReceive;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.mapper.CDeclarationMapper;
import com.liwei.ruiyi.bo.mapper.CFbaReceiveMapper;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.bo.mapper.TSellerMapper;
import com.liwei.ruiyi.dao.CDeclarationDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository("declarationDao")
public class CDeclarationDaoImpl implements CDeclarationDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public PageUtils queryMyDeclaration(PageUtils page) {
        JSONObject searchParams = page.getSearchParams();
        String key = searchParams.getString("key");
        String user_id = searchParams.getString("user_id");
        String status = searchParams.getString("status");
        String isAdmin = searchParams.getString("is_admin");
        String isLadder = searchParams.getString("is_ladder");
        String departmentCode = searchParams.getString("departmentCode");

        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from c_declaration where user_id = ?";
        String querySql = "select * from c_declaration where user_id = ? ";
        List<Object> args = new ArrayList<>();

        if (StringUtils.isNotBlank(isAdmin) && isAdmin.equals("1")) {
            sql = "select count(0) from c_declaration  where 1 = ? ";
            querySql = "select * from c_declaration where 1 = ? ";
            args.add(1);
        } else if (StringUtils.isNotBlank(isLadder) && isLadder.equals("1")) {
            sql = "select count(0) from c_declaration where user_id in (select id from t_user where department_code like ?)";
            querySql = "select * from c_declaration where user_id in (select id from t_user where department_code like ?)";
            args.add(departmentCode + "%");
        } else {
            args.add(user_id);
        }


        if (StringUtils.isNotBlank(key)) {
            key = "%" + key.trim() + "%";
            sql += " and pro_name like ?";
            querySql += " and pro_name like ?";
            args.add(key);
        }
        if (StringUtils.isNotBlank(status)) {
            sql += " and status = ? ";
            querySql += " and status = ?";
            args.add(status);
        }

        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);

        querySql += " limit ?,?";
        args.add(pageStart);
        args.add(limit);

        List<CDeclaration> supplierList = jdbc.query(querySql, new CDeclarationMapper(), args.toArray());
        page.setData(supplierList);

        return page;
    }

    @Override
    public boolean delDdeclaration(String id) {
        String sql = "delete from c_declaration where id = ?";
        int count = jdbc.update(sql, id);
        return count > 0;
    }

    @Override
    public boolean createDeclaration(CDeclaration dec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dec.getSellerId());

        sql = "insert into c_declaration(user_id,user_name,user_phone,pro_name,asin," +
                "image_path,purchase_packages,per_package_quantity,total_quantity,other," +
                "status,seller_id,seller_name,shc,link) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?)";
        Object[] args = {dec.getUserId(), dec.getUserName(), dec.getUserPhone(), dec.getProName(), dec.getAsin(),
                dec.getImagePath(), dec.getPurchasePackages(), dec.getPerPackageQuantity(), dec.getTotalQuantity(), dec.getOther(),
                "已申报", dec.getSellerId(), seller.getName(), dec.getShc(), dec.getLink()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public boolean updateDeclaration(CDeclaration dec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dec.getSellerId());

        sql = "update c_declaration set pro_name = ?,link = ?,asin = ?,seller_id = ?,seller_name = ?," +
                "shc = ?,image_path = ?,purchase_packages = ?,per_package_quantity = ?,total_quantity = ?," +
                "other = ? where id = ?";
        Object[] args = {dec.getProName(), dec.getLink(), dec.getAsin(), dec.getSellerId(), seller.getName(),
                dec.getShc(), dec.getImagePath(), dec.getPurchasePackages(), dec.getPerPackageQuantity(), dec.getTotalQuantity(),
                dec.getOther(), dec.getId()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public CDeclaration queryDeclarationById(String id) {
        String sql = "select * from c_declaration where id = ?";
        CDeclaration declaration = jdbc.queryForObject(sql, new CDeclarationMapper(), id);
        if (declaration != null) {
            return declaration;
        }
        return null;
    }

    @Override
    public boolean queren(String id) {
        String sql = "update c_declaration set status = '已确认',confirm_time = current_timestamp where id = ?";
        return jdbc.update(sql, id) > 0;
    }

    @Override
    public boolean updatePurcacheMsg(CDeclaration dbDec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dbDec.getSellerId());

        sql = "update c_declaration set cost_price = ?,cost_all_price = ?,total_price = ?,buy_quantity = ?,plan_total_quantity = ?," +
                "purchase_time = current_timestamp,plan_ship_time = ?,seller_id = ?,seller_name = ?,status = '已采购' where id = ?";
        Object[] args = {dbDec.getCostPrice(), dbDec.getCostAllPrice(), dbDec.getTotalPrice(), dbDec.getBuyQuantity(), dbDec.getPlanTotalQuantity(),
                dbDec.getPlanShipTime(), dbDec.getSellerId(), seller.getName(), dbDec.getId()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public void uploadDeclarationFaPiao(String id, String src) {
        String sql = "update c_declaration set fapiao = ?,fapiao_time = current_timestamp where id = ?";
        jdbc.update(sql, src, id);
    }

    @Override
    public void uploadDeclarationTips(String id, String src) {
        String sql = "update c_declaration set tips = ?,tips_time = current_timestamp where id = ?";
        jdbc.update(sql, src, id);
    }

    @Override
    public boolean arrival(String id) {
        String sql = "update c_declaration set status = '已到货',ship_time = current_timestamp where id = ?";
        return jdbc.update(sql, id) > 0;
    }

    @Override
    public boolean sendToFba(JSONObject params) {
        String id = params.getString("id");
        String planReceiveTime = params.getString("planReceiveTime");
        CDeclaration dec = queryDeclarationById(params.getString("id"));
        //获取计划发货量
        int planQuantity = dec.getPlanTotalQuantity();
        //检查实际发货量
        int actualQuantity = params.getInteger("sendQuantity");


        if (actualQuantity > planQuantity) {
            //如果实际发货量大于计划发货量，逻辑会出现问题的，暂时不发货
            return false;
        } else if (planQuantity == actualQuantity) {
            String sql = "update c_declaration set `status` = '已发货',send_time = current_timestamp,plan_receive_time = ?,send_quantity = ? where id = ?";
            Object[] args = {planReceiveTime, actualQuantity, id};
            return jdbc.update(sql, args) > 0;
        } else {
            int wfsl = planQuantity - actualQuantity;
            int spid = dec.getProId();
            BigDecimal costPrice = dec.getCostPrice();
            BigDecimal unshipPrice = costPrice.multiply(new BigDecimal(wfsl));

            //先更新库存
            String sql = "update c_product set unship_quantity = unship_quantity + ?,unship_price = unship_price + ? where id = ?";
            Object[] args = {wfsl, unshipPrice, spid};
            jdbc.update(sql, args);//未发货数量

            sql = "update c_declaration set `status` = '已发出',send_time = current_timestamp,plan_receive_time = ?,shipped_quantity = ?,unshipped_quantity = ? where id = ?";
            Object[] args2 = {planReceiveTime, actualQuantity, wfsl, id};
            return jdbc.update(sql, args2) > 0;
        }
    }

    @Override
    public boolean fbaReceive(JSONObject params) {
        String id = params.getString("id");
        int receiveQuantity = params.getInteger("receiveQuantity");
        String receiveTime = params.getString("receiveTime");
        String sql = "insert into c_fba_receive(dec_id,receive_quantity,receive_time)" +
                "values (?,?,?)";
        Object[] args = {id, receiveQuantity, receiveTime};
        int count = 0; //误差
        count = jdbc.update(sql, args);
        if(count > 0){
            //计算签收误差
            receiveError(id);
        }
        return count > 0;
    }

    @Override
    public boolean deleteFbaReceive(String id) {
        CFbaReceive receive = queryFbaReceiveById(id);
        String sql = "delete from c_fba_receive where id = ?";
        if(jdbc.update(sql, id) > 0){
            receiveError(receive.getDecId()+"");
            return true;
        }
        return false;
    }

    private CFbaReceive queryFbaReceiveById(String id) {
        String sql = "select * from c_fba_receive where id = ?";
        return jdbc.queryForObject(sql, new CFbaReceiveMapper(), id);
    }

    public void receiveError(String id){
        String sql = "select sum(receive_quantity) from c_fba_receive where dec_id = ?";
        int receiveQuantity = jdbc.queryForObject(sql, Integer.class, id);

        sql = "select shipped_quantity from c_declaration where id = ?";
        int shippedQuantity = jdbc.queryForObject(sql, Integer.class, id);

        int receiverQuantity = receiveQuantity - shippedQuantity;
        sql = "update c_declaration set received_quantity = ?,error_quantity = ? where id = ?";
        Object[] args = {receiveQuantity, receiverQuantity, id};
        jdbc.update(sql, args);
    }

    @Override
    public List<CFbaReceive> queryFbaReceiveList(String id) {
        String sql = "select * from c_fba_receive where dec_id = ? order by receive_time asc";
        return jdbc.query(sql, new CFbaReceiveMapper(), id);
    }

    @Override
    public boolean signOrderFinish(String id) {
        String sql = "update c_declaration set status = '已完成',receive_time = current_timestamp where id = ?";
        if(jdbc.update(sql, id) > 0){
            return true;
        }
        return false;
    }
}
