package org.jcg.springboot.aws.s3.serv;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

	void uploadFile(MultipartFile multipartFile,String strName);
}
