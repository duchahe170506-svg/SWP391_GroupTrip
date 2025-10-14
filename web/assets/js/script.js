(function($) {

    "use strict";

        // init Chocolat light box
        var initChocolat = function () {
          Chocolat(document.querySelectorAll('.image-link'), {
            imageSize: 'contain',
            loop: true,
          })
        }

        // init jarallax parallax
    var initJarallax = function () {
      jarallax(document.querySelectorAll(".jarallax"));

      jarallax(document.querySelectorAll(".jarallax-img"), {
        keepImg: true,
      });
    }
//search pop

var searchPopup = function () {
  // open search box
  $('#header').on('click', '.search-button', function (e) {
    $('.search-popup').toggleClass('is-visible');
  });

  $('#header').on('click', '.btn-close-search', function (e) {
    $('.search-popup').toggleClass('is-visible');
  });

  $(".search-popup-trigger").on("click", function (b) {
    b.preventDefault();
    $(".search-popup").addClass("is-visible"),
      setTimeout(function () {
        $(".search-popup").find("#search-popup").focus()
      }, 350)
  }),
    $(".search-popup").on("click", function (b) {
      ($(b.target).is(".search-popup-close") || $(b.target).is(".search-popup-close svg") || $(b.target).is(".search-popup-close path") || $(b.target).is(".search-popup")) && (b.preventDefault(),
        $(this).removeClass("is-visible"))
    }),
    $(document).keyup(function (b) {
      "27" === b.which && $(".search-popup").removeClass("is-visible")
    })
}

    $(document).ready(function() {
      

      /* Video */
      var $videoSrc;  
        $('.play-btn').click(function() {
          $videoSrc = $(this).data( "src" );
        });

        $('#myModal').on('shown.bs.modal', function (e) {

        $("#video").attr('src',$videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0" ); 
      })

      $('#myModal').on('hide.bs.modal', function (e) {
        $("#video").attr('src',$videoSrc); 
      })
        
      var swiper = new Swiper(".slider", {
        effect: "fade",
        pagination: {
          el: ".slider-pagination",
          clickable: true,
        },
      });

      var swiper = new Swiper(".testimonial-swiper", {
        slidesPerView: 1,
        spaceBetween: 20,
        pagination: {
          el: ".testimonial-pagination",
          clickable: true,
        },
      });

      var swiper = new Swiper(".services-swiper", {
        slidesPerView: 5,
        spaceBetween: 120,
        freeMode: true,
        pagination: {
          el: ".services-pagination",
          clickable: true,
        },
        breakpoints: {
          0: {
            slidesPerView: 1,
          },
          572: {
            slidesPerView: 3,
          },
          1024: {
            slidesPerView: 4,
          },
          1280: {
            slidesPerView: 5,
          },
        },
      });

          // product single page
    var thumb_slider = new Swiper(".product-thumbnail-slider", {
      autoplay: true,
      loop: true,
      spaceBetween: 8,
      slidesPerView: 4,
      freeMode: true,
      watchSlidesProgress: true,
    });

    var large_slider = new Swiper(".product-large-slider", {
      autoplay: true,
      loop:true,
      spaceBetween: 10,
      effect: 'fade',
      thumbs: {
        swiper: thumb_slider,
      },
    });

    


      window.addEventListener("load", (event) => {
        //isotope
        $('.isotope-container').isotope({
          // options
          itemSelector: '.item',
          layoutMode: 'masonry',
        });
  
  
  
        // Initialize Isotope
        var $container = $('.isotope-container').isotope({
          // options
          itemSelector: '.item',
          layoutMode: 'masonry',
        });

        initChocolat();
        searchPopup();
      initJarallax();
  
        $(document).ready(function () {
          //active button
          $('.filter-button').click(function () {
            $('.filter-button').removeClass('active');
            $(this).addClass('active');
          });
        });
  
        // Filter items on button click
        $('.filter-button').click(function () {
          var filterValue = $(this).attr('data-filter');
          if (filterValue === '*') {
            // Show all items
            $container.isotope({ filter: '*' });
          } else {
            // Show filtered items
            $container.isotope({ filter: filterValue });
          }
        });
  
      });



    }); // End of a document

})(jQuery);