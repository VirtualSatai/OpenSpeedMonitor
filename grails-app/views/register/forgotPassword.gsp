<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="kickstart_osm" />
	<title><g:message code="spring.security.ui.forgotPassword.title" args="[entityName]" /></title>
</head>
<body>
<g:form class="form-horizontal" role="main">
	<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
	</g:if>
	<div class="row">
		<div class="span12">
			<g:if test="${forgotPasswordCommand}">
				<g:hasErrors bean="${forgotPasswordCommand}">
					<div class="alert alert-error">
						<strong><g:message code="de.iteratec.isocsi.CsiDashboardController.selectionErrors.title"/></strong>
						<ul>
							<g:eachError var="eachError" bean="${forgotPasswordCommand}">
								<li><g:message error="${eachError}"/></li>
							</g:eachError>
						</ul>
					</div>
				</g:hasErrors>
			</g:if>
		</div>
	</div>
	<g:if test='${emailSent}'>
		<br/>
		<g:message code='spring.security.ui.register.sent'/>
	</g:if>
	<g:else>
		<br/>
		<label class="control-label">
			<g:message code="security.user.label" default="Username" />
		</label>
		<div class="control-group">
			<div class="controls">
				<g:textField  name="username" value="" />
			</div>
		</div>
		<div class="form-actions">
			<g:actionSubmit class="btn btn-primary" action="forgotPassword" value="${message(code: 'spring.security.ui.forgotPassword.submit', default: 'Reset password')}" />
		</div>
	</g:else>
</g:form>
</body>
</html>
