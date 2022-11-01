<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- header -->
<%@ include file="header.jsp"%>
<!-- header end -->
</head>
<body class="d-flex flex-column min-vh-100">
<!-- <img class="picture" src="./images/main_image.png" /> -->

<div id="carouselExampleCaptions" class="carousel slide" data-ride="carousel" data-interval="false">
  <ol class="carousel-indicators">
    <li data-target="#carouselExampleCaptions" data-slide-to="0" class="active"></li>
    <li data-target="#carouselExampleCaptions" data-slide-to="1"></li>
    <li data-target="#carouselExampleCaptions" data-slide-to="2"></li>
  </ol>
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="./images/main_image.png" class="d-block w-100" alt="배경이미지1">
      <div class="carousel-caption d-none d-md-block">
        <h5>첫번째</h5>
      </div>
    </div>
    <div class="carousel-item">
      <img src="./images/main_image2.png" class="d-block w-100" alt="배경이미지2">
      <div class="carousel-caption d-none d-md-block">
        <h5>두번째</h5>
      </div>
    </div>
    <div class="carousel-item">
      <img src="./images/main_image3.png" class="d-block w-100" alt="배경이미지3">
      <div class="carousel-caption d-none d-md-block">
        <h5>세번째</h5>
      </div>
    </div>
  </div>
 <!--  <button class="carousel-control-prev" type="button" data-target="#carouselExampleCaptions" data-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-target="#carouselExampleCaptions" data-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </button> -->
</div>

<!-- <div class="maintext">
aa
</div> -->
<br><br>
회원 메뉴 
<br><br>

<a href="/getUserExhibitionList">- 회원전시</a><br>
<!-- <a href="/login_kakao">- 카카오로그인</a><br> -->
<!-- <a href="/getPayList">- 나의 구매내역</a><br> -->
<!-- <a href="insertMember.jsp">- 회원가입</a><br> -->
<!-- <a href="/MyCoupon">- 내 쿠폰함</a><br> -->
<!-- <a href="/getOneList">- 1대1문의</a><br> -->
<!-- <a href="login.jsp">- 로그인</a><br> -->
<br><br>
관리자 메뉴 
<br><br>
<a href="/getMemberList">- 회원목록</a><br>
<a href="insertCoupon.jsp">- 쿠폰등록</a><br>
<a href="/getCouponList">- 쿠폰목록</a><br>
<a href="/pay">- 관리자 결제목록</a><br>
<div class="container-fluid">
	<div class="h-100 d-inline-block bg-dark text-white">디브</div>
</div>

<!-- footer start -->
<%@ include file="footer.jsp" %>
<!-- footer end -->
</body>
</html>