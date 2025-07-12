package com.example.evcs.admin.rentCarInfo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.example.evcs.admin.carInfo.model.dao.CarInfoMapper;
import com.example.evcs.admin.carInfo.model.dto.CarCompanyDTO;
import com.example.evcs.admin.carInfo.model.dto.CarImageDTO;
import com.example.evcs.admin.carInfo.model.dto.CarInfoDTO;
import com.example.evcs.admin.carInfo.model.dto.CarTypeDTO;
import com.example.evcs.admin.rentCarInfo.model.dao.RentCarInfoMapper;
import com.example.evcs.admin.rentCarInfo.model.dto.CategoryDTO;
import com.example.evcs.admin.rentCarInfo.model.dto.RentCarInfoDTO;
import com.example.evcs.admin.rentCarInfo.model.dto.RentCarOptionDTO;
import com.example.evcs.admin.rentCarInfo.model.vo.RentCarInfoVO;
import com.example.evcs.admin.rentCarInfo.model.vo.RentCarOption;
import com.example.evcs.util.model.dto.PageInfo;
import com.example.evcs.util.template.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RentCarInfoServiceImpl implements RentCarInfoService {

	private final RentCarInfoMapper rentCarInfoMapper;
	private final CarInfoMapper carInfoMapper;
	
	@Override
	public Map<String, Object> getRentCarCategory() {
		Map<String, Object> returnMap = new HashMap();
		
		List<CategoryDTO> categoryInfo =  rentCarInfoMapper.getRentCarCategory();
		List<CarCompanyDTO> companyInfo =  rentCarInfoMapper.getCompanyInfo();
		List<CarTypeDTO> carTypeInfo =  rentCarInfoMapper.getCarTypeInfo();
		
		log.info("categoryInfo : {}", categoryInfo);
		log.info("companyInfo : {}", companyInfo);
		log.info("carTypeInfo : {}", carTypeInfo);
		returnMap.put("categoryInfo", categoryInfo);
		returnMap.put("companyInfo", companyInfo);
		returnMap.put("carTypeInfo", carTypeInfo);
		
		return returnMap;
		
	}
	

	@Override
	public Map<String, Object> getAllCarInfo() {
		
		Map<String, Object> result = new HashMap();
		
		List<CarInfoDTO> carInfoResult = carInfoMapper.getAllCarInfo();
		List<CarImageDTO> imageResult = carInfoMapper.getAllCarImage();
		
		result.put("carInfoResult", carInfoResult);
		result.put("imageResult",imageResult);
		
		log.info("123123123123 : {}",imageResult);
		return result;
	}

	
	
	@Override
	public void insertRentCar(RentCarInfoDTO rentCarInfo) {
		
		/*
		 * 1. carNo으로 존재하는 렌트카인지 확인
		 * 
		 * 2. 존재한다면 렌터카 추가하기(categoryName으로 categoryNo 얻어와야함)
		 * 
		 * 3. 존재하지 않는다면 예외처리
		 */
		
		int carNo = rentCarInfo.getCarNo();

		int countCar = rentCarInfoMapper.findByCarNo(carNo);
		
		if(countCar != 0) {
			
			log.info("categoryName : {}",rentCarInfo.getCategoryNo());
			int categoryNo = rentCarInfoMapper.findCategoryNoByCategoryName(rentCarInfo.getCategoryNo());
			log.info("categoryNo : {}",categoryNo);
			RentCarInfoVO rentCarInfoVo = RentCarInfoVO.builder()
											.rentCarNo(rentCarInfo.getRentCarNo())
											.categoryNo(categoryNo)
											.carNo(carNo)
											.rentCarPrice(rentCarInfo.getRentCarPrice())
											.garageNo(rentCarInfo.getGarageNo())
											.build();
			
			rentCarInfoMapper.insertRentCar(rentCarInfoVo);
			
			if (rentCarInfo.getOptionNos() != null && !rentCarInfo.getOptionNos().isEmpty()) {
				RentCarOption rentCarOptionVo = RentCarOption.builder()
						.rentCarNo(rentCarInfo.getRentCarNo())
						.optionNos(rentCarInfo.getOptionNos())
						.build();
				
			    rentCarInfoMapper.insertCarOptions(rentCarOptionVo);
			}
		}
	}


	@Override
	public Map<String, Object> rentCarList(Map<String, String> map) {
		
		Map<String, Object> returnmap = new HashMap();
		
		int boardNoPerPage = 10;
		int pageSize = 5;
		int currentPage = Integer.parseInt(map.get("currentPage"));
		
		RowBounds rowBounds = new RowBounds((currentPage-1)*boardNoPerPage,boardNoPerPage);
		int totalBoardNo = rentCarInfoMapper.countAllRentCar();
		
		PageInfo pageInfo = Pagination.getPageInfo(currentPage,  pageSize, boardNoPerPage,totalBoardNo);
		
		List<RentCarInfoDTO> rentCarInfo = rentCarInfoMapper.getRentCarList(map, rowBounds);
		
		List<CarInfoDTO> carInfo = rentCarInfoMapper.getCarList(rowBounds);
		
		log.info("carInfo : {}",carInfo);
		log.info("rentCarInfo : {}",rentCarInfo);
		log.info("pageInfo : {}",pageInfo);
				
		returnmap.put("pageInfo", pageInfo);
		returnmap.put("rentCarInfo", rentCarInfo);
		returnmap.put("carInfo", carInfo);
		
		return returnmap;
	}


	@Override
	public void updateRentCar(RentCarInfoDTO rentCarInfo) {
		
		/*
		 * 1. 존재하는 렌터카인지 확인
		 * 
		 * 2. 존재한다면 수정(categoryNo 얻어와야함)
		 * 
		 * 3. 존재하지 않는다면 예외처리
		 */
		
		
		
		String rentCarNo = rentCarInfo.getRentCarNo();
		
		int countRentCar = rentCarInfoMapper.findByRentCarNo(rentCarNo);
		
		if(countRentCar == 1) {
			int categoryNo = rentCarInfoMapper.findCategoryNoByCategoryName(rentCarInfo.getCategoryNo());
			
			RentCarInfoVO rentCarInfoVo = RentCarInfoVO.builder()
					.rentCarNo(rentCarInfo.getRentCarNo())
					.categoryNo(categoryNo)
					.carNo(rentCarInfo.getCarNo())
					.rentCarPrice(rentCarInfo.getRentCarPrice())
					.garageNo(rentCarInfo.getGarageNo())
					.status(rentCarInfo.getStatus())
					.build();
			log.info("rentCarInfoVo : {}",rentCarInfoVo);
			rentCarInfoMapper.updateRentCar(rentCarInfoVo);
			
			rentCarInfoMapper.deleteCarOptions(rentCarInfo.getRentCarNo());
			
		    if (rentCarInfo.getOptionNos() != null && !rentCarInfo.getOptionNos().isEmpty()) {
		        RentCarOption rentCarOptionVo = RentCarOption.builder()
		            .rentCarNo(rentCarInfo.getRentCarNo())
		            .optionNos(rentCarInfo.getOptionNos())
		            .build();

		        rentCarInfoMapper.insertCarOptions(rentCarOptionVo);
		    }
		}
	}


	@Override
	public void deleteRentCar(RentCarInfoDTO rentCarInfo) {

		String rentCarNo = rentCarInfo.getRentCarNo();
		
		int countRentCar = rentCarInfoMapper.findByRentCarNo(rentCarNo);
		
		if(countRentCar == 1) {
			
			rentCarInfoMapper.deleteRentCar(rentCarNo);
		}
	}


	@Override
	public Map<String, Object> getTimeRentCarInfo(Map<String,Object> dateMap) {
		Map<String, Object> result = new HashMap();
		
		log.info("atemap::: {}",dateMap);
		
		List<RentCarInfoDTO> timeRentCarResult = rentCarInfoMapper.getTimeRentCarInfo(dateMap);
		
		log.info("시간별 렌트카 목록 : {}",timeRentCarResult);
		result.put("timeRentCarResult", timeRentCarResult);
		
		return result;
	}


	@Override
	public List<RentCarOptionDTO> getOptions() {
		List<RentCarOptionDTO> optionList = rentCarInfoMapper.getOptions();
		return optionList;
	}
	
	@Override
	public List<RentCarOptionDTO> getRentCarOptions(String rentCarNo) {
		log.info("??? 머임?? {}" , rentCarNo);
		List<RentCarOptionDTO> optionList = rentCarInfoMapper.getRentCarOptions(rentCarNo);
		return optionList;
	}
}
