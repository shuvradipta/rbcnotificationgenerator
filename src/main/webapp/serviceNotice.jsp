<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html">
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

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="js/angular_modules/serviceNoticeAngularModule.js"></script>

<!--  ANGULARJS : END -->

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

<title>Manage Service Notices</title>
</head>
<body ng-app="serviceNoticeApp" ng-controller="serviceNoticeController as snCtrl">

<!-- Header : BEGIN -->
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="home.html"><img src="images/rbc_royalbank_en.gif" /></a>
		</div>
	</div>
</nav>
<!-- Header : END -->

<!-- Main Body : BEGIN -->
<c:set var="htmlGenAppUrl" value="${appConfigMappings.HTMLGENAPPURL}"></c:set>
<c:set var="mobileGenAppUrl" value="${appConfigMappings.MOBILEGENAPPURL}"></c:set>
<c:set var="enKey" value="en_"></c:set>
<c:set var="frKey" value="fr_"></c:set>



<div class="container-fluid text-center">
	<div class="row content">
		<!-- LHN : BEGIN -->
		<div class="col-sm-2 sidenav">
			<p class="text-muted">Work with Service Notices</p>
			<p><a href="${htmlGenAppUrl}">Work with HTML</a></p>
			<p><a href="${mobileGenAppUrl}">Work with Mobile</a></p>
		</div>
		<!-- LHN : END -->
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
			<h1 style="text-decoration: underline;">Message List</h1>
			<!-- <p class="text-info">
				Please check the messages you want to add in the Service Notice
			</p> -->
			
			<div class="container">
				<div class="row">
					<div class="table-responsive">
						<table class="table table-condensed table-bordered col-sm-8 bodyContentText">
							<thead>
								<tr>
									<c:forEach var="columnName" items="${tableColumnNames}">
										<th>${columnName}</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${activeNoticeMappings}" var="notice">
									<tr>
										<c:set var="noticeEnKeyUrl" value="${enKey}${notice.key}url"></c:set>
										<c:set var="noticeFrKeyUrl" value="${frKey}${notice.key}url"></c:set>
										<td><a href="${htmlGenAppUrl}?enFileName=${noticeMap[noticeEnKeyUrl]}&frFileName=${noticeMap[noticeFrKeyUrl]}&startTime={{ snCtrl.startTime${notice.key}}}&expiryTime={{ snCtrl.expiryTime${notice.key} }}">${notice.noticeText}</a></td>
										<!-- <td><c:if test="${notice.publicInd}"><span class="glyphicon glyphicon-check"></span></c:if></td> -->
										<td>${notice.startTime}</td>
										<td>${notice.expiryTime}</td>
										<td><input type="checkbox" name="isIncluded${notice.key}" ng-click="snCtrl.manageNotice('${notice.key}')" ng-model="checkBox${notice.key}" ng-disabled="serviceNoticeForm.startTime${notice.key}.$error.datetimelocal || !serviceNoticeForm.startTime${notice.key}.$dirty || !serviceNoticeForm.expiryTime${notice.key}.$dirty || serviceNoticeForm.expiryTime${notice.key}.$error.datetimelocal"/></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<form action="/ServiceNoticeServlet" method="post" name="serviceNoticeForm">
			<p><h1 style="text-decoration: underline;">Notices Leaderboard</h1></p>
			<div class="container">
				<div class="row">
					<div class="table-responsive">
						<table class="table table-condensed table-bordered col-sm-8 bodyContentText">
							<thead>
								<tr>
									<c:forEach var="columnName" items="${tableColumnNames}">
										<th>${columnName}</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${enNoticeMappings}" var="notice">
									<tr>
										<c:set var="noticeEnKeyUrl" value="${enKey}${notice.key}url"></c:set>
										<c:set var="noticeFrKeyUrl" value="${frKey}${notice.key}url"></c:set>
										<td><a href="${htmlGenAppUrl}?enFileName=${noticeMap[noticeEnKeyUrl]}&frFileName=${noticeMap[noticeFrKeyUrl]}&startTime={{ snCtrl.startTime${notice.key}}}&expiryTime={{ snCtrl.expiryTime${notice.key} }}">${notice.noticeText}</a></td>
										<!-- <td><c:if test="${notice.publicInd}"><span class="glyphicon glyphicon-check"></span></c:if></td> -->
										<td><input type="datetime-local" name="startTime${notice.key}" ng-model="snCtrl.startTime${notice.key}" min="${serverDateTime}" />
										<div role="alert">
									    <span class="error" ng-show="serviceNoticeForm.startTime${notice.key}.$error.datetimelocal">Not a valid date!</span></div></td>
										<td><input type="datetime-local" name="expiryTime${notice.key}" ng-model="snCtrl.expiryTime${notice.key}" min="${serverDateTime}"/>
										<div role="alert">
									    <span class="error" ng-show="serviceNoticeForm.expiryTime${notice.key}.$error.datetimelocal">Not a valid date!</span></div></td>
										<!-- <td><c:if test="${notice.kioskInd}"><span class="glyphicon glyphicon-check"></span></c:if></td> -->
										<td><input type="checkbox" name="isIncluded${notice.key}" ng-click="snCtrl.manageNotice('${notice.key}')" ng-model="checkBox${notice.key}" ng-disabled="serviceNoticeForm.startTime${notice.key}.$error.datetimelocal || !serviceNoticeForm.startTime${notice.key}.$dirty || !serviceNoticeForm.expiryTime${notice.key}.$dirty || serviceNoticeForm.expiryTime${notice.key}.$error.datetimelocal"/></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<input type="hidden" name="noticeKeyList" value="{{snCtrl.noticesList}}"/>
			<div class="container">
				<div class="row">
					<div class="panel-group">
						<div class="col-sm-10 panel panel-default">
							<div class="panel-header panel-nopadding"><b>The below notices will be included</b></div>
							<div class="panel-body" ng-hide="snCtrl.noticesList.length > 0" ng-show="snCtrl.noticesList.length == 0"><i>No notices added yet</i></div>
							<div class="table-responsive" ng-show="snCtrl.noticesList.length > 0" ng-hide="snCtrl.noticesList.length == 0">
								<table class="table table-condensed table-bordered col-sm-8 bodyContentText">
									<tbody>
										<tr ng-show="snCtrl.noticesList.length > 0" ng-hide="snCtrl.noticesList.length == 0"><td colspan="5"><h4>English</h4></td></tr>
										<c:forEach items="${enNoticeMappings}" var="notice">
											<tr ng-show="checkBox${notice.key}">
												<td>${notice.noticeText}</td>
												<td>${notice.url}</td>
												<td><c:if test="${notice.publicInd}"><span class="glyphicon glyphicon-check"></span></c:if></td>
												<td>{{ snCtrl.startTime${notice.key} | date:"yyyy-MMM-dd HH:mm:ss"}}</td>
												<td>{{ snCtrl.expiryTime${notice.key} | date:"yyyy-MMM-dd HH:mm:ss"}}</td>
												<td><c:if test="${notice.kioskInd}"><span class="glyphicon glyphicon-check"></span></c:if></td>
											</tr>
										</c:forEach>
									</tbody>
									<tbody>
										<tr ng-show="snCtrl.noticesList.length > 0" ng-hide="snCtrl.noticesList.length == 0"><td colspan="5"><h4>French</h4></td></tr>
										<c:forEach items="${frNoticeMappings}" var="notice">
											<tr ng-show="checkBox${notice.key}">
												<td>${notice.noticeText}</td>
												<td>${notice.url}</td>
												<td><c:if test="${notice.publicInd}"><span class="glyphicon glyphicon-check"></span></c:if></td>
												<td>{{ snCtrl.startTime${notice.key} | date:"yyyy-MMM-dd HH:mm:ss"}}</td>
												<td>{{ snCtrl.expiryTime${notice.key} | date:"yyyy-MMM-dd HH:mm:ss"}}</td>
												<td><c:if test="${notice.kioskInd}"><span class="glyphicon glyphicon-check"></span></c:if></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div style="padding-bottom:15px;padding-top:10px;"><div class="text-center"><button class="btn btn-primary btn-md">Submit Changes <span class="glyphicon glyphicon-floppy-disk"></span></button></div></div>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
		<!-- Middle section : END -->
		<!-- RHN : BEGIN -->
		<div class="col-sm-2 sidenav">
			<c:if test="${PROCESSING_STATUS eq true}">
				<p><a class="btn btn-success btn-sm" download="servicenotice.js" href="/js/servicenotice_en.js">Download English JS File <span class="glyphicon glyphicon-floppy-save"></span></a></p>
				<p><a class="btn btn-success btn-sm" download="servicenotice.js" href="/js/servicenotice_fr.js">Download French JS File <span class="glyphicon glyphicon-floppy-save"></span></a></p>
			</c:if>
		</div>
		<!-- RHN : END -->
	</div>
</div>
<!-- Main Body : END -->

</body>
</html>