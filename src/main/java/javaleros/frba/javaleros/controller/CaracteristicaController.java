package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.models.dto.CaracteristicaDto;
import javaleros.frba.javaleros.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}