/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author arti01
 */
@RestController
@RequestMapping("/url")
public class NewRestController {

    @Autowired
    private NewEntityRepository repository;
    @Autowired
    private PaymentsRep payRep;
    @Autowired
    private PaymentsAdrRep payAdrRep;

    @GetMapping()
    public List<Object> list() {
        return null;
    }

    @GetMapping("/u/{id}")
    public Object get1(@PathVariable String id) {
        Date ds = new Date();
        //System.err.println(payRep.findAll().iterator().next());
        //System.err.println(payRep.findById(payRep.findAll().iterator().next().getId()));
        //payAdrRep.findById(1);
        //System.err.println(payRep.findTop10().size());
        //System.err.println(payRep.findTop100ByTytul("rzzz").size());
        //System.err.println(payRep.test3(id));
        //System.err.println(payRep.test4().size());
        //System.err.println(payRep.test1("rzzz").size());
        //System.err.println(payRep.test3().size());
        //System.err.println(payRep.findZ(id).size());
        //System.err.println(payRep.test6(id));
        System.err.println(payRep.findTop3000ByTytul(id).size());
        for (int i = 0; i < 1; i++) {
        System.err.println(payRep.test6(id));
        //System.err.println(payRep.findTop3000ByTytul(id).size());
            //System.err.println(payRep.test4().size());
        }
        Date de = new Date();
        return id + "--" + (de.getTime() - ds.getTime()) + "---"; //+ payRep.li().size();
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable String id) {
        for (int i = 0; i < 2; i++) {
            NewEntity ne = new NewEntity();
            ne.setTekst("balala");
            ne.setId(new Long(555));
            repository.save(ne);
            repository.flush();
            repository.count();
            repository.delete(ne);
            repository.flush();
            repository.findAll();
        }
        System.err.println("rrrrrrrrrrrrr");
        return id + repository.count();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Object input) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Object input) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }

}
