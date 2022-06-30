package com.binar.grab.service.impl;

import com.binar.grab.dao.response.BarangProcedure;
import com.binar.grab.model.Barang;
import com.binar.grab.model.Supplier;
import com.binar.grab.repository.BarangRepository;
import com.binar.grab.repository.SupplierRepository;
import com.binar.grab.service.BarangService;
import com.binar.grab.service.procedure.BarangServiceProcedure;
import com.binar.grab.util.TemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BarangImplProcedure implements BarangServiceProcedure {


    //1. call repository banrang dan supplierbi
    @Autowired
    public BarangRepository barangRepository;

    public static final Logger log = LoggerFactory.getLogger(BarangImplProcedure.class);


    @Autowired
    public SupplierRepository supplierRepository;

    @Autowired
    public TemplateResponse templateResponse;


    @Override
    public List<Object> getBarangLikeNama(String nama) {
        return barangRepository.getBarang1("%"+nama+"%");
    }

    @Override
    public List<BarangProcedure> getBarangLikeNama2(String nama) {
        return barangRepository.getBarang2("%"+nama+"%");
    }

    @Override
    public Object updateProcedure() {
        return barangRepository.updateBarangProcedure(30,"barnag 4","pcs",3,2);
    }

    @Override
    public BarangProcedure updateProcedure2() {
        return barangRepository.updateBarangProcedure2(30,"barnag 4","pcs",3,2);
    }
}
