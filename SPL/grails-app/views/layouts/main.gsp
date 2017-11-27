<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">


  		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>

		<!-- Bootstrap core CSS -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">

		<!-- Custom fonts for this template -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'font-awesome.min.css')}" type="text/css">
		<link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
		<link href='https://fonts.googleapis.com/css?family=Kaushan+Script' rel='stylesheet' type='text/css'>
		<link href='https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
		<link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700' rel='stylesheet' type='text/css'>

		<!-- Custom styles for this template -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'agency.min.css')}" type="text/css">

		<!-- Data table -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'datatables.min.css')}" type="text/css">

		<!-- Circle -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'circle.css')}" type="text/css">
	</head>
	<body id="page-top">
		<g:layoutBody/>

		<script type="text/javascript">
			$(document).ready(function(){
				$('#table').DataTable();
			});
		</script>

		<!-- Bootstrap core JavaScript -->
		<script type="text/javascript" src="${resource(dir:'js',file:'jquery.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js',file:'popper.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js',file:'bootstrap.min.js')}"></script>

		<!-- Plugin JavaScript -->
		<script type="text/javascript" src="${resource(dir:'js',file:'jquery.easing.min.js')}"></script>

		<!-- Contact form JavaScript -->
		<script type="text/javascript" src="${resource(dir:'js',file:'jqBootstrapValidation.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js',file:'contact_me.js')}"></script>

		<!-- Custom scripts for this template -->
		<script type="text/javascript" src="${resource(dir:'js',file:'agency.min.js')}"></script>
		<!-- Data table -->
		<script type="text/javascript" src="${resource(dir:'js',file:'datatables.js')}"></script>
		<!-- File select -->
		<script type="text/javascript" src="${resource(dir:'js',file:'bootstrap-fileselect.js')}"></script>
	</body>
</html>
