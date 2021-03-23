/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.lang.annotation.Native;
import java.util.List;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 *
 * @author arti01
 */
public interface PaymentsRep extends Repository<Payments, Integer> {

    @Query("FROM Payments WHERE tytul like %?1%")
    List<Payments> test1(String firstName);

   @Query(value = "select count(*)  from payments_adr where id_agenta in(\n"
            + "	select id from payments where id in(\n"
            + "		select id from payments_adr where tytul like %?1%)\n"
            + "		)", nativeQuery = true)
    Integer test3(String a);
    
    @Query(value = "select count(pa1.id) from PaymentsAdr pa1 where pa1.idAgenta in(\n"
            + "	select p.id from Payments p where p.id in(\n"
            + "		select pa.id from PaymentsAdr pa where tytul like %?1%)\n"
            + "		)")
    Integer test6(String a);

    //@Query("from PaymentsAdr where id_agenta in(select Payments.id from Payments where id in(select PaymentsAdr.id from PaymentsAdr where tytul = 'l'))")
    @Query("SELECT pa1.id from PaymentsAdr pa1 where pa1.idAgenta in(Select p.id from Payments p where p.id in(SELECT pa.id From PaymentsAdr pa where pa.tytul like '%z%'))")
    List<Integer> test2();
    
    @Query(value = "SELECT pa1.id from payments_adr pa1 where pa1.id_Agenta in(Select p.id from payments p where p.id in(SELECT pa.id From Payments_Adr pa where pa.tytul like '%z%')) limit 500", nativeQuery = true)
    List<Integer> test4();
    @Query(value = "SELECT pa.id From Payments_Adr pa where pa.tytul like %?1% LIMIT 10", nativeQuery = true)
    List<Integer> test5(String a);

    List<Payments> findTop3000ByTytul(String a);

}
