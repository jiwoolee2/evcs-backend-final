package com.example.evcs.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.exception.InsertFileException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtil {
	

//	public String saveFile(MultipartFile file) {
//		
//		String originalFileName = file.getOriginalFilename();
//		
//		// 파일 이름 변경
//		String changeFileName = makeRandomName(originalFileName);
//		
//		// 파일경로에 이름을 붙여줌
//		Path targetLocation = this.fileLocation.resolve(changeFileName);
//		log.info("targetLocation : {}",targetLocation);
//		try {
//			// 파일을 저장함 inputStream 만들어주고, 저장할 경로 입력, 혹시 이름이 같으면 덮어씌운다.
//			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//			// 경로 마지막 짤라서 뒤에 부분에 맞게 리턴
//			String fullPath = String.valueOf(this.fileLocation);
//			String parentPath = fullPath.substring(fullPath.lastIndexOf("\\") + 1);
//			if("event".equals(parentPath)) {
//				return "http://localhost/uploads/event/" + changeFileName;
//			}
//			if("car".equals(parentPath)) {
//				return "http://localhost/uploads/car/" + changeFileName;
//			}
//			
//			if("board".equals(parentPath)) {
//				return "http://localhost/uploads/board/" + changeFileName;
//			}
//			
//			if("driveRoute".equals(parentPath)) {
//				return "http://localhost/uploads/driveRoute/" + changeFileName;
//			}
//			
//			return null;
//		} catch (IOException e) {
//			throw new InsertFileException("FileUtil : 파일 생성중 에러");
//		}
//	}
	

	// 파일 저장할 때 이름 바꿔주는 메서드
	public String makeRandomName (String originalFileName) {
		
		StringBuilder sb = new StringBuilder();
		
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		int rNum = (int)(Math.random()*900) + 100;
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		sb.append("evcs_");
		sb.append(currentTime);
		sb.append("_");
		sb.append(rNum);
		sb.append(ext);
		
		log.info("바뀐이름 나오는지 test : FileUtile class 바뀐 파일 이름 : {}", sb.toString());
		String changeFileName = sb.toString();
		
		return changeFileName;
	}

}
