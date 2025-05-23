//package com.hbs.hbsfinan.controller;
//
//import com.hbs.hbsfinan.dto.RestResponseMessage;
//import com.hbs.hbsfinan.model.DoacaoAlimenticia;
//import com.hbs.hbsfinan.service.DoacaoAlimenticiaService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/doacaoAli")
//public class DoacaoAlimenticiaController {
//    @Autowired
//    DoacaoAlimenticiaService doacaoAlimenticiaService;
//
//        @PostMapping("/novo")
//        public ResponseEntity save(@Valid @RequestBody DoacaoAlimenticia doacaoAlimenticia){
//            try
//            {
//                doacaoAlimenticiaService.save(doacaoAlimenticia);
//                RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Doacao registrada com sucesso!");
//                return new ResponseEntity<>(message, HttpStatus.CREATED);
//            }
//            catch (Exception e)
//            {
//                System.err.println("Erro ao salvar doção: " + e.getMessage());
//                throw new RuntimeException();
//            }
//        }
//
//        @GetMapping("/listar")
//        public ResponseEntity <List<DoacaoAlimenticia>> findAll()
//        {
//            try
//            {
//                return ResponseEntity.ok(doacaoAlimenticiaService.findAll());
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return ResponseEntity.badRequest().build();
//        }
//
//        @PutMapping("/editar/{id}")
//        public ResponseEntity update(@PathVariable int id, @RequestBody DoacaoAlimenticia doacaoAlimenticia )//Pq a data esta 1 dia a menos?
//        {
//            try
//            {
//                DoacaoAlimenticia oldDoacaoAlimenticia = doacaoAlimenticiaService.findById(id);
//                if(doacaoAlimenticia.getData_Doacao()!=null)
//                    oldDoacaoAlimenticia.setData_Doacao(doacaoAlimenticia.getData_Doacao());
//
//
//                doacaoAlimenticiaService.update(oldDoacaoAlimenticia);
//                RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Doacao atualizado com sucesso!");
//                return new ResponseEntity<>(message, HttpStatus.OK);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return ResponseEntity.badRequest().build();
//        }
//
//        @GetMapping("/{id}")
//        public ResponseEntity<DoacaoAlimenticia>findById(@PathVariable int id){
//            try
//            {
//                DoacaoAlimenticia doacaoAlimenticia = doacaoAlimenticiaService.findById(id);
//                return ResponseEntity.ok(doacaoAlimenticia);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return ResponseEntity.badRequest().build();
//        }
//
//
//
//        @DeleteMapping("/excluir/{id}")
//        public ResponseEntity delete(@PathVariable int id)
//        {
//            try
//            {
//                doacaoAlimenticiaService.findById(id);
//                doacaoAlimenticiaService.delete(id);
//                RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento excluido com sucesso!");
//                return new ResponseEntity<>(message, HttpStatus.OK);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return ResponseEntity.badRequest().build();
//        }
//}
//
