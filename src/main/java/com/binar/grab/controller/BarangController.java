package com.binar.grab.controller;

import com.binar.grab.model.Barang;
import com.binar.grab.model.Supplier;
import com.binar.grab.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.binar.grab.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/binar/barang")
public class BarangController {

    @Autowired
    public BarangService barangService;

    @PostMapping("/save/{idsupplier}")
    public ResponseEntity<Map> save(@PathVariable(value = "idsupplier") Long idsupplier,
                                    @RequestBody Barang objModel) {
        Map obj = barangService.insert(objModel, idsupplier);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @PutMapping("/update/{idsupplier}")
    public ResponseEntity<Map> update(@PathVariable(value = "idsupplier") Long idsupplier,
                                      @RequestBody Barang objModel ) {
        Map map = barangService.update(objModel, idsupplier);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map map = barangService.delete(id);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map> listByBama(
            @RequestParam() Integer page,
            @RequestParam() Integer size) {
        Map list = barangService.getAll(size, page);
        return new ResponseEntity<Map>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /*
    kita ingin call rest kita sendiri: barang
    a. post localhost:8082/api/v1/binar/barang/save/{idsupplier}
    b. put localhost:8082/api/v1/binar/barang/update/{idsupplier}
    c. delete localhost:8082/api/v1/binar/barang/delete/{id}
    d. get localhost:8082/api/v1/binar/barang/list

     */




}
