package com.binar.grab.repository;

import com.binar.grab.model.Barang;
import com.binar.grab.model.Mahasiswa;
import com.binar.grab.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository //step 2
public interface BarangRepository extends
        PagingAndSortingRepository<Barang, Long> {
    @Query("select c from Barang c")// nama class
    public Page<Barang> getAllData(Pageable pageable);

    @Query("select c from Barang c WHERE c.id = :idbarang")// titik dua menunjukkan parameter
    public Barang getbyID(@Param("idbarang") Long idbebas);

    @Query("select c from Barang c where c.nama= :nama")// nama class
    public Page<Barang> findByNama(String nama , Pageable pageable);

//    @Query("select c from Barang c where c.nama like :nama")// nama class

    // perhatikan lowernya
    public Page<Barang> findByNamaLike(String nama , Pageable pageable);

    public Barang findBySatuan(String satuan);

    public Barang findBySatuanAndHarga(String satuan, int harga);

    public Barang findBySatuanAndHargaOrNama(String satuan, int harga,  String nama);

    public Barang findBySatuanAndHargaOrNamaLike(String satuan, int harga,  String nama);// "'%%'"

    @Query("select c from Barang c where  c.harga BETWEEN :priceMin and :priceMax")// nama class
    Page<Barang> getDataByPrice( Double priceMin, Double priceMax, Pageable pageable);

    @Query("select c from Barang c where  c.harga BETWEEN :priceMin and :priceMax and c.nama like :nama")// nama class
    Page<Barang> getDataByPriceAndNama( Double priceMin, Double priceMax,String nama, Pageable pageable);


    // function or procedure postgres : sukses ini
    @Query( value = "select resid,  resnama,  resstok,  resharga,  ressatuan from public.getBarang1(:rqNama)", nativeQuery = true)
    List<Object> getBarang1(@Param("rqNama") String rqNama);

    //gagal
//    @Query( value = "select new com.binar.grab.dao.response.BarangProcedure(resid,  resnama,  resstok,  resharga,  ressatuan) from public.getBarang1(:rqNama)", nativeQuery = true)
//    List<BarangProcedure> getBarang2(@Param("rqNama") String rqNama);

    @Query(value = "CALL public.updatebarang(:harga1,:nama1,:satuan1,:stok1,:resid);", nativeQuery = true)
    Object updateBarangProcedure(@Param("harga1") Integer harga1,
                                 @Param("nama1") String nama1,
                                 @Param("satuan1") String satuan1,
                                 @Param("stok1") Integer stok1,
                                 @Param("resid") Integer resid);

//    ..gagal
//    @Query(value = "CALL public.updatebarang(:harga1,:nama1,:satuan1,:stok1,:resid);", nativeQuery = true)
//    BarangProcedure updateBarangProcedure2(@Param("harga1") Integer harga1,
//                                 @Param("nama1") String nama1,
//                                 @Param("satuan1") String satuan1,
//                                 @Param("stok1") Integer stok1,
//                                 @Param("resid") Integer resid);
}