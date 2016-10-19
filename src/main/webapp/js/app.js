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
		.fadeTo(1000, 1000)
		.slideUp(1000, function(){
    		$('#mensagem').hide();
		});
}

function formatDate(value) {
    if (value)
        return new Date(value).toLocaleString('pt-BR');
    return "";
}

function setMascaraTelefone(value){
    value=value.replace(/\D/g,"");             //Remove tudo o que não é dígito
    value=value.replace(/^(\d{2})(\d)/g,"($1) $2"); //Coloca parênteses em volta dos dois primeiros dígitos
    value=value.replace(/(\d)(\d{4})$/,"$1-$2");    //Coloca hífen entre o quarto ou quinto dígitos
    return value;
}