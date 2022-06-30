package com.binar.grab.service.procedure.impl;

import com.binar.grab.repository.BarangRepository;
import com.binar.grab.repository.SupplierRepository;
import com.binar.grab.service.procedure.BarangServiceProcedure;
import com.binar.grab.util.TemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;


@Service
public class BarangImplProcedure implements BarangServiceProcedure {


    //1. call repository banrang dan supplierbi
    @Autowired
    public BarangRepository barangRepository;

    public static final Logger log = LoggerFactory.getLogger(BarangImplProcedure.class);

    @Autowired
    private EntityManager entityManager;


    @Autowired
    public SupplierRepository supplierRepository;

    @Autowired
    public TemplateResponse templateResponse;


    @Override
    public List<Object> getBarangLikeNama(String nama) {
        return barangRepository.getBarang1("%" + nama + "%");
    }


    @Override
    public Object updateProcedure() {
        return barangRepository.updateBarangProcedure(30, "barnag 4ssaa", "pcs", 3, 2);
    }

    //    @Override
//    public BarangProcedure updateProcedure2() {
////        https://www.appsdeveloperblog.com/calling-a-stored-procedure-in-spring-boot-rest-with-jpa/
//        StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("updatebarang");
//        query.registerStoredProcedureParameter("harga1", Integer.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("nama1", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("satuan1", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("stok1", Integer.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("resid", Integer.class, ParameterMode.INOUT);
//
//        query.setParameter("harga1", 30);
//        query.setParameter("nama1", "barnag 4sc");
//        query.setParameter("satuan1", "pcss");
//        query.setParameter("stok1", 56);
//        query.setParameter("resid", 2);
//
////        query.execute();
//        int idUpdate = ((Number) query.getOutputParameterValue("resid")).intValue();
//        System.out.println("masuk idUpdateidUpdate=" + idUpdate);
//        return null;//barangRepository.updateBarangProcedure2(30,"barnag 4","pcs",3,2);
//    }
//    @SuppressWarnings("unchecked")
//    @Override//https://programmer.group/5c2402567a77a.html
//    public List<BarangProsedue> findAllViaProc() {
//        StoredProcedureQuery storedProcedureQuery = this.entityManager.createNamedStoredProcedureQuery("getByIDFunction");
//        storedProcedureQuery.setParameter("rqid", 2);
//        storedProcedureQuery.execute();
//        return storedProcedureQuery.getResultList();
//    }


}
