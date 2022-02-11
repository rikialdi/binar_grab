package com.binar.grab.service.impl;

import com.binar.grab.model.Barang;
import com.binar.grab.service.BarangService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BarangImpl  implements BarangService{

   static List<Barang> barangStatic = new ArrayList<>();
    @Override
    public Map save(Barang barang) {
        Map map = new HashMap();
        map.put("data",barang);
        map.put("status","sukses");
        map.put("message","200");
        barangStatic.add(barang);
        return map;
    }

    @Override
    public List<Barang> list() {
        Map map = new HashMap();
        map.put("data",barangStatic);
        map.put("status","sukses");
        map.put("message","200");
        return barangStatic;
    }

    @Override
    public Map update(Barang barang) {
        Barang update = new Barang();
        for(Barang obj : barangStatic  ){
            if(obj.getId() == barang.getId()){
                update.setId(obj.getId());
                update.setHarga(obj.getHarga());
                update.setNama(obj.getNama());
                barangStatic.remove(obj);
                barangStatic.add(update);
            }
        }
        Map map = new HashMap();
        map.put("data",update);
        map.put("status","sukses");
        map.put("message","200");
        return map;
    }

    @Override
    public void delete(Long id) {
        for(Barang obj : barangStatic  ){
            if(obj.getId() == id){
                barangStatic.remove(obj);
            }
        }
    }
}
