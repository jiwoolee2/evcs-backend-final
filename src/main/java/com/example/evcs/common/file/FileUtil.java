package com.example.evcs.common.file;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.exception.InsertFileException;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {
	
//	private final Path fileLocation;
//	
//	public FileUtil (String fileLoad) {
//		//                  파일의 경로를 가져 fileLoad 를 가리키는 경로를 만들고 , 절대경로로 바꿔준 후, 경로 안에 "..", "." 같은 걸 깔끔하게 정리해준다.
//		this.fileLocation = Paths.get(fileLoad).toAbsolutePath().normalize();
//	}
//	
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
//	


	// 파일 저장할 때 이름 바꿔주는 메서드
	private String makeRandomName (String originalFileName) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("evcs_");
		
		String crrentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		sb.append(crrentTime);
		
		sb.append("_");
		
		int rNum = (int)(Math.random()*900) + 100;
		
		sb.append(rNum);
		
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		sb.append(ext);
		
		log.info("바뀐이름 나오는지 test : FileUtile class 바뀐 파일 이름 : {}", sb.toString());
		
		String changeFileName = sb.toString();
		
		return changeFileName;
	}

}
