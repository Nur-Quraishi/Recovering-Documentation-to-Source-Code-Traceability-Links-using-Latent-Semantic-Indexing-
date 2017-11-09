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
                allowedFileSize: 31457280 // 30MB, default: false, no limitation
            });
            $('#srs').fileselect({
                browseBtnClass: 'btn btn-info', // default: btn btn-primary
                allowedFileExtensions: ['pdf'], // default: false, all extensions allowed
                allowedFileSize: 104857600 // 1000MB, default: false, no limitation
            });
        });

		$(document).on("click", '#submit', function () {
		    var srs = $('#srs').val();
		    var sc = $('#sc').val();
			if(srs != "" && sc != "")
			{
				$(this).attr("href", "#portfolio");
			}
			else
			{
			    alert("Please complete the input fields.");
                $(this).attr("href", "#sevices");
			}
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
			<div class="row justify-content-center">
				<div class="col-sm-4">
					<div class="form-group">
						<label for="srs">Document (SRS):</label>
						<input id="srs" type="file" name="srs" required data-validation-required-message="Please enter your document."/>
					</div>
				</div>
			</div>
			<div class="row justify-content-center">
				<div class="col-sm-4">
					<div class="form-group">
						<label for="sc">Source Code:</label>
						<input id="sc" type="file" name="sc" required data-validation-required-message="Please enter your source code."/>
					</div>
				</div>
			</div>
			<div class="row text-center justify-content-center">
				<a class="btn btn-xl js-scroll-trigger" id="submit" href="">Start!</a>
			</div>
		</div>
	</section>

	<!-- Portfolio Grid -->
	<section class="bg-light" id="portfolio">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Result</h2>
					<h3 class="section-subheading text-muted">Lorem ipsum dolor sit amet consectetur.</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal1">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/01-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Threads</h4>
						<p class="text-muted">Illustration</p>
					</div>
				</div>
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal2">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/02-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Explore</h4>
						<p class="text-muted">Graphic Design</p>
					</div>
				</div>
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal3">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/03-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Finish</h4>
						<p class="text-muted">Identity</p>
					</div>
				</div>
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal4">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/04-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Lines</h4>
						<p class="text-muted">Branding</p>
					</div>
				</div>
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal5">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/05-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Southwest</h4>
						<p class="text-muted">Website Design</p>
					</div>
				</div>
				<div class="col-md-4 col-sm-6 portfolio-item">
					<a class="portfolio-link" data-toggle="modal" href="#portfolioModal6">
						<div class="portfolio-hover">
							<div class="portfolio-hover-content">
								<i class="fa fa-plus fa-3x"></i>
							</div>
						</div>
						<img class="img-fluid" src="img/portfolio/06-thumbnail.jpg" alt="">
					</a>
					<div class="portfolio-caption">
						<h4>Window</h4>
						<p class="text-muted">Photography</p>
					</div>
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

	<!-- Portfolio Modals -->

	<!-- Modal 1 -->
	<div class="portfolio-modal modal fade" id="portfolioModal1" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/01-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Threads</li>
									<li>Category: Illustration</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal 2 -->
	<div class="portfolio-modal modal fade" id="portfolioModal2" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/02-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Explore</li>
									<li>Category: Graphic Design</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal 3 -->
	<div class="portfolio-modal modal fade" id="portfolioModal3" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/03-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Finish</li>
									<li>Category: Identity</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal 4 -->
	<div class="portfolio-modal modal fade" id="portfolioModal4" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/04-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Lines</li>
									<li>Category: Branding</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal 5 -->
	<div class="portfolio-modal modal fade" id="portfolioModal5" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/05-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Southwest</li>
									<li>Category: Website Design</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal 6 -->
	<div class="portfolio-modal modal fade" id="portfolioModal6" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 mx-auto">
							<div class="modal-body">
								<!-- Project Details Go Here -->
								<h2>Project Name</h2>
								<p class="item-intro text-muted">Lorem ipsum dolor sit amet consectetur.</p>
								<img class="img-fluid d-block mx-auto" src="img/portfolio/06-full.jpg" alt="">
								<p>Use this area to describe your project. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Est blanditiis dolorem culpa incidunt minus dignissimos deserunt repellat aperiam quasi sunt officia expedita beatae cupiditate, maiores repudiandae, nostrum, reiciendis facere nemo!</p>
								<ul class="list-inline">
									<li>Date: January 2017</li>
									<li>Client: Window</li>
									<li>Category: Photography</li>
								</ul>
								<button class="btn btn-primary" data-dismiss="modal" type="button">
									<i class="fa fa-times"></i>
									Close Project</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
