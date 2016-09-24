(function () {	
	
    function Equipamento(id, nome, tipo) {
        this.id = id;
        this.descricao = nome;
    }

    function equipamentoControler() {
		var tamanhoPagina = 10;
		var pagina = 0;			
        var repositorio;
        var itemAtual;
        var proximoId;
        var modeloLinhaTabela;
		var idRemover;
        var inserindo = false;

        function _carregar() {
            $.getJSON('banco/equipamento.json', function(data) {
                repositorio = data;
                proximoId = repositorio.length + 1;
                _renderTable();
            });
        }

        _carregar();

        function _inserir() {
            $('.form-group').removeClass('has-error');
            itemAtual = new Equipamento(proximoId++);
            repositorio.push(itemAtual);
            _preencheForm(); 
            inserindo = true;           
        }

        function _cancelar() {
            if (inserindo) {
                var linha = repositorio[repositorio.length - 1];
                _setRemove(linha.id);
                _remove();
                proximoId--;
            }
            inserindo = false;
        }

        function _preencheForm() {
            $('input[name=id]').val(itemAtual.id);
            $('input[name=descricao]').val(itemAtual.descricao);
        }

        function _parseForm() {
            itemAtual.id = $('input[name=id]').val();
            itemAtual.descricao = $('input[name=descricao]').val();
        }

        function _salvar() {
            if (_validarCampos()) {
                _parseForm();
                _renderTable();
                $("#cadastroEquipamento-modal").modal("hide");
                showMessage('success', 'Equipamento salvo com sucesso!')
            }
        }

        function _renderTable() {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum equipamento cadastrado.</td></tr>');
            }
            else {
                for (var i = pagina * tamanhoPagina; i < repositorio.length && i < (pagina + 1) *  tamanhoPagina; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(descricao\)\)/g, linha.descricao);
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
			var ultimaPagina;
            for (var i = 0; i < repositorio.length; i++) {
                var item = repositorio[i];
                if (item.id == idRemover) {
					if (pagina == Math.ceil(repositorio.length / tamanhoPagina) - 1) {
						ultimaPagina = 1;
					}					
                    repositorio.splice(i, 1);
                    if (repositorio.length == 0) {
                        pagina = 0;
                    }
					if (ultimaPagina == 1 && repositorio.length > 0 && pagina != Math.ceil(repositorio.length / tamanhoPagina) - 1) {
						pagina = Math.ceil(repositorio.length / tamanhoPagina) - 1;
					}
                    break;
                }
            }
            _renderTable();
			idRemover = 0;
        }

        function _editar(id) {
            for (var i = 0; i < repositorio.length; i++) {
                var item = repositorio[i];
                if (item.id == id) {
                    itemAtual = item;
                    break;
                }
            }
            _preencheForm();
            inserindo = false;
        }
		
		function _primeiro() {
			if (pagina > 0) {
				pagina = 0;
				_renderTable();
			}	
		}
		
		function _anterior() {
			if (pagina > 0) {
				pagina--;
				_renderTable();
			}
		}
		
		function _proximo() {
			if (pagina < repositorio.length / tamanhoPagina - 1) {
				pagina++;
				_renderTable();
			}
		}
		
		function _ultimo() {
			if (pagina < repositorio.length / tamanhoPagina - 1) {
				pagina = Math.ceil(repositorio.length / tamanhoPagina) - 1;
				_renderTable();
			}			
		}

        function _validarCampos() {
            $('#formEquipamento input[name=descricao]').closest('.form-group').removeClass('has-error');
            if (!$('#formEquipamento input[name=descricao]').val()) {
                $('#formEquipamento input[name=descricao]').closest('.form-group').addClass('has-error');
                return false;
            }
            return true;
        }        

        return {
            inserir: _inserir,
            cancelar: _cancelar,
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
        window.ctrl = equipamentoControler();
        $('#btnSalvar').click(function(){
            ctrl.salvar();
        });
        $('#btnNovo').click(function(){
            ctrl.inserir();
        });

        $('#btnCancelar').click(function(){
            ctrl.cancelar();
        })
        $("#btnFecharFormEquipamento").click(function(){
            ctrl.cancelar();
        });
        $('#cadastroEquipamento-modal').keydown(function(e) {
            if (e.keyCode == 27) {
                ctrl.cancelar();
            }
        });                
    });
})();