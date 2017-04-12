(function ($) {
    var url = AJS.contextPath() + "/rest/jrgisync/1.0/";

    $(document).ready(function() {
        $.ajax({
            url: url,
            dataType: "json"
        })
        .done(function(config) {
            $("#name").val(config.name);
            $("#time").val(config.time);
        });
    });
})(AJS.$ || jQuery);