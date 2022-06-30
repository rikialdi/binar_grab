package com.binar.grab.junit;

import com.binar.grab.model.Barang;
import com.binar.grab.repository.BarangRepository;
import com.binar.grab.service.BarangRestTemplateService;
import com.binar.grab.service.email.email.EmailService;
import com.binar.grab.service.email.email.MailRequest;
import com.binar.grab.service.email.email.MailResponse;
import com.binar.grab.service.procedure.BarangServiceProcedure;
import com.binar.grab.service.procedure.impl.BarangProsedure;
import com.binar.grab.util.SimpleStringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BarangJunitTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BarangServiceProcedure barangServiceProcedure;

    @Autowired
    private BarangRestTemplateService barangRestTemplateService;


    @Test
    public void contohJunit() {
        int a = 5;
        int b = 10;
        int c = a + b;
        assertEquals(15, c);

    }

    @Test
    public void contohJunit2() {
        int a = 5;
        int b = 10;
        int c = a * b;
        assertEquals(50, c);
    }

    @Test
    public void simpanBarang() {
        Barang req = new Barang();
        req.setHarga(200.0);
        req.setNama("riski wisesar");
        req.setStok(100);
        req.setSatuan("kg");

        Long idSupp = 1L;
        Map map = barangRestTemplateService.insert(req, idSupp);
        assertEquals("200", map.get("status"));
        if (map.get("status").equals("200")) {
            System.out.println(map.get("data"));
            System.out.println(map.get("status"));
            System.out.println(map.get("message"));
        } else {
            System.out.println("terjadi eror");
        }
    }

    @Test
    public void updateBarang() {
        Barang req = new Barang();
        req.setId(6L);
        req.setHarga(200.0);
        req.setNama("riski wisesar ubah");
        req.setStok(1001);
        req.setSatuan("kg");

        Long idSupp = 1L;
        Map map = barangRestTemplateService.update(req, idSupp);
        assertEquals("200", map.get("status"));
        if (map.get("status").equals("200")) {
            System.out.println(map.get("data"));
            System.out.println(map.get("status"));
            System.out.println(map.get("message"));
        } else {
            System.out.println("terjadi eror");
        }

    }

    @Test
    public void listBarang() {
        Map map = barangRestTemplateService.getAll(10, 0);
        assertEquals("200", map.get("status"));
        if (map.get("status").equals("200")) {
            System.out.println(map.get("data"));
            System.out.println(map.get("status"));
            System.out.println(map.get("message"));
        } else {
            System.out.println("terjadi eror");
        }
    }

    @Value("${SHOWFILEPATH:}")//FILE_SHOW_RUL
    private String SHOWFILEPATH;

    @Autowired
    public BarangRepository barangRepository;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();


    @Autowired
    private EmailService service;

    @Test
    public void sendEmail() {
        MailRequest request = new MailRequest();
        request.setName("Invoice Test name");
        request.setTo("rikialdipari@gmail.com");
        request.setFrom("rikialdipari@gmail.com");
        request.setSubject("Invoice Test");
        Map<String, Object> model = new HashMap<>();
        model.put("namesaya", "riki aldi pari");

        model.put("chuteicon", "https://binar-test.herokuapp.com/api/showFile/2432022025921Captureass.PNG");
        BigDecimal total = new BigDecimal(0);

        Pageable show_data = simpleStringUtils.getShort("id", "desc", 0, 1);
        Page<Barang> data = barangRepository.getAllData(show_data);


        model.put("datainvoice", data.getContent());
        model.put("iconuser", "https://binar-test.herokuapp.com/api/showFile/2432022025921Captureass.PNG");
        MailResponse ma = service.sendEmail(request, model);
        System.out.println("MailResponse 1=" + ma.getMessage());

    }

    @Test
    public void getBarangProcedure() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> exchange = restTemplate.exchange("http://localhost:8082/api/v1/binar/barang/list-procedure", HttpMethod.GET, entity, Object.class);


        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getBarangProcedure1() {
        List<Object> obj = barangServiceProcedure.getBarangLikeNama("");
        System.out.println("response  =" + obj);
    }

    @Test
    public void getBarangProcedure3() {
        StoredProcedureQuery storedProcedureQuery = this.entityManager.createStoredProcedureQuery("public.getbarang1");

        storedProcedureQuery.registerStoredProcedureParameter("rqnama", String.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("rqnama", "");
        storedProcedureQuery.execute();
        storedProcedureQuery.getResultList();
//        for(BarangProsedue b: obj){
//            System.out.println("nama  =" + b.getResnama());
//            System.out.println("satuan  =" + b.getRessatuan());
//            System.out.println("harga  =" + b.getResharga());
//
//        }

    }

    @Test
    public void updateProsedure() {
        StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("public.updatebarang");
        query.registerStoredProcedureParameter("harga1", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("nama1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("satuan1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("stok1", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("resid", Integer.class, ParameterMode.INOUT);

        query.setParameter("harga1", 30);
        query.setParameter("nama1", "barnag 4sc");
        query.setParameter("satuan1", "pcss");
        query.setParameter("stok1", 56);
        query.setParameter("resid", 2);

//        query.executeUpdate();
        query.getSingleResult();
        int idUpdate = ((Number) query.getOutputParameterValue("resid")).intValue();
    }


    @Test//sukses
    public void updateBarangProcedure() {
        Object obj = barangServiceProcedure.updateProcedure();
        System.out.println("response  1=" + obj.toString());
        System.out.println("response  =" + obj);
    }


    //    @Test//still failed
//    public void  updateBarangProcedure2() {
//        BarangProcedure obj=  barangServiceProcedure.updateProcedure2();
//        System.out.println("response  1=" + obj.getResid());
//        System.out.println("response  =" + obj);
//    }
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public Barang2 barang2;




    @Test
    public void getByIdIbatis1() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
//            Map map = new HashMap<>();
//            map.put("rqnama", "%%");
//            List<BarangProsedure> list = session.selectList("scmt_manual.getListManual", map);
            System.out.println("response  1=" + barang2.selectBlog(2));
        } finally {
            session.close();
        }
    }

    @Test
    public void getListIbatis1() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            System.out.println("response  1=" + barang2.selectList("%bar%"));
            List<BarangProsedure> list = barang2.selectList("%bar%");
            for (BarangProsedure data : list) {
                System.out.println("data ke ============== ");
                System.out.println("id=" + data.getResid());
                System.out.println("nama=" + data.getResnama());
                System.out.println("satuan=" + data.getRessatuan());
                System.out.println("harga=" + data.getResharga());
                System.out.println("stok=" + data.getResstok());
            }
        } finally {
            session.close();
        }
    }

    @Test
    public void insertIbatis1() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            barang2.insertProcedure(123, "barang 90", "pcs", 12, 0);
            System.out.println("resId =");
        } finally {
            session.close();
        }
    }

    @Test
    public void updateIbatis1() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            barang2.updateProcedure(123, "barang 90666 updated", "pcs", 12, 3);
            System.out.println("resId =");
        } finally {
            session.close();
        }
    }


    @Test
    public void deleteIbatis1() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            barang2.deleteProcedure(11);
            System.out.println("resId =");
        } finally {
            session.close();
        }
    }

//    ===================================================XML
    @Test
    public void getByIdIbatisXML() throws IOException {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Map map = new HashMap<>();
            map.put("rqnama", "%%");
            List<BarangProsedure> list = session.selectList("scmt_manual.getListManual", map);
            for (BarangProsedure data : list) {
                System.out.println("data ke ============== ");
                System.out.println("id=" + data.getResid());
                System.out.println("nama=" + data.getResnama());
                System.out.println("satuan=" + data.getRessatuan());
                System.out.println("harga=" + data.getResharga());
                System.out.println("stok=" + data.getResstok());
            }
        } finally {
            session.close();
        }
    }

    @Test//gagal
    public void updateWithXML() {
        SqlSession session = sqlSessionFactory.openSession();

        try {
            Map map = new HashMap<>();
            map.put("rqharga",23);
            map.put("rqnama","svsv");
            map.put("rqsatuan","pcs");
            map.put("rqstok",239);
            map.put("resid",4);
            session.update("scmt_manual.updatebarangxml", map);
            System.out.println("data response ="+map.get("resid").toString());
            session.commit();
        } finally {
            session.close();
        }
    }

}
