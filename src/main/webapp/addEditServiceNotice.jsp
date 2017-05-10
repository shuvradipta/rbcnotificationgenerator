<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
       uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="https://www1.royalbank.com/uos/common/images/icons/favicon.ico?5" rel="icon" />

<!-- BOOTSTRAP : BEGIN -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- BOOTSTRAP : END -->

<!--  ANGULARJS : BEGIN -->

<!-- <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script> -->

<!--  ANGULARJS : END -->

<title>HTML Page Generator</title>
</head>
<!-- Override CSS : BEGIN -->
<style>
.navbar-default {
    background-color: white;
    border-color: white;
}
.panel-nopadding {
	padding-right: 0px;
	padding-left: 0px;
}
.bodyContentText {
	font-size: 14px;
}
</style>
<!-- Override CSS : END -->
<body>

<!-- Header : BEGIN -->
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="${appConfigMappings.HOMEDOMAIN}/home.html"><img src="${appConfigMappings.HOMEDOMAIN}/images/rbc_royalbank_en.gif" /></a>
		</div>
	</div>
</nav>
<!-- Header : END -->

<!-- Main Body : BEGIN -->
<c:set var="serviceNoticeGenAppUrl" value="${appConfigMappings.SERVICENOTICEGENAPPURL}"></c:set>

<div class="container-fluid text-center">
	<div class="row content">
		<!-- Middle section : BEGIN -->
		<div class="col-sm-8 text-left">
			<c:if test="${PROCESSING_STATUS eq true}">
			<div class="alert alert-success">
			  	<strong>Success!</strong> The file was successfully generated. 
			  	<p>Please use the Download File buttons on the right to view/download the file. </p> 
			</div>
			</c:if>
			<c:if test="${PROCESSING_STATUS eq false}">
			<div class="alert alert-danger">
			  	<strong>Error!</strong> There was some error while processing the input.
			</div>
			</c:if>
			<h1 style="text-decoration: underline;">Message Details</h1>
			<form action="/HTMLGeneratorServlet" method="post" name="htmlGeneratorForm">
			<div class="container">
				<div class="row">
				<table class="col-sm-8">
					<tr>
						<td>
							<div class="col-sm-4">
						    	<p><b>START DATE/TIME :</b> ${startTime}</p>
						  	</div>
					  	</td>
					  	<td>
							<div class="col-sm-4">
						    	<p><b>EXPIRY DATE/TIME :</b> ${expiryTime}</p>
						  	</div>
					  	</td>
					</tr>
				</table>
				</div>
				<div class="row">
					<div class="table-responsive">
						<table class="col-sm-8 bodyContentText">
							<tr>
								<td>
									<div class="form-group">
								    	<label for="serviceNoticeMessage">Service Notice Message:</label>
								    	<input type="text" class="form-control" id="serviceNoticeMessage" name="serviceNoticeMessage" value="${noticeText}" />
								  	</div>
							  	</td>
							</tr>
							<tr>
								<td>
									<div class="form-group">
								    	<label for="serviceNoticeUrl">Message URL:</label>
								    	<input type="text" class="form-control" id="serviceNoticeUrl" name="serviceNoticeUrl" value="${url}" />
								  	</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="form-group">
								    	<label for="kioskIndicator">Opens in a kiosk:</label>
								    	<p><input type="checkbox" class="form-control" id="kioskIndicator" name="kioskIndicator" value="${kioskInd}"/></p>
								  	</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="form-group">
								    	<label for="publicIndicator">Opens in a kiosk:</label>
								    	<p><input type="checkbox" class="form-control" id="publicIndicator" name="publicIndicator" value="${publicInd}"/></p>
								  	</div>
								</td>
							</tr>
							<tr>
								<td>
									<div style="padding-bottom:15px;padding-top:10px;"><div class="text-center">
								    	<a class="btn btn-md" href="/ServiceNoticeServlet">Cancel Changes</a>
								    </div></div>
								</td>
								<td>
									<div style="padding-bottom:15px;padding-top:10px;"><div class="text-center">
								    	<button type="submit" class="btn btn-success btn-md">Save Changes</button>
								    </div></div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			</form>
		</div>
		<!-- Middle section : END -->
		<!-- RHN : BEGIN -->
		<div class="col-sm-2 sidenav">
			<c:if test="${PROCESSING_STATUS eq true}">
				<p><a class="btn btn-success btn-sm" download="${htmlFileName}" href="/html/generated.html">Download HTML <span class="glyphicon glyphicon-floppy-save"></span></a></p>
			</c:if>
		</div>
		<!-- RHN : END -->
	</div>
</div>
<!-- Main Body : END -->

</body>
</html>