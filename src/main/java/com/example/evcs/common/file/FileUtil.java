package com.example.evcs.common.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtil {

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
