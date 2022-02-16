package com.binar.grab.service.impl;

import com.binar.grab.model.Barang;
import com.binar.grab.model.Mahasiswa;
import com.binar.grab.model.Supplier;
import com.binar.grab.repository.BarangRepository;
import com.binar.grab.repository.SupplierRepository;
import com.binar.grab.service.BarangService;
import com.binar.grab.util.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class BarangImpl implements BarangService {

    //1. call repository banrang dan supplierbi
    @Autowired
    public BarangRepository barangRepository;

    @Autowired
    public SupplierRepository supplierRepository;

    @Autowired
    public TemplateResponse templateResponse;

    @Override
    public Map insert(Barang obj, Long idsupplier) {
        try {
            if(templateResponse.chekNull(obj.getNama())){
                return   templateResponse.templateEror("Nama is Requiered");
            }

            if(templateResponse.chekNull(obj.getHarga())){
                return  templateResponse.templateEror("Harga is requiered");
            }

            if(templateResponse.chekNull(idsupplier)){
                return templateResponse.templateEror("Id Supplier is requiered");
            }
            Supplier chekId = supplierRepository.getbyID(idsupplier);
            if(templateResponse.chekNull(chekId)){
                return   templateResponse.templateEror("Id Supplier NOt found");
            }
            //do save
            obj.setSupplier(chekId);
            Barang barangSave = barangRepository.save(obj);
            return templateResponse.templateSukses(barangSave);
        }catch (Exception e){
            return templateResponse.templateEror(e);
        }

    }

    @Override
    public Map getAll(int size, int page) {
        Pageable show_data = PageRequest.of(page, size);
        Page<Barang> list = barangRepository.getAllData(show_data);
        return templateResponse.templateSukses(list);
    }


    @Override
    public Map update(Barang barang, Long idsupplier) {
        return null;
    }

    @Override
    public Map delete(Long barang) {
        return null;
    }



    @Override
    public Map findByNama(String nama, Integer page, Integer size) {
        return null;
    }

    @Override
    public Page<Barang> findByNamaLike(String nama, Pageable pageable) {
        return null;
    }
}
