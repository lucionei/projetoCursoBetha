(function () {	
	
    function Cliente(id, nome, telefone, documento, email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.documento = documento;
        this.email = email;
    }

    function clienteControler() {
        var limitePagina = 10;
	    var pagina = 0;			
        var modeloLinhaTabela;
	    var idRemover;

        function _carregar(pesquisar) {
            if (pesquisar) {
                if ($('#formPesquisar input[name=dadoPesquisa]').val()) {
                    var dadoPesquisa = $('#formPesquisar input[name=dadoPesquisa]').val();
                    $.getJSON('api/clientes?pagina='+(pagina+1)+'&limitePagina='+limitePagina+'&pesquisa='+dadoPesquisa, function(data) {
                        if (data.length >= 0 || pagina > 0) {
                            _renderTable(data);
                        }
                        else{
                            showMessage('warning', 'Nada encontrado!');
                        }
                        $('#proximo').prop('disabled', data.length == 0 || data.length < limitePagina);
                    });
                }
            }
            else { 
                $.getJSON('api/clientes?pagina='+(pagina+1)+'&limitePagina='+limitePagina, function(data) {
                    if (data.length >= 0 || pagina > 0) {
                        _renderTable(data);   
                    }
                    $('#proximo').prop('disabled', data.length == 0 || data.length < limitePagina);
                });
            }
        }

        _carregar();

        function _pesquisar() {
            _carregar(true);
        }

        function _inserir() {
            var titulo = 'Cadastro de Clientes <samp id="idModalLabelCadastro">((id))</samp>';
            $('.form-group').removeClass('has-error');
            $('#modalLabelCadastro').html(titulo);
            $('#idModalLabelCadastro').addClass('hide');            
            _preencheForm(new Cliente());
        }

        function _preencheForm(registro) {
            if (registro.id != null) {
                var id = $('#idModalLabelCadastro').html();
                id = id.replace(/\(id\)/g, registro.id);
                $('#idModalLabelCadastro').html(id);            
            }    
            $('input[name=id]').val(registro.id);
            $('input[name=nome]').val(registro.nome);
            $('input[name=telefone]').val(registro.telefone);
            $('input[name=documento]').val(registro.documento);
            $('input[name=email]').val(registro.email);
        }

        function _salvar() {
            if (_validarCampos()) {
                $.post('api/clientes', $('form[role=cadastro]').serialize(), function () {
                    _carregar();
                    showMessage('success', 'Cliente salvo com sucesso!');
                }).error(function (data) {
                    showMessage('danger', data.status + ' - Erro ao salvar cliente! &nbsp' + data.responseText);
                });
                $("#cadastroCliente-modal").modal("hide");
            }
        }

        function _renderTable(repositorio) {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum cliente cadastrado.</td></tr>');
            }
            else {
                for (var i = 0; i < repositorio.length; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(nome\)\)/g, linha.nome);
                    res = res.replace(/\(\(telefone\)\)/g, setMascaraTelefone(linha.telefone));
                    final += res;
                }
                $('table.table tbody').html(final);
            }
            $('#anterior').prop('disabled', pagina == 0);
        }
		
	    function _setRemove(id) {
            idRemover = id;
	    }

        function _remove() {
            $.ajax({
                url: "api/clientes?id=" + idRemover,
                type: "DELETE"
            }).done(function () {
                _carregar();
            }).error(function (data) {
                showMessage('danger', data.status + ' - Erro ao excluir cliente!');
            });
            idRemover = 0;
        }

        function _editar(id) {
            $('#idModalLabelCadastro').removeClass();
            var titulo = 'Editar Cliente <samp id="idModalLabelCadastro">((id))</samp>';
            $('#modalLabelCadastro').html(titulo);
            $.getJSON('api/clientes', 'id=' + id, function (data) {
                _preencheForm(data);
            })

        }
		
		function _anterior() {
			if (pagina > 0) {
				pagina--;
				_carregar();
			}
		}
		
		function _proximo() {
            pagina++;
            _carregar();
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
            return retorno;
        }        

        return {
            inserir: _inserir,
            editar: _editar,
            salvar: _salvar,
            setRemove: _setRemove,
            remove: _remove,
            anterior: _anterior,
            proximo: _proximo,
            pesquisar: _pesquisar
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
        $('#btnPesquisar').click(function(){
            ctrl.pesquisar();
        });
        $('#formCliente input[name=telefone]').inputmask({mask: ['(99) 9999-9999', '(99) 99999-9999'], keepStatic: true });
        $("#formCliente input[name=documento]").inputmask({mask: ['999.999.999-99', '99.999.999/9999-99'], keepStatic: true });
    });
})();