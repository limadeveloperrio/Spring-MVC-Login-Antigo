package br.com.bruno.meta.controller;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bruno.meta.dao.JDBCTarefaDao;
import br.com.bruno.meta.dao.TarefaDao;
import br.com.bruno.meta.modelo.Tarefa;

@Controller
public class MetasController {

	@RequestMapping("novaTarefa")
	public String form() {
		return "tarefa/formulario";
	}

	@RequestMapping("adicionaTarefa")
	public String adiciona(@Valid Tarefa tarefa, BindingResult result) {
		if (result.hasFieldErrors("descricao")) {
			return "tarefa/formulario";
		}
		JDBCTarefaDao dao = new JDBCTarefaDao();
		dao.adiciona(tarefa);
		return "tarefa/adicionada";
	}

	@RequestMapping("listaTarefas")
	public String lista(Model model) {
		JDBCTarefaDao dao = new JDBCTarefaDao();
		model.addAttribute("tarefas", dao.lista());
		return "tarefa/lista";
	}

	@RequestMapping("removeTarefa")
	public String remove(Tarefa tarefa) {
		JDBCTarefaDao dao = new JDBCTarefaDao();
		dao.remove(tarefa);
		return "redirect:listaTarefas";
	}

	@RequestMapping("mostraTarefa")
	public String mostra(Long id, Model model) {
		JDBCTarefaDao dao = new JDBCTarefaDao();
		model.addAttribute("tarefa", dao.buscaPorId(id));
		return "tarefa/mostra";
	}

	@RequestMapping("alteraTarefa")
	public String alterar(Tarefa tarefa) {
		JDBCTarefaDao dao = new JDBCTarefaDao();
		dao.altera(tarefa);
		return "redirect:listaTarefas";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Calendar.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String value) {

				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(value));
					setValue(cal);

				} catch (ParseException e) {
					setValue(null);
				}
			}

			@Override
			public String getAsText() {
				if (getValue() == null) {
					return "";
				}
				return new SimpleDateFormat("dd/MM/yyyy").format(((Calendar) getValue()).getTime());
			}
		});

	}
}
