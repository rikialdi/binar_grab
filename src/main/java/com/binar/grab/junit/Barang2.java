package com.binar.grab.junit;

import com.binar.grab.service.procedure.impl.BarangProsedure;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper//https://github.com/mybatis/spring-boot-starter
public interface Barang2 {
    @Select("SELECT resid,  resharga, resnama,  ressatuan, resstok FROM getByIDFunction(#{rqid});")
    BarangProsedure selectBlog(int rqid);

    @Select("select resid,resharga, resnama,  ressatuan,resstok from public.getBarang1(#{rqnama});")
    List<BarangProsedure> selectList(String rqnama);

    @Insert("call savebarang(#{rqharga,mode=IN},#{rqnama,mode=IN},#{rqsatuan,mode=IN},#{rqstok,mode=IN},#{resid,mode=INOUT})")
    void insertProcedure(int rqharga, String rqnama, String rqsatuan, int rqstok, int resid);

    @Update("call updatebarang(#{rqharga,mode=IN},#{rqnama,mode=IN},#{rqsatuan,mode=IN},#{rqstok,mode=IN},#{resid,mode=INOUT})")
    void updateProcedure(int rqharga, String rqnama, String rqsatuan, int rqstok, int resid);

    @Update("call deletebarang(#{rqid,mode=IN})")
    void deleteProcedure(int resid);

}
