package org.jcg.springboot.aws.s3.ctrl;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcg.springboot.aws.s3.serv.AWSS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value= "/s3")
public class AWSS3Ctrl {

	@Autowired
	private AWSS3Service service;

	@PostMapping(value= "/upload")
	public ResponseEntity<Map<String, Object>>  uploadFile(@RequestPart(value= "file") final MultipartFile multipartFile ,@RequestPart(value= "strName") final String strName) {
		 HttpStatus httpStatus = HttpStatus.OK;
		 Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			service.uploadFile(multipartFile, strName);
			final String response = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";
        	retMap = new HashMap<String, Object>();
			retMap.put("STATUS", "successfully");
			retMap.put("DATA", strName );
        } catch (Exception e) {
			e.printStackTrace();   
			retMap = new HashMap<String, Object>();
			retMap.put("STATUS", "ERREO");
			retMap.put("DATA", "NO DATA FOUND" );
		}
		
			
		
		//return new ResponseEntity<>(response, HttpStatus.OK);
		return ResponseEntity
		        .status(httpStatus)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(retMap);
	}
	
	  // define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    // Define a method to upload files
    @PostMapping("/uploadListImage")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        for(MultipartFile file : multipartFiles) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }
        return ResponseEntity.ok().body(filenames);
    }
    
    
    
}
