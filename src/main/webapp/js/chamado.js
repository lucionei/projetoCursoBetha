(function () {	
	
    function Chamado(id, cliente, equipamento, problema, emissao, aprovacao, tecnico, gerente, tipo, status) {
        this.id = id;
        this.cliente = cliente;
        this.equipamento = equipamento,
        this.problema = problema;
        this.emissao = emissao;
        this.aprovacao = aprovacao;
        this.tecnico = tecnico;
        this.gerente = tecnico;
        this.tipo = tipo;
        this.status = status;
    }

    function chamadoControler() {
		var tamanhoPagina = 10;
		var pagina = 0;			
        var repositorio;
        var itemAtual;
        var proximoId;
        var modeloLinhaTabela;
		var idRemover;
        var inserindo = false;

        function _carregar() {
            $.getJSON('banco/chamado.json', function(data) {
                repositorio = data;
                proximoId = repositorio.length + 1;
                _renderTable();
            });
        }

        _carregar();

        function _inserir() {
            $('.form-group').removeClass('has-error');
            itemAtual = new Chamado(proximoId++);
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
            $('select[name=cliente]').val(itemAtual.cliente.id);
            $('select[name=equipamento]').val(itemAtual.equipamento.id);
            $('textarea[name=problema]').val(itemAtual.problema);
            $('input[name=emissao]').val(formatDate(itemAtual.emissao));
            $('input[name=aprovacao]').val(formatDate(itemAtual.aprovacao));
            $('select[name=tecnico]').val(itemAtual.tecnico.id);
            $('select[name=gerente]').val(itemAtual.gerente.id);
            $('select[name=tipo]').val(itemAtual.tipo);
            $('select[name=status]').val(itemAtual.status);
        }

        function _parseForm() {
            itemAtual.id = $('input[name=id]').val();
            itemAtual.cliente.id = $('select[name=cliente]').val();
            itemAtual.equipamento.id = $('select[name=equipamento]').val();
            itemAtual.problema = $('textarea[name=problema]').val();
            itemAtual.emissao = formatDate($('input[name=emissao]').val());
            itemAtual.aprovacao = formatDate($('input[name=aprovacao]').val());
            itemAtual.tecnico.id = $('select[name=tecnico]').val();
            itemAtual.gerente.id = $('select[name=gerente]').val();
            itemAtual.tipo = $('select[name=tipo]').val();
            itemAtual.status = $('select[name=status]').val();
        }

        function _salvar() {
            if (_validarCampos()) {
                _parseForm();
                _renderTable();
                $("#cadastroChamado-modal").modal("hide");
                showMessage('success', 'Chamado salvo com sucesso!')
            }
        }

        function _renderTable() {
            var final = '';
            modeloLinhaTabela = modeloLinhaTabela || $('table.table tbody').html();
            if (repositorio.length == 0) {
                $('table.table tbody').html('<tr><td>Nenhum Chamado cadastrado.</td></tr>');
            }
            else {
                for (var i = pagina * tamanhoPagina; i < repositorio.length && i < (pagina + 1) *  tamanhoPagina; i++) {
                    var res = modeloLinhaTabela;
                    var linha = repositorio[i];
                    res = res.replace(/\(\(id\)\)/g, linha.id);
                    res = res.replace(/\(\(cliente.nome\)\)/g, linha.cliente.nome);
                    res = res.replace(/\(\(problema\)\)/g, linha.problema);
                    res = res.replace(/\(\(emissao\)\)/g, linha.emissao);
                    res = res.replace(/\(\(aprovacao\)\)/g, linha.aprovacao);
                    if (linha.status == '01') {
                        res = res.replace(/\(\(status\)\)/g, 'Aberto');
                    }
                    else if (linha.status == '02') {
                        res = res.replace(/\(\(status\)\)/g, 'Aprovado');
                    }   
                    else if (linha.status == '03') {
                        res = res.replace(/\(\(status\)\)/g, 'Cancelado');
                    }   
                    else if (linha.status == '04') {
                        res = res.replace(/\(\(status\)\)/g, 'Finalizado');
                    }   
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
            var retorno = true;
            $('#formChamado textarea[name=problema]').closest('.form-group').removeClass('has-error');
            if (!$('#formChamado textarea[name=problema]').val()) {
                $('#formChamado textarea[name=problema]').closest('.form-group').addClass('has-error');
                retorno =  false;
            }
            return retorno;
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
        window.ctrl = chamadoControler();
        $('#btnSalvar').click(function(){
            ctrl.salvar();
        });
        $('#btnNovo').click(function(){
            ctrl.inserir();
        });
        $('#btnCancelar').click(function(){
            ctrl.cancelar();
        });
        $("#btnFecharFormChamado").click(function(){
            ctrl.cancelar();
        });        
        $('#cadastroChamado-modal').keydown(function(e) {
            if (e.keyCode == 27) {
                ctrl.cancelar();
            }
        });                
    });
})();