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
<body>
<br>
<br>
<a href="/getMemberList">회원목록</a><br>
<!-- <a href="/getMemberList">회원 결제</a> -->
<!-- <a href="/getMemberList">관리자 결제목록</a> -->
<a href="/getCouponList"> 쿠폰목록</a> <br>
<a href="/pay">관리자 결제목록</a><br>
<a href="/login_kakao">카카오로그인</a><br>
<a href="/payUser">회원 결제</a><br>
<a href="./myPayList">회원 결제 목록</a><br>
<a href="login.jsp">로그인</a><br>
<a href="insertMember.jsp">회원가입</a><br>
<a href="insertCoupon.jsp">쿠폰등록</a><br>
<a href="/MyCoupon">내 쿠폰함</a><br>

</body>
</html>