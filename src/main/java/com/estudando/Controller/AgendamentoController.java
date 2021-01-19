package com.estudando.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudando.Repositories.AgendamentoRepository;
import com.estudando.modals.Agendamento;

import javassist.tools.rmi.ObjectNotFoundException;


@RestController
@RequestMapping(path="/agendamentos")
public class AgendamentoController {
	private AgendamentoRepository agendamentoRepository;
	
	public AgendamentoController(AgendamentoRepository agendamentoRepository) {
		super();
		this.agendamentoRepository = agendamentoRepository;
	}
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<List<Agendamento>> buscarTodos(){
		List <Agendamento> agendamentos = new ArrayList<>();
		agendamentos = agendamentoRepository.findAll();
		return new ResponseEntity<> (agendamentos, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<Optional<Agendamento>> buscar (@PathVariable Long id) throws ObjectNotFoundException {
		Optional<Agendamento> agendamento;
		try {
			agendamento = agendamentoRepository.findById(id);
			return new ResponseEntity<Optional<Agendamento>>(agendamento, HttpStatus.OK);
		} catch(NoSuchElementException nsee){
			return new ResponseEntity<Optional<Agendamento>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<Agendamento> salvar(@RequestBody Agendamento agendamento){
		agendamentoRepository.save(agendamento);
		return new ResponseEntity<>(agendamento, HttpStatus.OK);
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Optional<Agendamento>> deletarPorId(@PathVariable	 Long id){
		try {
			agendamentoRepository.deleteById(id);
			return new ResponseEntity<Optional<Agendamento>> (HttpStatus.OK);
		} catch(NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Agendamento>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(path="/{id}")
	public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @RequestBody Agendamento novoAgendamento){
		return agendamentoRepository.findById(id)
				.map(agendamento -> {
					agendamento.setPeriodo(novoAgendamento.getPeriodo());
					agendamento.setDataEvento(novoAgendamento.getDataEvento());
					agendamento.setValor(novoAgendamento.getValor());
					Agendamento agendamentoAtualizado= agendamentoRepository.save(agendamento);
					return ResponseEntity.ok().body(agendamentoAtualizado);
				}).orElse(ResponseEntity.notFound().build());
	}
}
