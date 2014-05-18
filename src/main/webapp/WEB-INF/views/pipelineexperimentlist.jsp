<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Pipeline experiments</title>
<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet"
	media="screen">

<!-- Boostrap and JQuery libraries, for the logout button and other JS features -->
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script
	src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>

<!-- Script to set the highlight the active menu in the header -->
<script>
	$(document).ready(function(event) {
		$("#menuPipelineExperiment").addClass("active");
	});
</script>

<!-- Script for Delete dialog -->
<script>
	$(document)
			.on(
					"click",
					".open-DeleteDialog",
					function() {
						var theId = $(this).data('id');
						$(".modal-footer #deleteBtn")
								.html(
										'<a href="#" class="btn" data-dismiss="modal" aria-hidden="true">Cancel</a> <a href="<c:url value="/pipelineexperiment/"/>'
												+ theId
												+ '/delete" class="btn btn-danger">Delete</a>');
					});
</script>


</head>
<body>

	<c:import url="header.jsp"></c:import>

	<div class="container">
		<div class="page-header">
			<h1>Pipeline experiments</h1>
		</div>

		<!--  <div class="row">
        <div class="span2 pull-right"><a href="javascript:history.go(0)">Reload page</a></div>
        </div>
        -->

		<c:if test="${not empty msg}">
			<div class="alert alert-success">
				<strong>Success! </strong>${msg}
			</div>
		</c:if>


		<div>
			<a href="<c:url value="/pipelineexperiment/create/"/>">Create pipeline experiment</a>
		</div>


		<table class="table">
			<thead>
				<tr>
					<th>Name</th>
					<th>Account</th>
				</tr>

			</thead>
			<tbody>
				<c:forEach var="exp" items="${pipelineexperimentList}">
					<tr>
						<td><a href="<c:url value="/pipelineexperiment/"/>${exp.id}">${exp.name}</a></td>
						<td>${accountChoices[exp.account_id]}</td>
						<td><a href="#deleteModal" data-toggle="modal"
							data-id="${exp.id}"
							class="open-DeleteDialog btn btn-danger btn-small">Delete</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>

	<div id="deleteModal" class="modal hide fade" tabindex="-1">
		<div class="modal-header">
			<h3 id="deleteModalLabel">Delete pipeline experiment</h3>
		</div>
		<div class="modal-body">
			<div>Are you sure you want to delete the pipeline experiment? This will
				stop the EMR job and delete all data.</div>
		</div>
		<div class="modal-footer">
			<div id="deleteBtn"></div>
		</div>
	</div>

</body>
</html>