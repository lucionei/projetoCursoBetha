(function () {	
	
    function Cliente(id, nome, documento, telefone, email) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
    }

    function clienteControler() {
        var tamanhoPagina = 10;
	var pagina = 0;			
        var modeloLinhaTabela;
	var idRemover;

        function _carregar() {
            $.getJSON('api/clientes', function(data) {
                _renderTable(data);
            });
        }

        _carregar();

        function _inserir() {
            $('.form-group').removeClass('has-error');
            _preencheForm(new Cliente());
        }

        function _preencheForm(registro) {
            $('input[name=id]').val(registro.id);
            $('input[name=nome]').val(registro.nome);
            $('input[name=documento]').val(registro.documento);
            $('input[name=telefone]').val(registro.telefone);
            $('input[name=email]').val(registro.email);
        }

        function _salvar() {
            if (_validarCampos()) {
                $.post('api/clientes', $('form[role=cadastro]').serialize(), function () {
                    _carregar();
                });
                $("#cadastroCliente-modal").modal("hide");
                showMessage('success', 'Cliente salvo com sucesso!')
            }
        }

        function _renderTable(repositorio) {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum cliente cadastrado.</td></tr>');
            }
            else {
                for (var i = pagina * tamanhoPagina; i < repositorio.length && i < (pagina + 1) *  tamanhoPagina; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(nome\)\)/g, linha.nome);
                    res = res.replace(/\(\(telefone\)\)/g, linha.telefone);
                    res = res.replace(/\(\(email\)\)/g, linha.email);
                    final += res;
                }
                $('table.table tbody').html(final);
            }
            if (repositorio.length > 0) {
			    $('#numeracao').text((pagina + 1) + ' de ' + Math.ceil(repositorio.length / tamanhoPagina));
            }
            else {
                $('#numeracao').text('0 de 0');
            }  
			$('#primeiro').prop('disabled', repositorio.length <= tamanhoPagina || pagina == 0);
			$('#anterior').prop('disabled', repositorio.length <= tamanhoPagina || pagina == 0);
			$('#proximo').prop('disabled', repositorio.length <= tamanhoPagina || pagina >= Math.ceil(repositorio.length / tamanhoPagina) - 1);
			$('#ultimo').prop('disabled', repositorio.length <= tamanhoPagina || pagina == Math.ceil(repositorio.length / tamanhoPagina) - 1);
        }
		
		function _setRemove(id) {
			idRemover = id;
		}

        function _remove() {
            $.ajax({
                method: "DELETE",
                url: "api/clientes",
                data: { id: idRemover }
            }).done(function () {
                _carregar();
            });
            _carregar();
			idRemover = 0;
        }

        function _editar(id) {
            $.getJSON('api/clientes', 'id=' + id, function (data) {
                _preencheForm(data);
            })

        }
		
		function _primeiro() {
			if (pagina > 0) {
				pagina = 0;
				_carregar();
			}	
		}
		
		function _anterior() {
			if (pagina > 0) {
				pagina--;
				_carregar();
			}
		}
		
		function _proximo() {
			if (pagina < repositorio.length / tamanhoPagina - 1) {
				pagina++;
				_carregar();
			}
		}
		
		function _ultimo() {
			if (pagina < repositorio.length / tamanhoPagina - 1) {
				pagina = Math.ceil(repositorio.length / tamanhoPagina) - 1;
				_carregar();
			}			
		}

        function _validarCampos() {
            var retorno = true;
            $('#formCliente input[name=nome]').closest('.form-group').removeClass('has-error');
            if (!$('#formCliente input[name=nome]').val()) {
                $('#formCliente input[name=nome]').closest('.form-group').addClass('has-error');
                retorno =  false;
            }
            $('#formCliente input[name=telefone]').closest('.form-group').removeClass('has-error');
            if (!$('#formCliente input[name=telefone]').val()) {
                $('#formCliente input[name=telefone]').closest('.form-group').addClass('has-error');
                retorno = false;
            }
            $('#formCliente input[name=documento]').closest('.form-group').removeClass('has-error');
            if (!$('#formCliente input[name=documento]').val()) {
                $('#formCliente input[name=documento]').closest('.form-group').addClass('has-error');
                retorno = false;
            }
            $('#formCliente input[name=email]').closest('.form-group').removeClass('has-error');
            if (!$('#formCliente input[name=email]').val()) {
                $('#formCliente input[name=email]').closest('.form-group').addClass('has-error');
                retorno = false;
            }
            return retorno;
        }        

        return {
            inserir: _inserir,
            editar: _editar,
            salvar: _salvar,
			setRemove: _setRemove,
            remove: _remove,
			primeiro: _primeiro,
			anterior: _anterior,
			proximo: _proximo,
			ultimo: _ultimo
			
        }
    }

    $(function(){        
        window.ctrl = clienteControler();
        $('#btnSalvar').click(function(){
            ctrl.salvar();
        });
        $('#btnNovo').click(function(){
            ctrl.inserir();
        });
        $('#btnCancelar').click(function(){
            ctrl.cancelar();
        });
        $("#btnFecharFormCliente").click(function(){
            ctrl.cancelar();
        });        
    });
})();