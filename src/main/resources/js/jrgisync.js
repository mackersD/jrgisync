(function ($) {
    var url = AJS.contextPath() + "/rest/jrgisync/1.0/";

    $(document).ready(function() {
        $.ajax({
            url: url,
            dataType: "json"
        })
        .done(function(config) {
            console.log("this was loaded!!!");
            $("#defaultProject").val(config.defaultProject);
            $("#defaultUser").val(config.defaultUser);
        });
    });   
})(AJS.$ || jQuery);

function updateConfig() {
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/jrgisync/1.0/",
        type: "PUT",
        contentType: "application/json",
        data: '{"defaultProject": "' + AJS.$("#defaultProject").attr("value") + '",' + 
               '"defaultUser: "' + AJS.$("#defaultUser").attr("value") + '}' ,
        processData: false
    });
} 

AJS.$("#admin").submit(function(e) {
    e.preventDefault();
    updateConfig();
});