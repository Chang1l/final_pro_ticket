<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>

</head>
<body>
<script>
$(document).ready(function(){
$('#Comment_regist').click(function() {
		console.log("��۹�ư ����");
		//Json���� ������ �Ķ���� �������� // ����ȸ���� �;��� �Խù� ��ȣ 
				//${exh_no};
		const review_bno = 2;
		const review_writer = $('#review_writer').val();
		const review_content = $('#review_content').val();
		//�ۼ��� �ۼ��ڿ� ���� 
		
		console.log(review_bno);
		console.log(review_writer);
		console.log(review_content);
	
		if(review_writer == ''){
			alert('�α��� �� �̿����ּ���');
			return;
		}else if(review_content == '') {
			alert('������ �Է��ϼ���');
		}
		
		$.ajax({
			type:'post',
			url:'/insertReview',
			data: JSON.stringify(
				{
					"review_bno":review_bno,
					"review_writer":review_writer,
					"review_content":review_content
					
				}		
			),
			contentType: 'application/json',
			success:function(data){
				console.log('��ż���' + data);
				if(data === 'InsertSuccess') {
					alert('��� ����� �Ϸ�Ǿ����ϴ�.');
					console.log('��� ��� �Ϸ�');
					$('#review_writer').val(review_writer);
// 					$('#review_content').val('');
  					$('#review_content').val(review_content);
  					// ����� ��ϵǸ� , ���ο� ���â �����鼭 ��� ���̵�� �ۼ��ڷ�, ���â�� ���ڷ� ����� .
  					console.log("getList �޼ҵ� ���� ��");
  					getList();
  					console.log("getList �޼ҵ� ȣ��");
				} else {
					alert('�α��� ���� �̿����ֽñ� �ٶ��ϴ�.');
					console.log('��� ��� ����');
				}
			},
			error:function(){
				alert('��Ž���');
			}
		});// ��� �񵿱� ��
			
})// ��۵�� �̺�Ʈ ��



function getList() {
	
	//${exh_no}
	
	const review_bno1=$('#review_bno').val();
	const review_writer1 = $('#review_writer').val();
		const review_content1 = $('#review_content').val();
		
	var objparams={review_bno:review_bno1,review_writer:review_writer1,review_content:review_content1};
		/* const com_no = ${com}; */
// 	$.getJSON(
// 		"/reviewList"+review_bno,
   $.ajax({
		 type:'post',
		 url:"/reviewList",
		 data:objparams,
	
		success:function(data) {
			if(data.total > 0){
				console.log("get list ���� ��");
				var list = data.list;
				
				var comment_html = "<div>";
				
				$('#count').html(data.total);
				for(i = 0;i < list.length;i++){
					var content = list[i].review_content;
					var writer = list[i].review_writer;
					comment_html += "<div><span id='review_writer'><strong>" + writer + "</strong></span><br/>";
					comment_html += "<span id='com-content'>" + content + "</span><br>";
					if(writer === $("#review_writer").val()){
						 comment_html += "<span id='delete' style='cursor:pointer;' data-id ="+content+">[����]</span><br></div><hr>";
						 
					}
					else{
						comment_html += "</div><hr>";
					}
				}
				
				$(".comment_Box").html(comment_html);
				
				
			}
			else{
				var comment_html = "<div>��ϵ� ����� �����ϴ�.</div>";
				$(".comment_Box").html(comment_html);
			}
		}
   });	
		
	
};
});







		
</script>



<div class="comment-box">
                    
   		                 <div class="comment-count">��� <span id="count">0</span></div>
						<input type="hidden" id="review_bno" name="review_bno" value="2">
   		                 	   <!-- <span class="c-icon"><i class="fa-solid fa-user"></i>  -->
   		                 <div class="comment-name">
	                        <span class="anonym">�ۼ��� : 
	                    	    <input type="text" class="form-control" id="review_writer" placeholder="�̸�" name ="review_writer" value='${login.userId}'  style="width: 100px; border:none;">
	                        </span>
	                      </div>   
	                        	
	                        <!-- </span> -->
                     <!--<img src="/�͸�.jpg" width ="50px" alt="My Image"><!-->
                    <div class="comment-sbox">
                        <input type="text" class="comment-input" id="review_content" cols="80" rows="2" name="review_content" >
                        </div>
                        <!-- <span class="com-function-btn" type="hidden">
                            
                            <a href="#"><i class="fa-solid fa-pen-to-square"></i></a>
                            <a href="#"><i class="fa-solid fa-trash-can"></i></a>
                         </span> -->
                    </div>
                    	<div class="regBtn">
                    		<button id="Comment_regist"> ��۵��</button>
                    	 </div>
                    	 
                    	 
                    	 
</body>
</html>