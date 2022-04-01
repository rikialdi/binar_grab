package com.binar.grab.controller;

import com.binar.grab.config.Config;
import com.binar.grab.model.Barang;
import com.binar.grab.model.Supplier;
import com.binar.grab.model.oauth.User;
import com.binar.grab.repository.BarangRepository;
import com.binar.grab.repository.oauth.UserRepository;
import com.binar.grab.service.SupplierService;
import com.binar.grab.service.oauth.Oauth2UserDetailsService;
import com.binar.grab.util.SimpleStringUtils;
import com.binar.grab.util.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.binar.grab.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/binar/barang")
public class BarangController {

    @Autowired
    public BarangService barangService;

    Config config = new Config();

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    public BarangRepository barangRepository;

    @Autowired
    public TemplateResponse templateResponse;

    @Autowired
    public UserRepository userRepository;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

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

    @GetMapping("list-barang")// by seller
    public ResponseEntity<Map> listNotif(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String nama,// BUY OR SELL : menentukan siapa yang akses
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            Principal principal) {
        /*
        1.principal : mendapatkan username berdasarkan token user yang akses di client
        2.  getShort : shoritng
         */
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Barang> list = null;

        User idUser = getUserIdToken(principal, userDetailsService);
        if (idUser == null) {
            return new ResponseEntity<Map>(templateResponse.notFound("User id notfound"), HttpStatus.NOT_FOUND);
        }
        if (nama != null && priceMin !=null && priceMax != null ) {
            list = barangRepository.getDataByPriceAndNama(priceMin,priceMax, "'%" + nama + "%'",show_data);
        } else if ( priceMin !=null && priceMax != null ) {
            list = barangRepository.getDataByPrice(priceMin,priceMax, show_data);
        } else if (nama != null ) {
            list = barangRepository.findByNamaLike("'%" + nama + "%'", show_data);
        } else {
            list = barangRepository.getAllData(show_data);
        }
        return new ResponseEntity<Map>(templateResponse.templateSukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    public User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
        UserDetails user = null;
        String username = principal.getName();
        if (!StringUtils.isEmpty(username)) {
            user = userDetailsService.loadUserByUsername(username);
        }
        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        User idUser = userRepository.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;
    }

    @Value("${app.uploadto.cdn}")//FILE_SHOW_RUL
    private String UPLOADED_FOLDER;

    @PostMapping(value = "/uploadsimpanbarang/{idsupplier}",consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Map> uploadFile(
            @RequestParam(value="file", required = true) MultipartFile file,
            @PathVariable(value = "idsupplier") Long idsupplier,
            @RequestParam(value="nama", required = true) String nama,
            @RequestParam(value="stok", required = true) int stok,
            @RequestParam(value="satuan", required = true) String  satuan,
            @RequestParam(value="harga", required = true) double harga
    )  {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(date);
        String fileName = UPLOADED_FOLDER + strDate + file.getOriginalFilename();
        String fileNameforDOwnload = strDate + file.getOriginalFilename();
        Path TO = Paths.get(fileName);
        Map map = new HashMap();
        try {
            Files.copy(file.getInputStream(), TO); // pengolahan upload disini :
            // insert barang
            Barang b = new Barang();
            b.setNama(nama);
            b.setStok(stok);
            b.setSatuan(satuan);
            b.setHarga(harga);
            b.setFileName(fileNameforDOwnload);
            map = barangService.insert(b, idsupplier);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Map>(templateResponse.templateEror("eror"), HttpStatus.OK);
        }
        return new ResponseEntity<Map>(templateResponse.templateSukses(map), HttpStatus.OK);
    }



}
