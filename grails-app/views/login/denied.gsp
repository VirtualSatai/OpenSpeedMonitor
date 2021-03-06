<g:set var="lang" value="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'}"/>
<html>
<head>
	<title><g:message code="springSecurity.denied.title"/></title>
	<meta name="layout" content="kickstart" />

	<g:set var="layout_nomainmenu"		value="${true}" scope="request"/>
	<g:set var="layout_nosecondarymenu"	value="${true}" scope="request"/>
</head>

<body>
<h1><g:message code="springSecurity.denied.title" locale="${lang}"/>!</h1>

  	<section id="Error" class="card">
		<div class="big-message">
			<div class="container">
		    	<h2><g:message code="springSecurity.denied.message" locale="${lang}"/></h2>
				
				<div class="actions margin-top-large">
					<a href="${createLink(uri: '/')}" class="btn btn-lg btn-primary">
                        <i class="fas fa-chevron-left"></i>
						<g:message code="error.button.backToHome" locale="${lang}"/>
					</a>
					<a href="${createLink(uri: '/login')}" class="btn btn-lg btn-success">
                        <i class="fas fa-user"></i>
						<g:message code="error.button.login" locale="${lang}"/>
					</a>					
				</div>
			</div>
		</div>
	</section>

<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>

</body>
</html>
