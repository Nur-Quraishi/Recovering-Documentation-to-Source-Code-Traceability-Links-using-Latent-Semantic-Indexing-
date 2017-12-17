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
                    formData.append("threshold", $('#threshold').val());

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
                            $('#tableDetails').slideUp();
                            populateTable(data);

							// Success message
							$('#status').html("<div class='alert alert-success'>");
							$('#status > .alert-success').html("<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;")
								.append("</button>");
							$('#status > .alert-success')
								.append("<strong>Your files has been successfully uploaded and result is ready. Scroll down to see it.</strong>");
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
        
        $(document).on('click', '#more', function () {
            if($(this).val() == "see more")
			{
			    $(this).val("see less");
			    $(this).html("<i class='fa fa-arrow-circle-o-up' aria-hidden='true'></i>" + "Click here for collapse details...");
                $('#tableDetails').slideDown();
			}
			else
			{
                $(this).val("see more");
                $(this).html("<i class='fa fa-arrow-circle-o-down' aria-hidden='true'></i>" + "Click here for more details...");
                $('#tableDetails').slideUp();
			}
        });

        var ajaxAction = "${createLink(controller:'commonAjax',action:'mailSending')}";

        $(document).ready(function(){
            $('#table').DataTable({
                "sScrollY": "500px",
                "bScrollCollapse": true,
                "autoWidth": false,
                "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                    if(aData[2] > parseFloat($('#threshold').val()))
                    {
                        $(nRow).addClass('table-success');
                    }
                    else if(aData[2] < 0)
                    {
                        $(nRow).addClass('table-danger');
                    }
                    else
                    {
                        $(nRow).addClass('table-primary');
                    }

                    $(nRow).find('*').each(function () {
                        $(this).addClass('text-center');
                    });
                }
            });
        });

        function populateTable(data)
		{
			$('#table').DataTable().clear();
            var length = (Object.keys(data).length - 1) / 3;
            for(var i = 0; i < length; i++)
            {
                $('#table').dataTable().fnAddData([
                    data["scDoc_" + i],
                    data["srsDoc_" + i],
                    data["sv_" + i]
                ]);
            }
        }
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
								<div class="form-group">
									<label for="threshold">Similarity Threshold:</label>
									<input id="threshold" type="number" step="0.01" class="form-control" name="threshold" min="-1" max="1" value="0.70"/>
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
					<button id="more" class="btn btn-xs btn-info" style="margin-bottom: 20px" value="see more"><i class="fa fa-arrow-circle-o-down" aria-hidden="true"></i>Click here for more details...</button>
				</div>
			</div>
			<div class="row justify-content-center afterUpload" id="tableDetails" style="display: none">
				<div class="col-xs-12 text-center">
					<table class="table table-responsive" id="table">
						<thead>
							<tr class="table-dark">
								<th class="text-center">Source Code Document</th>
								<th class="text-center">External Document</th>
								<th class="text-center">Similarity Value</th>
							</tr>
						</thead>
					</table>
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
					<h3 class="section-subheading text-muted">This web based application is based on the research report named, <q>Recovering Documentation-to-Source-Code Traceability Links using Latent Semantic Indexing</q> accomplished by Mr. Andrian Marcus & Mr. Jonathan I. Maletic. It is developed by M. A. Nur Quraishi, who is supervised by Mr. Rayhanur Rahman. This application is built on Grails Framework with Groovy as programming language. The methodology is described below:</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<ul class="timeline">
						<li>
							<div class="timeline-image" style="background-color: slateblue">
								<h4>Source Code
									<br>and
									<br>External Docs</h4>
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>Step-1:</h4>
									<h4 class="subheading">Reading Input</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">In the first step, the application takes an external document as pdf file and Java source code as zip file.</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image">
								<h4><br>Corpus</h4>
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>Step-2:</h4>
									<h4 class="subheading">Building Corpus</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">In this step, the input files are parsed using <a href="https://pdfbox.apache.org/" target="_blank" style="text-decoration:none">Apache PDFBox</a> and <a href="https://javaparser.org" target="_blank" style="text-decoration:none">JavaParser</a> and lots of pre-processing techniques includes pdf section extraction, camel case splitting & method and variable extraction.</p>
								</div>
							</div>
						</li>
						<li>
							<div class="timeline-image">
								<h4>Vector
									<br>Space</h4>
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>Step-3:</h4>
									<h4 class="subheading">Constructing Vector Space</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Here, Latent Semantic Indexing (LSI) is applied on the corpus to produce term-by-document matrix where each column represents a document vector and each row represents term occurrence in each document.</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image">
								<h4>LSI
									<br>Subspace</h4>
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>Step-4:</h4>
									<h4 class="subheading">Dimension Reduction</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">After creating term-by-document matrix, Singular Value Decomposition (SVD) is used to reduce the matrix dimension and preclude the less frequently occurring terms.</p>
								</div>
							</div>
						</li>
						<li>
							<div class="timeline-image">
								<h4>Link
									<br>Recovery</h4>
							</div>
							<div class="timeline-panel">
								<div class="timeline-heading">
									<h4>Step-5:</h4>
									<h4 class="subheading">Identifying Links</h4>
								</div>
								<div class="timeline-body">
									<p class="text-muted">Finally, Similarity measures are calculated for each vector of source code with other vectors of external document. Similarity measure are computed by the cosine or inner product between the corresponding vectors, which increases as more terms are shared. If the calculated result for a link is greater than threshold (=0.7), then the link will be recovered.</p>
								</div>
							</div>
						</li>
						<li class="timeline-inverted">
							<div class="timeline-image" style="background-color: lightseagreen">
								<h4>
									<br>Result</h4>
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
						<img class="mx-auto rounded-circle" src="images/team/rayhanSir.jpg" alt="">
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
						<img class="mx-auto rounded-circle" src="images/team/nur.jpg" alt="">
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
						<img class="img-fluid d-block mx-auto" height="100px" width="100px" src="images/logos/iit.jpg" alt="">
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
