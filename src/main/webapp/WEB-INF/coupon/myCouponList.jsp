<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴전시스</title>

 <script>
  
     function notlogin() {
        alert("로그인 후 진행 해 주시기 바랍니다.");
        location.href ="/login.jsp"
     };
    </script>

<%@ include file="/header.jsp"%>
</head>
<body>
<fmt:parseDate var="startDate_D"  value="${today }" pattern="yyyy-MM-dd"/>
<fmt:parseNumber var="startDate_N" value="${startDate_D.time / (1000*60*60*24)}" integerOnly="true" />
 
 <div class="container" style="text-align: -webkit-center;">
	<div class="board" ><h1 >내 쿠폰함</h1></div>
		<br>
	<div class="container">
				<div class="row row-cols-1 row-cols-sm-1 row-cols-md-1 row-cols-xl-2">
				
<c:forEach items="${couponList}" var="coupon">
<fmt:parseDate var="endDate_D" value="${coupon.c_date }"  pattern="yyyy-MM-dd"/>
<fmt:parseNumber var="endDate_N" value="${endDate_D.time / (1000*60*60*24)}" integerOnly="true" /> 
	<div class="col">
   
      
         <div class="rectangle"
               style="-webkit-mask-image: radial-gradient(circle at 18px 50%, transparent 18px, red 18.5px); -webkit-mask-position: -18px;">
               <div class="row">
                  <div class="col-3">
                     <h1 class="text-center"
                        style="transform: translate(0, 70%); font-size:50px; font-weight: bolder; padding-left: 17px">${coupon.c_per}%</h1>
                  </div>
					   <div class="col title"
                     style="font-size: 15px; transform: translate(0, 40%); text-align: left;  white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                     <b id="c_name" style="font-size: 23px">${coupon.c_name}</b><br>
                     ${coupon.c_date}까지<br> <span class="text-danger"><small>
                           ${endDate_N-startDate_N}일 남음 &nbsp;</small></span>
                  </div>

					
					</div>
			     </div>
			</div>
      </c:forEach>
			</div>
		</div>
		</div>
 <!-- 		반복처리할 태그 시작 -->
			<div id="btnBox_parent">
				<div id="btnBox">
								<!-- 		맨처음 -->
								<c:if test="${paging.nowPageBtn > 1 }">
									<a href="MyCoupon?nowPageBtn=1">&laquo;</a>
								</c:if>
								<c:if test="${paging.nowPageBtn > 1 }">
									<a  href="MyCoupon?nowPageBtn=${paging.nowPageBtn-1}">&lt;</a>
								</c:if>

								<!-- 반복처리 태그 -->
								<c:forEach begin="${paging.startBtn}" end="${paging.endBtn }" step="1" var="i">
									<c:choose>
										<c:when test="${paging.nowPageBtn==i}">
											<a style="font-weight:400; color:#7832f7;" href="MyCoupon?nowPageBtn=${i}">${i}</a>
										</c:when>
										<c:otherwise>
											<a  href="MyCoupon?nowPageBtn=${i}">${i}</a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<!-- 		반복 끝 -->
								<c:if test="${paging.nowPageBtn < paging.totalBtnCnt }">
									<a href="MyCoupon?nowPageBtn=${paging.nowPageBtn+1}">&gt;</a>
								</c:if>
								<!-- 		맨끝 -->
								<c:if test="${paging.nowPageBtn < paging.totalBtnCnt }">
									<a href="MyCoupon?nowPageBtn=${paging.totalBtnCnt}">&raquo;</a>
								</c:if>
					</div>
					</div>
					
			<!-- 		반복처리할 태그 끝 --> 
			
      <%@ include file="../../footer.jsp" %>
</body>
</html>