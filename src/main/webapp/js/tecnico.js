(function () {	
	
    function Tecnico(id, nome, tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    function tecnicoControler() {
		var limitePagina = 10;
	    var pagina = 0;			
        var modeloLinhaTabela;
	    var idRemover;

        function _carregar(pesquisar) {
            if (pesquisar) {
                if ($('#formPesquisar input[name=dadoPesquisa]').val()) {
                    var dadoPesquisa = $('#formPesquisar input[name=dadoPesquisa]').val();
                    $.getJSON('api/tecnicos?pagina='+(pagina+1)+'&limitePagina='+limitePagina+'&pesquisa='+dadoPesquisa, function(data) {
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
                $.getJSON('api/tecnicos?pagina='+(pagina+1)+'&limitePagina='+limitePagina, function(data) {
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
            var titulo = 'Cadastro de Técnicos <samp id="idModalLabelCadastro">((id))</samp>';
            $('.form-group').removeClass('has-error');
            $('#modalLabelCadastro').html(titulo);
            $('#idModalLabelCadastro').addClass('hide');
            _preencheForm(new Tecnico());
            $('select[name=tipo]').val('T');
        }

        function _preencheForm(registro) {
            if (registro.id != null) {
                var id = $('#idModalLabelCadastro').html();
                id = id.replace(/\(id\)/g, registro.id);
                $('#idModalLabelCadastro').html(id); 
                $('select[name=tipo]').val(registro.tipo);           
            }    
            $('input[name=id]').val(registro.id);
            $('input[name=nome]').val(registro.nome);
        }

        function _salvar() {
            var api = 'api/tecnicos';
            if (_validarCampos()) {
                if ($('#formTecnico select[name=tipo]').val() == 'G') {
                    api = 'api/gerentes';
                }
                $.post(api, $('form[role=cadastro]').serialize(), function () {
                    _carregar();
                    showMessage('success', 'Técnico salvo com sucesso!');
                }).error(function (data) {
                    showMessage('danger', data.status + ' - Erro ao salvar técnico! &nbsp' + data.responseText);
                });
                $("#cadastroTecnico-modal").modal("hide");
            }
        }

        function _renderTable(repositorio) {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum técnico cadastrado.</td></tr>');
            }
            else {
                for (var i = 0; i < repositorio.length; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(nome\)\)/g, linha.nome);
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
                url: "api/tecnicos?id=" + idRemover,
                type: "DELETE"
            }).done(function () {
                _carregar();
            }).error(function (data) {
                showMessage('danger', data.status + ' - Erro ao excluir técnico!');
            });
            idRemover = 0;
        }

        function _editar(id) {
            $('#idModalLabelCadastro').removeClass();
            var titulo = 'Editar Técnico <samp id="idModalLabelCadastro">((id))</samp>';
            $('#modalLabelCadastro').html(titulo);
            $.getJSON('api/tecnicos', 'id=' + id, function (data) {
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
            $('#formTecnico input[name=nome]').closest('.form-group').removeClass('has-error');
            if (!$('#formTecnico input[name=nome]').val()) {
                $('#formTecnico input[name=nome]').closest('.form-group').addClass('has-error');
                retorno =  false;
            }
            $('#formTecnico select[name=tipo]').closest('.form-group').removeClass('has-error');
            if (!$('#formTecnico select[name=tipo]').val()) {
                $('#formTecnico select[name=tipo]').closest('.form-group').addClass('has-error');
                retorno =  false;
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
        window.ctrl = tecnicoControler();
        $('#btnSalvar').click(function(){
            ctrl.salvar();
        });
        $('#btnNovo').click(function(){
            ctrl.inserir();
        });
        $('#btnPesquisar').click(function(){
            ctrl.pesquisar();
        });
    });
})();