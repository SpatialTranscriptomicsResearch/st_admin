<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Edit dataset</title>
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet"
              media="screen">

            <!-- Bootstrap and JQuery libraries, for the logout button and other JS features -->
            <script
            src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
            <script
            src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>

            <!-- Script to set the highlight the active menu in the header -->
            <script>
                $(document).ready(function (event) {
                    $("#menuDataset").addClass("active");
                });
            </script>

            <!-- Script to style file upload -->
            <script type="text/javascript"
            src="<c:url value="/js/bootstrap-filestyle.min.js"/>"></script>


    </head>
    <body>

        <c:import url="header.jsp"></c:import>
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />

        <div class="container">

            <div class="page-header">
                <h1>
                    Edit dataset <small>${datasetform.dataset.name}</small>
                </h1>
            </div>

            <c:if test="${not empty errors}">
                <div class="alert alert-error">
                    <strong>Error: </strong>Your input is not valid. Please check the
                    values in the form below.
                </div>
            </c:if>

            <c:if test="${not empty featureerror}">
                <div class="alert alert-error">
                    <strong>Error: </strong>${featureerror}
                </div>
            </c:if>


            <div class="row">
                <form:form method="POST" commandName="datasetform"
                           action="${contextPath}/dataset/submitedit" class="form-horizontal"
                           enctype="multipart/form-data">

                    <fieldset>

                        <div class="span6">
                            <legend>General</legend>

                            <spring:bind path="dataset.name">
                                <div class="control-group  ${status.error ? 'error' : ''}">
                                    <label class="control-label" for="inputName">Name</label>
                                    <div class="controls">
                                        <form:input type="text" id="inputName" placeholder="Name"
                                                    path="dataset.name" />
                                        <span class='help-inline'>${status.errorMessage}</span>
                                    </div>
                                </div>
                            </spring:bind>

                            <spring:bind path="dataset.enabled">
                                <div class="control-group">
                                    <label class="control-label">Dataset enabled</label>
                                    <div class="controls">
                                        <form:checkbox path="dataset.enabled" />
                                    </div>
                                </div>
                            </spring:bind>

                            <spring:bind path="dataset.tissue">
                                <div class="control-group  ${status.error ? 'error' : ''}">
                                    <label class="control-label" for="tissue">Tissue</label>
                                    <div class="controls">
                                        <form:input type="text" id="tissue" placeholder="Tissue"
                                                    path="dataset.tissue" />
                                        <span class='help-inline'>${status.errorMessage}</span>
                                    </div>
                                </div>
                            </spring:bind>

                            <spring:bind path="dataset.species">
                                <div class="control-group  ${status.error ? 'error' : ''}">
                                    <label class="control-label" for="species">Species</label>
                                    <div class="controls">
                                        <form:input type="text" id="species" placeholder="Species"
                                                    path="dataset.species" />
                                        <span class='help-inline'>${status.errorMessage}</span>
                                    </div>
                                </div>
                            </spring:bind>

                            <spring:bind path="dataset.image_alignment_id">
                                <div class="control-group">
                                    <label class="control-label" for="inputImageAlignmentId">Image alignment ID</label>
                                    <div class="controls">
                                        <form:select id="inputImageAlignmentId" path="dataset.image_alignment_id" multiple="false">
                                            <form:options items="${imageAlignmentChoices}"></form:options>
                                        </form:select>
                                    </div>
                                </div>
                            </spring:bind>


                            <spring:bind path="dataset.granted_accounts">
                                <div class="control-group">
                                    <label class="control-label" for="inputGrantedAccounts">Granted accounts</label>
                                    <div class="controls">
                                        <form:select id="inputGrantedAccounts" path="dataset.granted_accounts" class="multiselect" multiple="multiple">
                                            <form:options items="${accountChoices}"></form:options>
                                        </form:select>
                                    </div>
                                </div>
                            </spring:bind>						


                            <legend>ST Data <small>(leave empty to keep the current data)</small></legend>

                            <spring:bind path="featureFile">
                                <div class="control-group  ${status.error ? 'error' : ''}">
                                    <label class="control-label" for="featureFile">ST Data file</label>
                                    <div class="control-group">
                                        <div class="controls">
                                            <form:input type="file" id="featureFile"
                                                        placeholder="Feature file" path="featureFile"
                                                        class="filestyle" />
                                            <span class='help-inline'>${status.errorMessage}</span>
                                        </div>
                                    </div>
                                </div>
                            </spring:bind>
                        </div>


                        <div class="span4">
                            <legend>Comments</legend>
                            <spring:bind path="dataset.comment">
                                <div class="control-group  ${status.error ? 'error' : ''}">
                                    <label class="control-label" for="comment">Comments</label>
                                    <div class="controls">
                                        <form:textarea rows="5" id="comment"
                                                       placeholder="Comment" path="dataset.comment"></form:textarea>
                                        <span class='help-inline'>${status.errorMessage}</span>
                                    </div>
                                </div>
                            </spring:bind>

                        </div>

                    </fieldset>

                    <div class="control-group">
                        <button type="submit" class="btn">Save</button>
                    </div>

                    <form:input type="hidden" path="dataset.id" />
                    <form:input type="hidden" path="dataset.created_by_account_id" />
                    <form:input type="hidden" path="dataset.created_at" />
                    <form:input type="hidden" path="dataset.last_modified" />

                </form:form>
            </div>
            <div>
                <a href="<c:url value="/dataset/"/>${datasetform.dataset.id}">Cancel</a>
            </div>

        </div>

        <!-- Load File upload style -->
        <script>
                $(":file").filestyle({
                    buttonText: "Choose .json.gz file",
                    classInput: "input-small"
                });
        </script>

    </body>
</html>
