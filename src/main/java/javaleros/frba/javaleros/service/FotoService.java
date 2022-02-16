package javaleros.frba.javaleros.service;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javaleros.frba.javaleros.models.Foto;
import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.repository.FotoRepository;

@Service
public class FotoService {
	
	@Autowired
    private FotoRepository fotoRepository;

    public Foto saveFile(MultipartFile file) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Foto foto = new Foto(fileName, file.getContentType(), file.getBytes());

            return fotoRepository.save(foto);
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    

    public Foto getFile(String fileId) {
        return fotoRepository.findById(fileId).orElseThrow();
    }

}
