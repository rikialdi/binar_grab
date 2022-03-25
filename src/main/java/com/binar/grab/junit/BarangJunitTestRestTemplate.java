package com.binar.grab.junit;

import com.binar.grab.model.Barang;
import com.binar.grab.repository.BarangRepository;
import com.binar.grab.service.BarangRestTemplateService;
import com.binar.grab.service.email.email.EmailService;
import com.binar.grab.service.email.email.MailRequest;
import com.binar.grab.service.email.email.MailResponse;
import com.binar.grab.util.SimpleStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public BarangRepository  barangRepository;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();


    @Autowired
    private EmailService service;

    @Test
    public void sendEmail( ) {
        MailRequest request = new MailRequest();
        request.setName("Invoice Test name");
        request.setTo("rikialdipari@gmail.com");
        request.setFrom("rikialdipari@gmail.com");
        request.setSubject("Invoice Test");
        Map<String, Object> model = new HashMap<>();
        model.put("namesaya", "riki aldi pari");

        model.put("chuteicon", "https://binar-test.herokuapp.com/api/showFile/2432022025921Captureass.PNG");
        BigDecimal total= new BigDecimal(0);

        Pageable show_data = simpleStringUtils.getShort("id", "desc", 0, 1);
        Page<Barang> data = barangRepository.getAllData(show_data);


        model.put("datainvoice", data.getContent());
        model.put("iconuser","https://binar-test.herokuapp.com/api/showFile/2432022025921Captureass.PNG") ;
        MailResponse ma =  service.sendEmail(request, model);
        System.out.println("MailResponse 1="+ma.getMessage());

    }


}
