package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.models.Caracteristica;
import javaleros.frba.javaleros.models.dto.CaracteristicaDto;
import javaleros.frba.javaleros.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/caracteristica")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @PostMapping("")
    public ResponseEntity agregarCatacteristica(@RequestBody CaracteristicaDto nuevaCaracteristica) throws Exception {
        try{
            caracteristicaService.agregarCaracteristica(nuevaCaracteristica);
        } catch(Exception e) {
            throw new Exception(e);
        }

        return new ResponseEntity("Caracteristica agregada",HttpStatus.CREATED);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity agregarCatacteristica(@PathVariable Integer id) throws Exception {
        try{
            caracteristicaService.borrarCaracteristica(id);
        } catch(Exception e) {
            throw new Exception(e);
        }

        return new ResponseEntity("Caracteristica Borrada",HttpStatus.NO_CONTENT);

    }

    @GetMapping("")
    public ResponseEntity getCaracteristicas() throws Exception {
        List<Caracteristica> caracteristicas  = caracteristicaService.traerCaracteristicas();

        return new ResponseEntity<>(caracteristicas,HttpStatus.OK);

    }
}