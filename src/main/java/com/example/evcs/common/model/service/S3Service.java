package com.example.evcs.common.model.service;


import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

	public String getFileName(MultipartFile file, String fileLocation);
	
	public String uploadFile(MultipartFile file, String fileLocation);
		
	public void deleteFile(String fileUrl);
		
	
}
