package javaleros.frba.javaleros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javaleros.frba.javaleros.models.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, String>{
	
	//Foto saveLisFotoFile(List<MultipartFile> files) throws Exception;

}
