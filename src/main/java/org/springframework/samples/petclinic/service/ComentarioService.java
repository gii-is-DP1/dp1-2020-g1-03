package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CitaMascotaRepository;
import org.springframework.samples.petclinic.repository.ComentarioRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.exceptions.ComentariosMaximoPorCitaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ComentarioService {
	
	private ComentarioRepository comentarioRepository;
	private VetRepository vetRepository;
	private OwnerRepository ownerRepository;
	private CitaMascotaRepository citaMascotaRepository;
	
	@Autowired
	public ComentarioService(ComentarioRepository comentarioRepository,
			VetRepository vetRepository, OwnerRepository ownerRepository, CitaMascotaRepository citaMascotaRepository) {
		this.comentarioRepository = comentarioRepository;
		this.vetRepository = vetRepository;
		this.ownerRepository = ownerRepository;
		this.citaMascotaRepository = citaMascotaRepository;
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
		int idVet = comentario.getVet().getId();
		int idOwner = comentario.getOwner().getId();
		int citasOwnerConVet = this.citaMascotaRepository.findCitasOwnerConVet(idVet, idOwner);
		int comentariosHechosOwnerAVet = this.comentarioRepository.findComentariosOwnerConVet(idVet, idOwner);
		if(comentariosHechosOwnerAVet>=citasOwnerConVet || citasOwnerConVet==0) {
			throw new ComentariosMaximoPorCitaException();
		}
		comentarioRepository.save(comentario);               
	}


	public Collection<Comentario> findAllComentariosByVetId(int vetId) throws DataAccessException{
		return comentarioRepository.findComentariosByVetId(vetId);
	}
	public Collection<Comentario> findAllComentariosByOwnerId(int ownerId) throws DataAccessException{
		return comentarioRepository.findComentariosByOwnerId(ownerId);
	}
//	public int findVetIdByUsername(String user) throws DataAccessException {
//		return this.comentarioRepository.findVetIdByUsername(user);
//	}
	public Comentario findComentarioByComentarioId(int comentarioId) throws DataAccessException{
		return comentarioRepository.findById(comentarioId);
	}
}
