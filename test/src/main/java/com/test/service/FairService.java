package com.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.model.Fair;
import com.test.repository.FairRepository;

@Service
public class FairService {
	
	private final FairRepository fairRepository;
	
	public FairService(FairRepository fairRepository) {
		this.fairRepository=fairRepository;
	}
	
	public void save(Fair fair) {
		fairRepository.save(fair);
	}
	
	public Fair findFairById(Long id) {
		return fairRepository.findById(id).orElse(new Fair());
	}
	
	public Page<Fair> findFairList(Pageable pageable){
		pageable =PageRequest.of(pageable.getPageNumber()<=0?0:pageable.getPageNumber()-1
				,pageable.getPageSize(), pageable.getSort());
			//삼항연산자를 이용 /페이지번호,페이지사이즈,페이지 정렬방식
			//시작페이지가 1이여야함으로 컴터가 인식하도록 첫페이지를 0으로 해주는 삼항연산
			//0일경우 0으로 두고 아니면 -1을 해주자.		
			return fairRepository.findAll(pageable);	
	}
	
	public void deleteById(Long id) {
		fairRepository.deleteById(id);
	}
}
