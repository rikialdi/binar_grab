package com.binar.grab.service;

import com.binar.grab.model.Barang;

import java.util.List;
import java.util.Map;

public interface BarangService {
    public Map save(Barang barang);
    public List<Barang> list();
    public Map update(Barang barang);
    public void delete(Long id);
}
