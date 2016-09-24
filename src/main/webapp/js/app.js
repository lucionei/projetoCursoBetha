(function () {	
	$(function(){
		$.get('menu.html', function(data) {
			$('#menu').html(data);
		});
		$.get('paginacao.html', function(data) {
			$('#paginacao').html(data);
		});		
	});
	$('#mensagem').hide();
})();

function showMessage(type, message) {
	$('#mensagem')
		.removeClass('alert-success')
		.removeClass('alert-info')
		.removeClass('alert-warning')
		.removeClass('alert-danger')
		.addClass('alert-' + type).html('<p>'+message+'</p>').show()
		.fadeTo(500, 500)
		.slideUp(500, function(){
    		$('#mensagem').hide();
		});
}

function formatDate(value) {
    if (value)
        return new Date(value).toLocaleString('pt-BR');
    return "";
}