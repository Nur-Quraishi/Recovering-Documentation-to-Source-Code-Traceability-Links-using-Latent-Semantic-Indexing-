<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>Welcome to Code Doctor</title>
	</head>
	<body id="page-top">
	<script type="text/javascript">
        $(function() {
            $('#sc').fileselect({
                browseBtnClass: 'btn btn-info', // default: btn btn-primary
                allowedFileExtensions: ['zip'], // default: false, all extensions allowed
                allowedFileSize: 104857600 // 100MB, default: false, no limitation
            });
            $('#srs').fileselect({
                browseBtnClass: 'btn btn-info', // default: btn btn-primary
                allowedFileExtensions: ['pdf'], // default: false, all extensions allowed
                allowedFileSize: 104857600 // 1000MB, default: false, no limitation
            });

            $("#uploadForm input").jqBootstrapValidation({
                preventSubmit: true,
                submitError: function($form, event, errors) {
                    // additional error messages or events
                },
                submitSuccess: function($form, event) {
                    event.preventDefault(); // prevent default submit behaviour
                    $('#noUpload').show();
                    $('.afterUpload').hide();
                    // get values from FORM
                    var formData = new FormData();
                    formData.append("srs", $('#srs').get(0).files[0]);
                    formData.append("sc", $('#sc').get(0).files[0]);
                    formData.append("dimensionality", $('#dimensionality').val());

                    $this = $("#start");
                    $this.prop("disabled", true); // Disable submit button until AJAX call is complete to prevent duplicate messages
                    $.ajax({
                        url: "${g.createLink(controller: 'commonAjax', action: 'resultCalculation')}",
                        type: "POST",
						dataType: "JSON",
                        mimeType: "multipart/form-data",
                        contentType: false,
                        processData: false,
                        data: formData,
						async: false,
                        cache: false,
                        success: function(data) {
                            $('#noUpload').hide();
                            // Result Integration
							$('#percentageClass').removeClass();
							$('#percentageClass').addClass("c100 big center p" + parseInt(data.percentageResult).toString());
							$('#percentageValue').html(parseInt(data.percentageResult).toString() + "%");
                            $('.afterUpload').show();
							// Success message
							$('#status').html("<div class='alert alert-success'>");
							$('#status > .alert-success').html("<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;")
								.append("</button>");
							$('#status > .alert-success')
								.append("<strong>Your files has been successfully uploaded and result is ready. </strong>");
							$('#status > .alert-success')
								.append('</div>');
							//clear all fields
							//$('#uploadForm').trigger("reset");
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            alert(textStatus + "\n" + errorThrown);
                            // Fail message
                            $('#status').html("<div class='alert alert-danger'>");
                            $('#status > .alert-danger').html("<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;")
                                .append("</button>");
                            $('#status > .alert-danger').append($("<strong>").text("Something went wrong. Please try again later!"));
                            $('#status > .alert-danger').append('</div>');
                            //clear all fields
                            $('#uploadForm').trigger("reset");
                        },
                        complete: function() {
                            setTimeout(function() {
                                $this.prop("disabled", false); // Re-enable submit button when AJAX call is complete
                            }, 1000);
                        }
                    });
                },
                filter: function() {
                    return $(this).is(":visible");
                },
            });
        });

		var ajaxAction = "${createLink(controller:'commonAjax',action:'mailSending')}";
	</script>
	<!-- Navigation -->
	<nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
		<div class="container">
			<a class="navbar-brand js-scroll-trigger" href="#page-top">Code Doctor!</a>
			<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
				Menu
				<i class="fa fa-bars"></i>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav ml-auto">
					<li class="nav-item">
						<a class="nav-link js-scroll-trigger" href="#services">Upload</a>
					</li>
					<li class="nav-item">
						<a class="nav-link js-scroll-trigger" href="#portfolio">Result</a>
					</li>
					<li class="nav-item">
						<a class="nav-link js-scroll-trigger" href="#about">About</a>
					</li>
					<li class="nav-item">
						<a class="nav-link js-scroll-trigger" href="#team">Team</a>
					</li>
					<li class="nav-item">
						<a class="nav-link js-scroll-trigger" href="#contact">Contact</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>

	<!-- Header -->
	<header class="masthead">
		<div class="container">
			<div class="intro-text">
				<div class="intro-lead-in">Welcome To Our Clinic!</div>
				<div class="intro-heading">Time To Inspect Your Code</div>
				<a class="btn btn-xl js-scroll-trigger" href="#services">Let's Try</a>
			</div>
		</div>
	</header>

	<!-- Services -->
	<section id="services">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Upload</h2>
					<h3 class="section-subheading text-muted">Put your project SRS and source code here.</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<form id="uploadForm" enctype="multipart/form-data" method="post" name="uploadFile" novalidate>
						<div class="row justify-content-center">
							<div class="col-md-5">
								<div class="form-group">
									<label for="srs">Document (SRS):</label>
									<input id="srs" type="file" class="form-control" name="srs" required data-validation-required-message="Please enter your document."/>
									<p class="help-block text-danger"></p>
								</div>
								<div class="form-group">
									<label for="sc">Source Code:</label>
									<input id="sc" type="file" class="form-control" name="sc" required data-validation-required-message="Please enter your source code."/>
									<p class="help-block text-danger"></p>
								</div>
								<div class="form-group">
									<label for="dimensionality">Dimension of LSI Subspace:</label>
									<input id="dimensionality" type="number" class="form-control" name="dimensionality" min="0" value="0"/>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="col-lg-12 text-center">
								<button id="start" class="btn btn-xl" type="submit">Start!</button>
								<div id="status" style="margin-top: 10px"></div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>

	<!-- Portfolio Grid -->
	<section class="bg-light" id="portfolio">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Result</h2>
					<h3 class="section-subheading text-muted">Here you can find the result in percentage of matching.</h3>
				</div>
			</div>
			<div class="row justify-content-center" style="margin-bottom: 150px" id="noUpload">
				<div class="col-sm-9">
					<h3 style="color: red">No file is uploaded yet for calculating result.</h3>
				</div>
			</div>
			<div class="row justify-content-center afterUpload" style="display: none">
				<div class="col-sm-3 text-center">
					<div class="c100 p0 big center" id="percentageClass" style="margin-bottom: 15px">
						<span id="percentageValue">0%</span>
						<div class="slice" style="z-index: auto">
							<div class="bar" style="z-index: auto"></div>
							<div class="fill" style="z-index: auto"></div>
						</div>
					</div>
					<button id="more" class="btn btn-xs btn-info">Click here for more details...</button>
				</div>
			</div>
		</div>
	</section>

	<!-- About -->
	<section id="about">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">About</h2>
					<h3 class="section-subheading text-muted">Lorem ipsum dolor sit amet consectetur.</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<ul class="timeline">
						<li>
							<div class="timeline-image">
								<img class="rounded-circle img-fluid" src="img/about/1.jpg" alt="">
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>2009-2011</h4>
									<h4 class="subheading">Our Humble Beginnings</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt ut voluptatum eius sapiente, totam reiciendis temporibus qui quibusdam, recusandae sit vero unde, sed, incidunt et ea quo dolore laudantium consectetur!</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image">
								<img class="rounded-circle img-fluid" src="img/about/2.jpg" alt="">
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>March 2011</h4>
									<h4 class="subheading">An Agency is Born</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt ut voluptatum eius sapiente, totam reiciendis temporibus qui quibusdam, recusandae sit vero unde, sed, incidunt et ea quo dolore laudantium consectetur!</p>
								</div>
							</div>
						</li>
						<li>
							<div class="timeline-image">
								<img class="rounded-circle img-fluid" src="img/about/3.jpg" alt="">
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>December 2012</h4>
									<h4 class="subheading">Transition to Full Service</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt ut voluptatum eius sapiente, totam reiciendis temporibus qui quibusdam, recusandae sit vero unde, sed, incidunt et ea quo dolore laudantium consectetur!</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image">
								<img class="rounded-circle img-fluid" src="img/about/4.jpg" alt="">
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>July 2014</h4>
									<h4 class="subheading">Phase Two Expansion</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt ut voluptatum eius sapiente, totam reiciendis temporibus qui quibusdam, recusandae sit vero unde, sed, incidunt et ea quo dolore laudantium consectetur!</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image">
								<h4>Be Part
									<br>Of Our
									<br>Story!</h4>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</section>

	<!-- Team -->
	<section class="bg-light" id="team">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Our Amazing Team</h2>
					<h3 class="section-subheading text-muted">It is our first work as a team for the purpose of SPL-3.</h3>
				</div>
			</div>
			<div class="row justify-content-center">
				<div class="col-sm-4">
					<div class="team-member">
						<img class="mx-auto rounded-circle" src="img/team/rayhanSir.jpg" alt="">
						<h4>Rayhanur Rahman</h4>
						<p class="text-muted">Supervisor</p>
						<ul class="list-inline social-buttons">
							<li class="list-inline-item">
								<a href="#">
									<i class="fa fa-twitter"></i>
								</a>
							</li>
							<li class="list-inline-item">
								<a href="https://www.facebook.com/rayhanur.rahman" target="_blank">
									<i class="fa fa-facebook"></i>
								</a>
							</li>
							<li class="list-inline-item">
								<a href="#">
									<i class="fa fa-linkedin"></i>
								</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="team-member">
						<img class="mx-auto rounded-circle" src="img/team/nur.jpg" alt="">
						<h4>M. A. Nur Quraishi</h4>
						<p class="text-muted">Supervisee</p>
						<ul class="list-inline social-buttons">
							<li class="list-inline-item">
								<a href="#">
									<i class="fa fa-twitter"></i>
								</a>
							</li>
							<li class="list-inline-item">
								<a href="https://www.facebook.com/nur.quraishi" target="_blank">
									<i class="fa fa-facebook"></i>
								</a>
							</li>
							<li class="list-inline-item">
								<a href="#">
									<i class="fa fa-linkedin"></i>
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-8 mx-auto text-center">
					<p class="large text-muted">This web based tool is used to recover SRS-to-Source-Code Traceability Links using Latent Semantic Indexing.</p>
				</div>
			</div>
		</div>
	</section>

	<!-- Clients -->
	<section class="py-5">
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-md-3 col-sm-6">
					<a href="http://www.iit.du.ac.bd/" target="_blank">
						<img class="img-fluid d-block mx-auto" height="100px" width="100px" src="img/logos/iit.jpg" alt="">
					</a>
				</div>
			</div>
		</div>
	</section>

	<!-- Contact -->
	<section id="contact">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Contact Us</h2>
					<h3 class="section-subheading text-muted">Put your opinion and review here.</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<form id="contactForm" name="sentMessage" novalidate>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<input class="form-control" name="name" id="name" type="text" placeholder="Your Name *" required data-validation-required-message="Please enter your name.">
									<p class="help-block text-danger"></p>
								</div>
								<div class="form-group">
									<input class="form-control" id="email" name="email" type="email" placeholder="Your Email *" required data-validation-required-message="Please enter your email address.">
									<p class="help-block text-danger"></p>
								</div>
								<div class="form-group">
									<input class="form-control" id="phone" name="phone" type="tel" placeholder="Your Phone *" required data-validation-required-message="Please enter your phone number.">
									<p class="help-block text-danger"></p>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<textarea class="form-control" id="message" name="message" placeholder="Your Message *" required data-validation-required-message="Please enter a message."></textarea>
									<p class="help-block text-danger"></p>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="col-lg-12 text-center">
								<div id="success"></div>
								<button id="sendMessageButton" class="btn btn-xl" type="submit">Send Message</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>

	<!-- Footer -->
	<footer>
		<div class="container">
			<div class="row">
				<div class="col-md-4">
					<span class="copyright">Copyright &copy; Code Doctor 2017</span>
				</div>
				<div class="col-md-4">
					<ul class="list-inline social-buttons">
						<li class="list-inline-item">
							<a href="#">
								<i class="fa fa-twitter"></i>
							</a>
						</li>
						<li class="list-inline-item">
							<a href="#">
								<i class="fa fa-facebook"></i>
							</a>
						</li>
						<li class="list-inline-item">
							<a href="#">
								<i class="fa fa-linkedin"></i>
							</a>
						</li>
					</ul>
				</div>
				<div class="col-md-4">
					<ul class="list-inline quicklinks">
						<li class="list-inline-item">
							<a href="#">Privacy Policy</a>
						</li>
						<li class="list-inline-item">
							<a href="#">Terms of Use</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</footer>
	</body>
</html>
