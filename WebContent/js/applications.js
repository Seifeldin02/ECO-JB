$(document).ready(function() {
    var $tables = $('#tables');
    var $applicationsTable = $('#applicationsTable');
    var $carbonTable = $('#carbonTable');

    // Slide between tables
    $applicationsTable.on('swiperight', function() {
        $tables.css('transform', 'translateX(-100%)');
    });

    $carbonTable.on('swipeleft', function() {
        $tables.css('transform', 'translateX(0)');
    });

    // Search functionality
    $('#search').on('keyup', function() {
        var value = $(this).val().toLowerCase();

        $('#applicationsTable tbody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
});