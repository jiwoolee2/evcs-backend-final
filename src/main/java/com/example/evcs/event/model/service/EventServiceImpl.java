package com.example.evcs.event.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.evcs.auth.model.vo.CustomUserDetails;
import com.example.evcs.auth.service.AuthService;
import com.example.evcs.common.board.BoardUtil;
import com.example.evcs.common.board.PageInfo;
import com.example.evcs.common.board.Pagination;
import com.example.evcs.common.file.FileUtil;
import com.example.evcs.common.model.service.S3Service;
import com.example.evcs.common.model.service.S3ServiceImpl;
import com.example.evcs.event.model.dao.EventMapper;
import com.example.evcs.event.model.dto.EventDTO;
import com.example.evcs.event.model.vo.Event;
import com.example.evcs.exception.NoFileException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final S3Service s3Service;
	private final EventMapper eventMapper;
	private final AuthService authService;
	private final BoardUtil boardUtil;
	private final FileUtil fileUtil;
	private String fileLocation = "event-image";
	
	@Override
	public void insertEvent(EventDTO event, MultipartFile file) {
		
		CustomUserDetails user = authService.getUserDetails();
		Long memberNo = user.getMemberNo();

		Event requestData = null;
		
		Map<String, String> utilMap = boardUtil.validateContentAndTitle(event.getEventName(), event.getEventContent());
		
		requestData = Event.builder()
				.memberNo(memberNo)
				.eventName(utilMap.get("title"))
				.startDate(event.getStartDate())
				.endDate(event.getEndDate())
				.eventContent(utilMap.get("content"))
				.build();

		eventMapper.insertEvent(requestData);
		
		if(file != null && !file.isEmpty()) {
			String filePath = s3Service.uploadFile(file,fileLocation);

			eventMapper.insertEventFile(filePath);
			
		} else {
			throw new NoFileException("EventService : 이벤트 사진이 없습니다."); 
		}
	}
	
	@Override
	public Map<String, Object> selctEventAll(Map<String, String> map) {
		int size = 10;
		Map<String, Object> returnMap = new HashMap();
		
		int count = eventMapper.selectTotalCount(map);
		
		RowBounds rowBounds = new RowBounds(Integer.parseInt(map.get("page")) * size, size);
		
		PageInfo pi = Pagination.getPageInfo(count, Integer.parseInt(map.get("page")) +1, 10, 5);

		// 특수문자 이스케이프 처리
		map.put("searchKeyword", boardUtil.escapeLikeParam(map.get("searchKeyword")));
		map.put("ingCategory", boardUtil.escapeLikeParam(map.get("ingCategory")));
		List<EventDTO> list = new ArrayList();
		list = eventMapper.selctEventAll(map, rowBounds);
		
		log.info("eventList : {}", list);
		log.info("pageInfo : {}", pi);
		returnMap.put("eventList", list);
		returnMap.put("pageInfo", pi);
		
		return returnMap;
	}

	@Override
	public EventDTO selectByEventNo(Long eventNo) {
		return eventMapper.selectByEventNo(eventNo);
	}

	@Override
	public EventDTO updateEvent(EventDTO event, MultipartFile file) {
		
		CustomUserDetails user = authService.getUserDetails();
		Long memberNo = user.getMemberNo();

		Event requestData = null;
		
		Map<String, String> utilMap = boardUtil.validateContentAndTitle(event.getEventName(), event.getEventContent());
		
		requestData = Event.builder()
				.memberNo(memberNo)
				.eventName(utilMap.get("title"))
				.startDate(event.getStartDate())
				.endDate(event.getEndDate())
				.eventContent(utilMap.get("content"))
				.eventNo(event.getEventNo())
				.build();

		eventMapper.updateEvent(requestData);
		
		
		if(file != null && !file.isEmpty()) {
			Map<String, String> map = new HashMap<String, String>();
			String filePath = s3Service.uploadFile(file,fileLocation);
			
			map.put("filePath", filePath);
			map.put("eventNo", String.valueOf(event.getEventNo()));

			eventMapper.updateEventFile(map);
			
		} 
		return null;
	}

	@Override
	public void deleteByEventNo(Long eventNo) {
		eventMapper.deleteByEventNo(eventNo);
	}
	
	
	@Override
	public Map<String, Object> selctEventAllUser(int page) {
		int size = 10;
		Map<String, Object> returnMap = new HashMap();
		
		int count = eventMapper.selectTotalCountUser();
		
		RowBounds rowBounds = new RowBounds(page * size, size);
		
		PageInfo pi = Pagination.getPageInfo(count, page +1, 10, 5);

		List<EventDTO> list = new ArrayList();
		list = eventMapper.selctEventAllUser(rowBounds);
		
		log.info("eventList : {}", list);
		log.info("pageInfo : {}", pi);
		returnMap.put("eventList", list);
		returnMap.put("pageInfo", pi);
		
		return returnMap;
	}


}
