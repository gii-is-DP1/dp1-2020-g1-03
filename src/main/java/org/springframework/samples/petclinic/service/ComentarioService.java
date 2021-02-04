package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CitaMascotaRepository;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.ComentarioRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.exceptions.ComentariosMaximoPorCitaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ComentarioService {
	
	private ComentarioRepository comentarioRepository;
	private OwnerRepository ownerRepository;
	private CitaMascotaRepository citaMascotaRepository;
	private CitaRepository citaRepository;
	
	@Autowired
	public ComentarioService(ComentarioRepository comentarioRepository,
			VetRepository vetRepository, OwnerRepository ownerRepository, CitaMascotaRepository citaMascotaRepository,CitaRepository citaRepository) {
		this.comentarioRepository = comentarioRepository;
		this.ownerRepository = ownerRepository;
		this.citaMascotaRepository = citaMascotaRepository;
		this.citaRepository = citaRepository;
	}

	
	@Transactional(readOnly = true)
	public Vet findVetById(int id) throws DataAccessException {
		return comentarioRepository.findVetById(id);
	}
	@Transactional(readOnly = true)
	public Owner findByOwnerId(int id) throws DataAccessException {
		return ownerRepository.findById(id);
	}
	@Transactional()
	public void saveComentario(Comentario comentario) throws DataAccessException,  ComentariosMaximoPorCitaException{
		Vet vet = comentario.getVet();
		Owner owner = comentario.getOwner();
		int citasOwnerConVet = this.citaRepository.findCitasOwnerConVet(vet, owner);
		int comentariosHechosOwnerAVet = this.comentarioRepository.findComentariosOwnerConVet(vet, owner);
		if(comentariosHechosOwnerAVet>=citasOwnerConVet || citasOwnerConVet==0) {
			throw new ComentariosMaximoPorCitaException();
		}
		comentarioRepository.save(comentario);               
	}

	@Transactional(readOnly = true)
	public Collection<Comentario> findAllComentariosByVetId(int vetId) throws DataAccessException{
		return comentarioRepository.findComentariosByVetId(vetId);
	}
	
	@Transactional(readOnly = true)
	public Collection<Comentario> findAllComentariosByOwner(Owner owner) throws DataAccessException{
		return comentarioRepository.findComentariosByOwner(owner);
	}
	
	@Transactional(readOnly = true)
	public Comentario findComentarioByComentarioId(int comentarioId) throws DataAccessException{
		return comentarioRepository.findById(comentarioId);
	}

	@Transactional(readOnly = true)
	public Collection<Comentario> findAllComentariosByOwnerId(int i) throws DataAccessException{
		return comentarioRepository.findComentariosByOwnerId(i);
	}
}
