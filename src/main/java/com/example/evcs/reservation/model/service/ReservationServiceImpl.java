package com.example.evcs.reservation.model.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.example.evcs.admin.hotdeal.model.dto.HotdealDTO;
import com.example.evcs.admin.rentCarInfo.model.dto.RentCarInfoDTO;
import com.example.evcs.auth.model.vo.CustomUserDetails;
import com.example.evcs.auth.service.AuthService;
import com.example.evcs.common.board.PageInfo;
import com.example.evcs.common.board.Pagination;
import com.example.evcs.reservation.model.dao.ReservationMapper;
import com.example.evcs.reservation.model.dto.ReservationDTO;
import com.example.evcs.reservation.model.dto.ResponseDTO;
import com.example.evcs.reservation.model.dto.TossPaymentsDTO;
import com.example.evcs.reservation.model.vo.Reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationMapper reservationMapper;
	private final AuthService authService;
	
    @Override
    public ResponseDTO createReservation(ReservationDTO dto) {
    	
    	ResponseDTO res = new ResponseDTO();
		CustomUserDetails user = authService.getUserDetails();
		Long memberNo = user.getMemberNo();
		int amount = 0;
		int months = 0;
		
		// 핫딜 하는지 안하는지 확인
		HotdealDTO hotdeal = reservationMapper.selectHotdealDealPercent(dto.getRentCarNo());
		String period = dto.getSelectedPeriod();
		
		double dealPercent = 0.0;
		
		if("24개월".equals(period)) {
			dealPercent += 0.05;
			months += 24;
		}else if("30개월".equals(period)) {
			dealPercent += 0.1;
			months += 30;
		}
		if(hotdeal != null) {
			dealPercent += hotdeal.getDealPercent()/100.0;
		}
		log.info("hotdeal : {}" , hotdeal );
		// 렌트카 가격 가져오기
		RentCarInfoDTO rentCar = reservationMapper.selectRentCarInfo(dto.getRentCarNo());
		int rentCarPrice = rentCar.getRentCarPrice();
		
		if(period == null) {
			rentCarPrice = dto.getAmount();
		}
		
		log.info("rentCarPrice : {}", rentCarPrice);
		log.info("dealPercent : {}", dealPercent);
		if(months != 0) {
			amount = (int)(Math.round(rentCarPrice * (1 - dealPercent)/months)) ;
		} else {
			amount = (int)(Math.round(rentCarPrice * (1 - dealPercent))) ;
		}
		log.info("amount : {}", amount);
		log.info("dto.getRentalTime() : {}", dto.getRentalTime());
		log.info("dto.getReturnTime() : {}", dto.getReturnTime());
		
    	
        Reservation vo = Reservation.builder()
                .memberNo(memberNo)
                .rentCarNo(dto.getRentCarNo())
                .amount(amount)
                .paymentsStatus("PENDING")
                .expireAt(LocalDateTime.now().plusMinutes(10))    
                .orderId(UUID.randomUUID().toString())
                .rentalTime(dto.getRentalTime())
                .returnTime(dto.getReturnTime())
                .build();
        
        reservationMapper.createReservation(vo);
        
        res.setAmount(amount);
        res.setOrderId(vo.getOrderId());
        res.setMemberNo(memberNo);
        
        return res;
    }

    /**
     * 주문번호로 예약 조회
     */
    @Override
    public ReservationDTO selectByOrderId(String orderId) {
        return reservationMapper.selectByOrderId(orderId);
    }

    /**
     * 결제 완료 처리
     */
    @Override
    public void setCompleted(Long revNo) {
        reservationMapper.updateReservationToCompleted(revNo);
    }
    
    public int findRentCarPrice(String rentCarNo) {
    	
    	return reservationMapper.findRentCarPrice(rentCarNo);
    }

	@Override
	public void updateReservationFailed(Long reservationNo) {
		reservationMapper.updateReservationFailed(reservationNo);
	}

	@Override
	public Map<String, Object> selectAllReservation(Map<String, String> map) {
		int size = 10;
		Map<String, Object> returnMap = new HashMap();
		
		int count = reservationMapper.selectReservationTotalCount(map);
		
		RowBounds rowBounds = new RowBounds(Integer.parseInt(map.get("page")) * size, size);
		
		PageInfo pi = Pagination.getPageInfo(count, Integer.parseInt(map.get("page")) +1, 10, 5);

		// 특수문자 이스케이프 처리
//		map.put("searchKeyword", boardUtil.escapeLikeParam(map.get("searchKeyword")));
//		map.put("ingCategory", boardUtil.escapeLikeParam(map.get("ingCategory")));
		List<ReservationDTO> list = new ArrayList();
		list = reservationMapper.selectAllReservation(map, rowBounds);
		
		log.info("reservationList : {}", list);
		log.info("pageInfo : {}", pi);
		returnMap.put("reservationList", list);
		returnMap.put("pageInfo", pi);
		
		return returnMap;
	}


}