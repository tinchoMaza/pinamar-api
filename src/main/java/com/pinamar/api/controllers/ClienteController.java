package com.pinamar.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinamar.api.exceptions.ClienteException;
import com.pinamar.api.negocio.Cliente;
import com.pinamar.api.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	private final ClienteService clientesServ;
	private Cliente c;
	
	@Autowired
	public ClienteController (ClienteService cliServ) {
		this.clientesServ = cliServ;
	}

	@GetMapping("")
	public ResponseEntity<List<Cliente>> getAllClientes() {
		return ResponseEntity.ok(clientesServ.getAllClientes());
	}
	
	@GetMapping("/{_id}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable("_id") String _id) throws ClienteException{
		try {
			c = clientesServ.findById(_id);
		}
		catch(ClienteException e) {
			c = null;
		}
		return ResponseEntity.ok(c);
	}
	
	@PostMapping("/")
	public ResponseEntity<Cliente> saveCliente(@RequestBody @Valid Cliente c){
		//debo recibir el objeto completo pero como tengo dos array, creo que debe mandar los arrays inicilizados y nada mas
		return ResponseEntity.ok(clientesServ.saveCliente(c));
	}
	
	@PutMapping("/")
	public ResponseEntity<Cliente> updateCliente(@RequestBody @Valid Cliente c){
		//hay que mandarle el id, si no te crea uno nuevo con otro id. Por lo que con lo que me manda, hago un get y despues actualizo los campos diferentes
		// los arrays se actualizan por otro metodo
		//si es persona fisica o juridica me lo tiene pasar. un boolean no puede ser null, por eso no verifico
		Cliente aux = clientesServ.findById(c.getId());
		if(c.getCuit() != null)
			aux.setCuit(c.getCuit());;
		if(c.getNombre() != null)
			aux.setNombre(c.getNombre());
		if(c.getPassword() != null)
			aux.setPassword(c.getPassword());
		return ResponseEntity.ok(clientesServ.saveCliente(aux)); //el metodo en el repo del save y update hacen lo mismo -> un save
	}
	
	
	
	@DeleteMapping("/{_id}")
	public ResponseEntity<Void> deleteCliente(@PathVariable String _id){
		clientesServ.deleteCliente(_id);
		return ResponseEntity.noContent().build();
	}

}