package com.binar.grab.repository;

import com.binar.grab.model.Barang;
import com.binar.grab.model.Mahasiswa;
import com.binar.grab.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository //step 2
public interface BarangRepository extends
        PagingAndSortingRepository<Barang, Long> {
    @Query("select c from Barang c")// nama class
    Page<Barang> getAllData(Pageable pageable);
}