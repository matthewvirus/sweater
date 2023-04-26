let $grid = $('#masonry').masonry({
    itemSelector: '.col',
    percentPosition: true
});

$grid.imagesLoaded().progress( function() {
    $grid.masonry();
});