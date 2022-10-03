package com.test.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.test.model.Fair;
import com.test.model.User;
import com.test.repository.UserRepository;
import com.test.service.FairService;






@Controller
public class MainController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FairService fairService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	


	//로그인 페이지
	@RequestMapping("/loginForm")
	public String loginForm() {
		return "/loginForm";
	}
		
	//회원가입 폼페이지
	@RequestMapping("/joinForm")
	public String joinForm() {
		return "/joinForm";
	}
	//회원가입 처리페이지
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(User user) {
		//System.out.println(user.toString());//제대로 콘솔에 나오는지 확인하려고함
		user.setRoll("ROLE_USER");
		//roll값을 ROLE_USER로 넣음
		
		String rawPassword=user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(rawPassword.toString());//암호화됐는지 확인해보기
		user.setPassword(encPassword);
		userRepository.save(user);		
		return "redirect:/loginForm";//redirect:이동하라는 의미 로그인폼으로 이동해라
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/form")
	public String form(Model mod) {
		UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("=========>"+user);
		User ur = userRepository.findByUsername(user.getUsername());
		mod.addAttribute("user",ur);
		return "form";
	}
	
	@RequestMapping(value = "/insert",method = RequestMethod.POST, consumes="multipart/form-data")
	public ResponseEntity<Fair> insert(Fair fair,@RequestParam("sdatee")String sdate,
			@RequestParam("edatee")String edate,
			@RequestParam("filek") MultipartFile[] file,
		Model mod) throws ParseException{
		
		//사진이름
		String imageFileName = file[0].getOriginalFilename();
		//사진경로
		String path ="D:\\springboot_nh\\test.zip_expanded\\test\\src\\main\\resources\\static\\image\\";
		
		//날짜확인
		System.out.println("++++++"+sdate);
		System.out.println("------"+edate);		
		
		//타입변환(String->Date)
		SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = formats.parse(sdate);
		Date datee = formats.parse(edate);
		
		System.out.println("---------------------------");
		System.out.println("start day"+dates);
		System.out.println("end day"+datee);
		
		//날짜 저장
		fair.setSdate(dates);
		fair.setEdate(datee);
		
		//사진이름
		System.out.println(imageFileName);
		
		//유저
		UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		System.out.println("=========>"+user);
		User ur = userRepository.findByUsername(user.getUsername());
		System.out.println("--------->"+ur.toString());
		
		//유저, 사진이름저장
		fair.setUname(ur);		
		fair.setFimg(imageFileName);
		
		//저장
		fairService.save(fair);
		System.out.println("======>>>"+fair);
				
		Path imaPath = Paths.get(path+imageFileName) ;		
		try {
			Files.write(imaPath, file[0].getBytes());
		}catch(Exception e) {
			
		}
		return new ResponseEntity<Fair>(fair, HttpStatus.CREATED);
	}
	
	

	
	//리스트띄우는거	
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String list(Model model,@PageableDefault(
			 sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable
	) {		
		System.out.println("=========>"+pageable);
		model.addAttribute("fairList",fairService.findFairList(pageable));		
			
		//유저있으면 사용, 없으면 에러발생하는 거
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//        String username = loggedInUser.getName();
		User us = userRepository.findByUsername(loggedInUser.getName());
		
		//유저디테일을 이용하여 유저정보를 가져옴 에러발생
//		UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
//		User ur = userRepository.findByUsername(user.getUsername());
		
		System.out.println("======="+us);
		model.addAttribute("username",us);
		return "main";
	}
	
	//1개 게시물 보게하는거
		@RequestMapping("/view")
		public String fair(Model model, //파람을 가져와서 글번호를 찍는것 기본값을 0 그것을 id로 받음
		@RequestParam(value = "id", defaultValue = "0")Long id) {	
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//	    String username = loggedInUser.getName();
		User us = userRepository.findByUsername(loggedInUser.getName());
			
		Fair fair =fairService.findFairById(id);//이렇게 되야함
		
		
		model.addAttribute("fair",fair);
		model.addAttribute("user",us);

		return "/viewform";	
		}
		
		//1개의 게시물에서 보고 수정하려고 폼으로 가는거
		@RequestMapping("/ud")
		public String ud(@RequestParam Long id,Model model) {
			Fair fa = fairService.findFairById(id);
			model.addAttribute("fair",fa);
			
			UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			User ur = userRepository.findByUsername(user.getUsername());
			model.addAttribute("user",ur);
			
			return "/form";
		}
		
		//수정처리하는거
		@RequestMapping(value = "/update/{id}",method = RequestMethod.PUT, consumes="multipart/form-data")
		public ResponseEntity<Fair> update(Fair fair,@RequestParam("sdatee")String sdate,
				@RequestParam("edatee")String edate,
				@RequestParam("filek") MultipartFile[] file,
			Model mod) throws ParseException{
			
			//사진이름
			String imageFileName = file[0].getOriginalFilename();
			//사진경로
			String path ="D:\\springboot_nh\\test.zip_expanded\\test\\src\\main\\resources\\static\\image\\";
			
			//날짜확인
			System.out.println("++++++"+sdate);
			System.out.println("------"+edate);		
			
			//타입변환(String->Date)
			SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
			Date dates = formats.parse(sdate);
			Date datee = formats.parse(edate);
			
			System.out.println("---------------------------");
			System.out.println("start day"+dates);
			System.out.println("end day"+datee);
			
			//날짜 저장
			fair.setSdate(dates);
			fair.setEdate(datee);
			
			//사진이름
			System.out.println(imageFileName);
			
			//유저
			UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
			System.out.println("=========>"+user);
			User ur = userRepository.findByUsername(user.getUsername());
			System.out.println("--------->"+ur.toString());
			
			//유저, 사진이름저장
			fair.setUname(ur);		
			fair.setFimg(imageFileName);
			
			//저장
			fairService.save(fair);
			System.out.println("======>>>"+fair);
					
			Path imaPath = Paths.get(path+imageFileName) ;		
			try {
				Files.write(imaPath, file[0].getBytes());
			}catch(Exception e) {
				
			}
			return new ResponseEntity<Fair>(fair, HttpStatus.CREATED);
		}
		
		//삭제처리하는거
		@RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
		public ResponseEntity<?> deleteFair(
				@PathVariable Long id){
			try {
			fairService.deleteById(id);
			}catch(Exception e){
				System.out.println("예외처리==>"+e);
			}
			return new ResponseEntity<>("{}", HttpStatus.OK);
		}
		
}

/*	@RequestMapping(value = "/map", method = RequestMethod.GET)
	public String map(Model model,@PageableDefault(
			 sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable
	) {		
		System.out.println("=========>"+pageable);
		model.addAttribute("fairList",fairService.findFairList(pageable));	
		
	return "/maptest";
	}*/