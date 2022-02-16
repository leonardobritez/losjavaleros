package javaleros.frba.javaleros.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javaleros.frba.javaleros.models.Foto;
import javaleros.frba.javaleros.models.UploadFileResponse;
import javaleros.frba.javaleros.service.FotoService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;




@RequestMapping("/fotocontroller")
@RestController
public class FotoController {
	
	private static final Logger logger = LoggerFactory.getLogger(FotoController.class);

    @Autowired
    private FotoService fotoStorageService;

    @SuppressWarnings("finally")
	@PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
         Foto dbFile = null;
         String fileDownloadUri = null;
         
		try {
			
			dbFile = fotoStorageService.saveFile(file);
			 fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/downloadFile/")
					.path(String.valueOf(dbFile.getId()))
					.toUriString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
			
		}

    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
    	Foto dbFile = fotoStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

}
