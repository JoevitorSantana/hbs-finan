package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

//    @PostMapping("/novo")
//    public ResponseEntity<Despesa> save(@PathVariable Long idCaixa, @RequestBody @Valid DespesaCreateDTO dto) {
//
//       // Despesa criaDespesa = despesaService.CriarDespesa(dto, idCaixa);
//        return ResponseEntity.ok(criaDespesa);
//    }



}
