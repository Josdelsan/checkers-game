<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="project" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">
				<project:menuItem active="${name eq 'home'}" url="/" title="Home">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Join Game</span>
				</project:menuItem>

				<project:menuItem active="${name eq 'ativeGame'}" url="/" title="Active Game">
					<span class="glyphicon glyphicon-book" aria-hidden="true"></span>
					<span>Active Game</span>
				</project:menuItem>
			</ul>
		</div>
	</div>
</nav>