(function () {	
	
    function Equipamento(id, descricao, tipo) {
        this.id = id;
        this.descricao = descricao;
    }

    function equipamentoControler() {
        var limitePagina = 10;
	    var pagina = 0;			
        var modeloLinhaTabela;
	    var idRemover;

        function _carregar(pesquisar) {
            if (pesquisar) {
                if ($('#formPesquisar input[name=dadoPesquisa]').val()) {
                    var dadoPesquisa = $('#formPesquisar input[name=dadoPesquisa]').val();
                    $.getJSON('api/equipamentos?pagina='+(pagina+1)+'&limitePagina='+limitePagina+'&pesquisa='+dadoPesquisa, function(data) {
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
                $.getJSON('api/equipamentos?pagina='+(pagina+1)+'&limitePagina='+limitePagina, function(data) {
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
            var titulo = 'Cadastro de Equipamentos <samp id="idModalLabelCadastro">((id))</samp>';
            $('.form-group').removeClass('has-error');
            $('#modalLabelCadastro').html(titulo);
            $('#idModalLabelCadastro').addClass('hide');
            _preencheForm(new Equipamento());
        }

        function _preencheForm(registro) {
            if (registro.id != null) {
                var id = $('#idModalLabelCadastro').html();
                id = id.replace(/\(id\)/g, registro.id);
                $('#idModalLabelCadastro').html(id);            
            }    
            $('input[name=id]').val(registro.id);
            $('input[name=descricao]').val(registro.descricao);
        }

        function _salvar() {
            if (_validarCampos()) {
                $.post('api/equipamentos', $('form[role=cadastro]').serialize(), function () {
                    _carregar();
                    showMessage('success', 'Equipamento salvo com sucesso!');
                }).error(function (data) {
                    showMessage('danger', data.status + ' - Erro ao salvar equipamento! &nbsp' + data.responseText);
                });
                $("#cadastroEquipamento-modal").modal("hide");
            }
        }

        function _renderTable(repositorio) {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum equipamento cadastrado.</td></tr>');
            }
            else {
                for (var i = 0; i < repositorio.length; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(descricao\)\)/g, linha.descricao);
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
                url: "api/equipamentos?id=" + idRemover,
                type: "DELETE"
            }).done(function () {
                _carregar();
            }).error(function (data) {
                showMessage('danger', data.status + ' - Erro ao excluir equipamento!');
            });
            idRemover = 0;
        }

        function _editar(id) {
            $('#idModalLabelCadastro').removeClass();
            var titulo = 'Editar Equipamento <samp id="idModalLabelCadastro">((id))</samp>';
            $('#modalLabelCadastro').html(titulo);
            $.getJSON('api/equipamentos', 'id=' + id, function (data) {
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
            $('#formEquipamento input[name=descricao]').closest('.form-group').removeClass('has-error');
            if (!$('#formEquipamento input[name=descricao]').val()) {
                $('#formEquipamento input[name=descricao]').closest('.form-group').addClass('has-error');
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
        window.ctrl = equipamentoControler();
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