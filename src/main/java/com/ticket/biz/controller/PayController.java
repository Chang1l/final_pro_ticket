package com.ticket.biz.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.biz.pay.PayService;
import com.ticket.biz.pay.PayVO;

@Controller
public class PayController {
	public static final String IMPORT_TOKEN_URL = "https://api.iamport.kr/users/getToken";
	public static final String IMPORT_PAYMENTINFO_URL = "https://api.iamport.kr/payments/find/";
	public static final String IMPORT_PAYMENTLIST_URL = "https://api.iamport.kr/payments/status/all";
	public static final String IMPORT_CANCEL_URL = "https://api.iamport.kr/payments/cancel";
	public static final String IMPORT_PREPARE_URL = "https://api.iamport.kr/payments/prepare";
	//"아임포트 Rest Api key로 설정";
	public static final String KEY = "6487055055077084";
	//"아임포트 Rest Api Secret로 설정"; 
	public static final String SECRET = "kRywHQTLmybRtsInkq7tGfHTJnJmNd0DaAP9aCcimZnrUrq17DBgyhvHi00KOKY8BOhiDBZ7G0ud9Xz1";  
	
	// 아임포트 인증(토큰)을 받아주는 함수 
	public String getImportToken() {
		String result = "";
		HttpClient client = HttpClientBuilder.create().build(); 
		HttpPost post = new HttpPost(IMPORT_TOKEN_URL); 
		Map<String,String> m =new HashMap<String,String>(); 
		m.put("imp_key", KEY);
		m.put("imp_secret", SECRET); 
		try { 
			post.setEntity( new UrlEncodedFormEntity(convertParameter(m)));
			HttpResponse res = client.execute(post); 
			ObjectMapper mapper = new ObjectMapper(); 
			String body = EntityUtils.toString(res.getEntity()); 
			JsonNode rootNode = mapper.readTree(body); 
			JsonNode resNode = rootNode.get("response"); 
			result = resNode.get("access_token").asText(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return result;
	}
	
	// Map을 사용해서 Http요청 파라미터를 만들어 주는 함수 private
	List<NameValuePair> convertParameter(Map<String,String> paramMap){
		List<NameValuePair> paramList = new ArrayList<NameValuePair>(); 
		Set<Entry<String,String>> entries = paramMap.entrySet();
		for(Entry<String,String> entry : entries) {
		 paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
		} 
		return paramList; 
	} 
	
	// 아임포트 결제금액 변조는 방지하는 함수 
		 public void setHackCheck(String amount,String mId,String token) { 
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(IMPORT_PREPARE_URL);
			Map<String,String> m =new HashMap<String,String>();
			post.setHeader("Authorization", token);
			m.put("amount", amount); 
			m.put("merchant_uid", mId);
			try { 
				post.setEntity(new UrlEncodedFormEntity(convertParameter(m)));
				HttpResponse res = client.execute(post);
				ObjectMapper mapper = new ObjectMapper();
				String body = EntityUtils.toString(res.getEntity());
				JsonNode rootNode = mapper.readTree(body);
				System.out.println(rootNode); 
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
		} 
	 
	// 결제취소
	@RequestMapping(value="/paycan" , method = RequestMethod.POST)
	@ResponseBody
	public int cancelPayment(String mid) {
		String token = getImportToken();
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(IMPORT_CANCEL_URL); 
		Map<String, String> map = new HashMap<String, String>();
		post.setHeader("Authorization", token);
		map.put("merchant_uid", mid); 
		String asd = ""; 
		try {
			post.setEntity(new UrlEncodedFormEntity(convertParameter(map)));
			HttpResponse res = client.execute(post); 
			ObjectMapper mapper = new ObjectMapper(); 
			String enty = EntityUtils.toString(res.getEntity()); 
			JsonNode rootNode = mapper.readTree(enty); 
			asd = rootNode.get("response").asText(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		if (asd.equals("null")) {
			System.err.println("환불실패");
			return -1;
		} else {
			System.err.println("환불성공");
			return 1; 
		} 
	}
	
	//상품결제 폼 호출 (관리자 결제목록)
	@RequestMapping(value={"/pay"}, method=RequestMethod.GET)
	public String pay(HttpServletRequest request, Model model) {
		String nm = request.getParameter("unm");
		// 값은 아임포트의 가맹점 식별코드 입력
		model.addAttribute("impKey", "imp32470313"); 
		return "admin/adminPay";
	}
	
	//상품결제 폼 호출 (회원 결제)
		@RequestMapping(value={"/payUser"}, method=RequestMethod.GET)
		public String pay1(HttpServletRequest request, Model model) {
			String nm = request.getParameter("unm");
			// 값은 아임포트의 가맹점 식별코드 입력
			model.addAttribute("impKey", "imp32470313"); 
			return "views/pay";
		}
	
	
	//결제 진행 폼=> 이곳에서 DB저장 로직도 추가하기
	@RequestMapping(value="/payUser", method=RequestMethod.POST)
	public void payment(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String nm = request.getParameter("unm");
		String amount = request.getParameter("amount");
		String mid = request.getParameter("mid");
		String token = getImportToken();
		setHackCheck(amount, mid, token);
		
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		out.println("<html>");
		out.println("<head><title>주문완료</title></head>");
		out.println("<body>");
		out.print(nm+"님의 주문이 완료 되었습니다.<br>");
		out.print("상점 거래ID: "+mid+"<br>");
		out.print("결제 금액: "+amount+"<br>");
		out.print("<a href='/pay'>쇼핑 계속하기</a>");
		out.print("<a href='javascript:(\"준비중입니다.\");'>나의 주문내역</a>");
		out.println("</body></html>");
	}
	 
	// 아임포트 결제완료건에 한하여 목록 반환 
	@RequestMapping(value="/payamount")
	@ResponseBody
	public Object getAmount(HttpServletRequest request) { 
		String mid = request.getParameter("mid");
		String token = getImportToken();
		System.out.println("토큰값: "+token);
		System.out.println("mid값: "+mid);
		Map<String, String> map = new HashMap<String, String>();
		HttpClient client = HttpClientBuilder.create().build(); 
		HttpGet get = new HttpGet(IMPORT_PAYMENTINFO_URL + mid + "/paid");
		get.setHeader("Authorization", token); 
		try {
			HttpResponse res = client.execute(get); 
			ObjectMapper mapper = new ObjectMapper(); 
			String body = EntityUtils.toString(res.getEntity()); 
			JsonNode rootNode = mapper.readTree(body); 
			JsonNode resNode = rootNode.get("response"); 
			System.out.println("777: " + resNode);
			if(resNode.asText().equals("null")) {
				System.out.println("내역이 없습니다.");
				map.put("msg","내역이 없습니다." );
			}else {
				map.put("imp_uid",resNode.get("imp_uid").asText() );
				map.put("merchant_uid",resNode.get("merchant_uid").asText() );
				map.put("name",resNode.get("name").asText() );
				map.put("buyer_name",resNode.get("buyer_name").asText() );
				map.put("amount",resNode.get("amount").asText() );
				map.put("pay_method",resNode.get("pay_method").asText() );
				map.put("pg_provider",resNode.get("pg_provider").asText() );
				map.put("started_at",resNode.get("started_at").asText() );
				map.put("cancelled_at",resNode.get("cancelled_at").asText() );
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return map; 
	} 
	
	public Long unixtime(String timestamp, String zoneoffset) {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
  		LocalDateTime localDateTime = LocalDateTime.parse(timestamp, pattern);
  		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.of(zoneoffset));
  		return zonedDateTime.toInstant().toEpochMilli();
  	}
	
	
	// 아임포트 전체 목록 반환 
	@RequestMapping(value="/paylist")
	@ResponseBody
	public Object getlist() { 
		String token = getImportToken();
		System.out.println("토큰값: "+token);
		List<Object> list = new ArrayList<Object>();
		HttpClient client = HttpClientBuilder.create().build(); 
		HttpGet get = new HttpGet(IMPORT_PAYMENTLIST_URL);
		get.setHeader("Authorization", token); 
		try {
			HttpResponse res = client.execute(get); 
			ObjectMapper mapper = new ObjectMapper(); 
			String body = EntityUtils.toString(res.getEntity()); 
			JsonNode rootNode = mapper.readTree(body); 
			JsonNode resNode = rootNode.get("response").get("list"); 
			System.out.println("555: " + resNode);
			String pg_provider_name="";
			for(int i=0; i< resNode.size();i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("imp_uid",resNode.get(i).get("imp_uid").asText() );
				map.put("merchant_uid",resNode.get(i).get("merchant_uid").asText() );
				map.put("name",resNode.get(i).get("name").asText() );
				map.put("buyer_name",resNode.get(i).get("buyer_name").asText() );
				map.put("amount",resNode.get(i).get("amount").asText() );
				map.put("cancel_amount",resNode.get(i).get("cancel_amount").asText() );
				map.put("status",resNode.get(i).get("status").asText() );
				
				map.put("pay_method",resNode.get(i).get("pay_method").asText() );
				if((resNode.get(i).get("pg_provider").asText().equals("html5_inicis"))){
					 pg_provider_name="(주)케이지이니시스";
				}
				map.put("pg_provider", pg_provider_name);
				
				String unixTimeStamp = resNode.get(i).get("started_at").asText();
			
				   long timestamp = Long.parseLong(unixTimeStamp);
				    Date date = new java.util.Date(timestamp*1000L); 
				    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
				    String StartDatetime = sdf.format(date);
				
			
				map.put("started_at", StartDatetime);
				
				 unixTimeStamp = resNode.get(i).get("cancelled_at").asText(); 
				
				 if(unixTimeStamp.equals("0")) {
//					 unixTimeStamp="";
				 }
				  timestamp = Long.parseLong(unixTimeStamp);
				     date = new java.util.Date(timestamp*1000L); 
				    sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
				    String endDatetime = sdf.format(date);
				    
				  
				    if(endDatetime.equals("1970-01-01 09:00:00")) {
				    	String cancelNull="";
				    	  map.put("cancelled_at",cancelNull);
				    }else {
				 
				    map.put("cancelled_at",endDatetime);
				    }
				list.add(map);
			}
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return list; 
	} 
	 
	 
	
	
	// 아임포트 전체 목록 반환 
//		@RequestMapping(value="/paylist")
//		@ResponseBody
//		public Object getlist() { 
//			String token = getImportToken();
//			System.out.println("토큰값: "+token);
//			List<Object> list = new ArrayList<Object>();
//			
//			long stime = unixtime("2022-07-08 00:00:00.000 +0100","+0100")/1000; //시작과 끝은 90일 단위로 맞추셈
//			long etime = unixtime("2022-10-21 00:00:00.000 +0100","+0100")/1000;
//			System.out.println("start unixtime : "+stime);
//			System.out.println("end unixtime : "+etime);
//			
//			HttpClient client = HttpClientBuilder.create().build(); 
//			HttpGet get = new HttpGet(IMPORT_PAYMENTLIST_URL+"?page=1&limit=100&from="+stime+"&to="+etime+"&sorting=-started");
//			get.setHeader("Authorization", token); 
//			try {
//				HttpResponse res = client.execute(get); 
//				ObjectMapper mapper = new ObjectMapper(); 
//				String body = EntityUtils.toString(res.getEntity()); 
//				JsonNode rootNode = mapper.readTree(body); 
//				JsonNode resNode = rootNode.get("response").get("list"); 
//				JsonNode resNode1 = rootNode.get("response"); 
//				System.out.println("555: " + resNode1);
//				
//				for(int i=0; i< resNode.size();i++) {
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("imp_uid",resNode.get(i).get("imp_uid").asText() );
//					map.put("merchant_uid",resNode.get(i).get("merchant_uid").asText() );
//					map.put("name",resNode.get(i).get("name").asText() );
//					map.put("buyer_name",resNode.get(i).get("buyer_name").asText() );
//					map.put("amount",resNode.get(i).get("amount").asText() );
//					map.put("cancel_amount",resNode.get(i).get("cancel_amount").asText() );
//					map.put("failed_at",resNode.get(i).get("status").asText() );
//					list.add(map);
//				}
//				
//			} catch (Exception e) { 
//				e.printStackTrace(); 
//			}
//			return list; 
//		} 

	@Autowired
	private PayService payService;
	
	// 글 목록
		@RequestMapping("/myPayList")
		public String getPayListPost(PayVO vo, Model model) {

//			//총 목록 수
//			int totalPageCnt = couponService.totalCouponListCnt(vo);
//			//현재 페이지 설정
//			int nowPage = Integer.parseInt(nowPageBtn==null || nowPageBtn.equals("") ? "1" :nowPageBtn);
//			System.out.println("totalPageCnt: "+totalPageCnt +", nowPage: "+nowPage);
//			//한페이지당 보여줄 목록 수
//			int onePageCnt = 10;
//			//한 번에 보여질 버튼 수
//			int oneBtnCnt = 5;
//
//			PagingVO pvo = new PagingVO(totalPageCnt, onePageCnt, nowPage, oneBtnCnt);
//			vo.setOffset(pvo.getOffset());
//
//			Date now = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
//			 String today = sdf.format(now);
//			 
//			model.addAttribute("today",today); 
//			model.addAttribute("paging", pvo);
			model.addAttribute("payList", payService.getPayList(vo));
			return "views/myPay";
		}


	}
