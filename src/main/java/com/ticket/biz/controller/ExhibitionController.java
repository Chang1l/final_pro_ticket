package com.ticket.biz.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ticket.biz.common.PagingVO;
import com.ticket.biz.exhibition.ExhibitionService;
import com.ticket.biz.exhibition.ExhibitionVO;
import com.ticket.biz.good.GoodService;

@Controller
@SessionAttributes("exhibition")
public class ExhibitionController {

	
	@Autowired
	private ExhibitionService exhibitionService;
	
	
	// 전시 필터
//	@ModelAttribute("exSearchConditionMap")
//	public Map<String, String> exFilterConditionMap(){
//		Map<String, String> exFilterMap = new HashMap<String, String>();
//		exFilterMap.put("전시시작일자 내림차순", "EXH_ST_DATE");
//		exFilterMap.put("전시시작일자 오름차순", "EXH_ST_DATE");
//		exFilterMap.put("전시종료일자", "EXH_END_DATE");
//		exFilterMap.put("전시삭제여부", "EXH_CANCELL");
//		return exFilterMap;
//	}
	
	// 전시 검색
	@ModelAttribute("exSearchConditionMap")
	public Map<String, String> exSearchConditionMap(){
		Map<String, String> exConditionMap = new HashMap<String, String>();
		exConditionMap.put("전시명", "EXH_TITLE");
		exConditionMap.put("장소", "EXH_HALL");
	return exConditionMap;
	}

	// 전시 등록 이동
		@RequestMapping("/insertmoveExhibition")
		public String insertmoveExhibition(ExhibitionVO vo, Model model) {
//			model.addAttribute("exhibition", exhibitionService.getExhibition(vo));
			return "admin/ExhibitionInsert";
		}
	
	// 전시 등록
	@PostMapping("/insertExhibition")
	// public String insertBoard(MultipartHttpServletRequest request, ExhibitionVO vo) throws IllegalStateExcetion, IOException {
	public String insertExhibition(ExhibitionVO vo) throws IllegalStateException, IOException {
		MultipartFile uploadFile = vo.getUploadFile();
		// String realPath=request.getSession().getServletContext().getRealPath("/img/");
		String realPath = "c:/swork/tickets/src/main/webapp/img/";
		String fileName = uploadFile.getOriginalFilename();
		if (!uploadFile.isEmpty()) {
			vo.setExh_img(fileName);
			uploadFile.transferTo(new File(realPath + fileName));
		}
		exhibitionService.insertExhibition(vo);
		return "redirect:ExhibitionList";
	}

	// 전시 수정
	@RequestMapping("/updateExhibition")
	public String updateExhibition(@ModelAttribute("exhibition") ExhibitionVO vo, HttpSession session) {
		exhibitionService.insertExhibition(vo);
		return "redirect:getExhibitionList";
	}

	// 전시 삭제
	@RequestMapping("/deleteExhibition")
	public String deleteExhibition(ExhibitionVO vo, HttpSession session,HttpServletRequest request) {
//		String realPath = "c:/swork/tickets/src/main/webapp/img/";
//		vo = exhibitionService.getExhibition(vo);
//		if (vo.getExh_img() != null) {
//			System.out.println("파일삭제 : " + realPath + vo.getExh_img());
//			File f = new File(realPath + vo.getExh_img());
//			f.delete();
//		}
		String page= request.getParameter("page");
		exhibitionService.deleteExhibition(vo);
		System.out.println("page :"+page);
		return "redirect:getExhibitionList?nowPageBtn="+page;
	}

	// 전시 상세 조회
	@RequestMapping("/getExhibition")
	public String getExhibition(ExhibitionVO vo, Model model, HttpServletRequest request) {
		String page= request.getParameter("page");
		System.out.println("page : "+page);
		model.addAttribute("page",page);
		model.addAttribute("exhibition", exhibitionService.getExhibition(vo));
		return "admin/ExhibitionDetail";
	}
	
	// 전시 목록 조회
	@RequestMapping("/getExhibitionList")
	
	public String getExhibitionList(ExhibitionVO vo, String nowPageBtn, Model model) {
		System.out.println("컨트롤");
		
		
		//총 목록 수
				int totalPageCnt = exhibitionService.totalExhibitionListCnt(vo);
				//현재 페이지 설정
				int nowPage = Integer.parseInt(nowPageBtn==null || nowPageBtn.equals("") ? "1" :nowPageBtn);
				System.out.println("totalPageCnt: "+totalPageCnt +", nowPage: "+nowPage);
				//한페이지당 보여줄 목록 수
				int onePageCnt = 10;
				//한 번에 보여질 버튼 수
				int oneBtnCnt = 5;

				PagingVO pvo = new PagingVO(totalPageCnt, onePageCnt, nowPage, oneBtnCnt);
				vo.setOffset(pvo.getOffset());

				model.addAttribute("paging", pvo);
				model.addAttribute("exhibitionList", exhibitionService.getExhibitionList(vo));
				System.out.println("컨트롤러 완료");
				return "admin/ExhibitionList";
	}
	
	// 유저 전시 상세 조회
		@RequestMapping("/getUserExhibition")
		public String getUserExhibition(ExhibitionVO vo, Model model) {
			model.addAttribute("exhibition", exhibitionService.getExhibition(vo));
			return "exhibition/UserExhibitionDetail";
		}
	
		
	// 유저 전시 목록 조회
		@RequestMapping("/getUserExhibitionList")
		public String getUserExhibitionList(ExhibitionVO vo, String nowPageBtn, Model model) {
			//총 목록 수
					int totalPageCnt = exhibitionService.totalUSerExhibitionListCnt(vo);
					//현재 페이지 설정
					int nowPage = Integer.parseInt(nowPageBtn==null || nowPageBtn.equals("") ? "1" :nowPageBtn);
					System.out.println("totalPageCnt: "+totalPageCnt +", nowPage: "+nowPage);
					//한페이지당 보여줄 목록 수
					int onePageCnt = 10;
					//한 번에 보여질 버튼 수
					int oneBtnCnt = 5;

					PagingVO pvo = new PagingVO(totalPageCnt, onePageCnt, nowPage, oneBtnCnt);
					vo.setOffset(pvo.getOffset());
					
					
					model.addAttribute("paging", pvo);
					model.addAttribute("UserExhibitionList", exhibitionService.getUserExhibitionList(vo));
					
					return "exhibition/getUserExhibitionList";
		}
}
