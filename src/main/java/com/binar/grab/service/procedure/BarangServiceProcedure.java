package com.binar.grab.service.procedure;

import com.binar.grab.model.Barang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BarangServiceProcedure {
    public List<Object> getBarangLikeNama(String nama);

//    public List<BarangProcedure> getBarangLikeNama2(String nama);

    public Object updateProcedure();

//    public BarangProcedure updateProcedure2();







}
