<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Document</title>
    
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>  
  <script type="text/javascript">
  
	//정규식 체크
	let telID = /^\d{3}-\d{3,4}-\d{4}$/;
	
	let ID = /^[a-z]+[a-z0-9]{3,19}$/g;
	
	let nameID = /^[가-힣]{2,4}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/;
	
	let pwdID = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
	
	let emailID = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	
	/* jQuery 초기화 */
	$(document).ready(function() {
		//중복 체크
		$("#userJoin #m_id").keyup(function(e) {
		    let join_id = $("#userJoin #m_id").val().trim();
			
			//아이디 체크
	  		if (!ID.test(join_id)) {
				$(".m_id_text").text("아이디를 입력하세요.");
	  			  			
				$("#com").attr("disabled", true);
				
	  			return;
			}
	  		
			console.log(join_id);

	  		$.ajax({
	  			url : 'check_id.do',
				data : {"m_id" : join_id},
				dataType : "json",
				success : function(resData) {
					
					//alert(resData);
					
					if(!resData.result){
						$(".m_id_text").text("사용중인 아이디입니다.");
						
						$("#com").attr("disabled", true);
						
						return;
					}
					
					$(".m_id_text").text("사용가능한 아이디입니다.");
					
					$("#com").attr("disabled", false);
					
				}
			});
			
		});
	});	
	
  //전송 ===================================================
  function send(f) {
	    let m_pwd = $("#userJoin #m_pwd").val().trim();
		let c_pwd = $("#userJoin #c_pwd").val().trim();
		
		let m_name = $("#m_name").val().trim();
		
		let m_gender = $("#m_gender").val();
				
		let m_phone1 = $("#m_phone1").val(); 
		let m_phone2 = $("#m_phone2").val(); 
		let m_phone3 = $("#m_phone3").val();
		
		let m_email = $("#m_email").val().trim();
  		  		
  		//비밀번호 체크
  		if (! pwdID.test(m_pwd)) {
			$(".m_pwd_text").text("비밀번호를 입력하세요.");
  			
  			$("#userJoin #m_pwd").val("");
  			$("#userJoin #m_pwd").focus();
  			
  			return;
		}
  		
  		if (! pwdID.test(c_pwd)) {
			$(".c_pwd_text").text("비밀번호를 입력하세요.");
  			
  			$("#userJoin #c_pwd").val("");
  			$("#userJoin #c_pwd").focus();
  			
  			return;
		}
  		
  		if (m_pwd != c_pwd) {
			$(".c_pwd_text").text("비밀번호가 일치하지 않습니다.");
  			
  			$("#userJoin #c_pwd").val("");
  			$("#userJoin #c_pwd").focus();
  			
  			return;
		}
  		
  		//이름 체크
  		if (! nameID.test(m_name)) {
			$(".m_name_text").text("이름을 입력하세요.");
  			
  			$("#m_name").val("");
  			$("#m_name").focus();
  			
  			return;
		}
  		
  		//전화번호 체크
  		let m_tel = m_phone1+"-"+m_phone2+"-"+m_phone3;
  		
  		if (! telID.test(m_tel)) {
			$(".m_tel_text").text("전화번호를 입력하세요.");
  			
  			$("#m_phone2").val("");
  			$("#m_phone3").val("");
  			
  			$("#m_phone2").focus();
  			
  			return;
		}
  		
  		$("#m_tel").val(m_tel);
  		
  		//이메일 체크
  		if (!emailID.test(m_email)) {
			$(".m_email_text").text("이메일을 입력하세요.");
  			
  			$("#m_email").val("");
  			$("#m_email").focus();
  			
  			return;
		}
  		
  		//전송
		f.submit();
	}
  	
  </script> 
    
</head>

<body>

    <header>
    	<%@ include file="../header/header.jsp" %>
    </header>

    <div id="content" >
		<div class="inner">
			<div id="userJoin">
		        <div class="inner-in">
		
		            <div class="titleBox">
		                <h2>회원정보 페이지</h1>
		            </div>
		
		            <div class="profileplace">
		                <img class="img-responsive center-block" id="m_photo" src="${pageContext.request.contextPath}/resources/img/mypage/user.jpg">
		            </div>
		
		            <form action="insert.do" method="post" enctype="multipart/form-data">
		
		                <table class="table">
		                    <tr>
		                        <th class="col-sm-3">사용자 ID</th>
		                        <td class="col-sm-9">
	                                <input type="text" name="m_id" id="m_id" class="form-control">
	                                <p class="m_id_text"></p>
		                        </td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">비밀번호</th>
		                        <td class="col-sm-9">
		                        	<input type="password" name="m_pwd" id="m_pwd" class="form-control">
		                        	<p class="m_pwd_text"></p>
	                        	</td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">비밀번호 확인</th>
		                        <td class="col-sm-9">
		                        	<input type="password" id="c_pwd" class="form-control">
		                        	<p class="c_pwd_text"></p>
	                        	</td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">이름</th>
		                        <td class="col-sm-9">
		                        	<input type="text" name="m_name" id="m_name" class="form-control">
		                        	<p class="m_name_text"></p>	
	                        	</td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">성별</th>
		                        <td class="col-sm-9">
		                            <label class="radio-inline">
		                                <input type="radio" name="m_gender" value="남성">남성
		                            </label>
		                            <label class="radio-inline">
		                                <input type="radio" name="m_gender" value="여성">여성
		                            </label>
		                        </td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">전화번호</th>
		                        <td class="col-sm-9">
		                        	<input type="hidden" name="m_tel" id="m_tel">
		                            <div class="col-sm-4">
		                                <select class="form-control" id="m_phone1">
		                                    <option value="010">010</option>
		                                    <option value="011">011</option>
		                                    <option value="016">016</option>
		                                    <option value="017">017</option>
		                                </select>
		                            </div>
		
		                            <div class="col-sm-4">
		                                <input type="text" id="m_phone2" class="form-control">
		                            </div>
		
		                            <div class="col-sm-4">
		                                <input type="text" id="m_phone3" class="form-control">
		                            </div>
		                            
		                            <p class="m_tel_text"></p>	
		                        </td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">E-Mail</th>
		                        <td class="col-sm-9">
		                        	<input class="form-control" type="text" id="m_email" name="m_email" maxlength="30">
		                        	<p class="m_email_text"></p>	
	                        	</td>
		                    </tr>
		
		                    <tr>
		                        <th class="col-sm-3">Image</th>
		                        <td class="col-sm-9">
		                        	<input class="form-control" type="file" name="mm_photo" maxlength="30">
		                        </td>
		                    </tr>
		
		                    <tr class="btnBox">
		                    	<td colspan="2">
		                    		<input id="com" type="button" class="btn btn-info" value="등록" onclick="send(this.form);" disabled="disabled">
		                    		<input type="button" class="btn" value="목록" onclick="location.href='${pageContext.request.contextPath}/index.html'">
	                    		</td>
		                    </tr>
		                </table>
		
		            </form>
		        </div>
		
		    </div>
		</div>
		
		<%@ include file="../etc/scrool.jsp" %>
	</div>

    <footer>
    	<%@ include file="../footer/footer.jsp" %>
    </footer>

</body>

</html>