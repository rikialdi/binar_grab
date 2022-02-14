package com.binar.grab.controller;

import com.binar.grab.model.Barang;
import com.binar.grab.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/binar/")// localhost :8080/v1/binar
public class BarangController {
    @Autowired
    private BarangService servis;

    @GetMapping("test")
    @ResponseBody
    public ResponseEntity<Map> test() {
        Map map = new HashMap();
        map.put("data", "sukses saya");
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @GetMapping("list")
    @ResponseBody
    public ResponseEntity<List<Barang>> getList() {
        List<Barang> c = servis.list();
        return new ResponseEntity<List<Barang>>(c, HttpStatus.OK);
    }
    @PostMapping("save")
    public ResponseEntity<Map> save(
            @RequestBody Barang objModel) {
        Map obj = servis.save(objModel);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);// response
    }

    @PutMapping("update")
    public ResponseEntity<Map> update(
            @RequestBody Barang objModel) {
        Map c = servis.update(objModel);
        return new ResponseEntity<Map>(c, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {

        Map map = new HashMap();
        servis.delete(id);
        map.put("data", "sukses delete");
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
}
