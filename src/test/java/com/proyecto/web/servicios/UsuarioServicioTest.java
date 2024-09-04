package com.proyecto.web.servicios;

import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.modelos.TipoUsuario;
import com.proyecto.web.modelos.Usuario;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServicioTest {

    @Mock
    private UsuarioRepositorio usuarioRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John");
        usuario.setApellido("Doe");
        usuario.setCorreo("john.doe@example.com");
        usuario.setContrasenia("password");
        usuario.setEdad(25);
        usuario.setTipoUsuario(TipoUsuario.ARRENDADOR);
        usuario.setComentarios("Sin comentarios");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("John");
        usuarioDTO.setApellido("Doe");
        usuarioDTO.setCorreo("john.doe@example.com");
        usuarioDTO.setContrasenia("password");
        usuarioDTO.setEdad(25);
        usuarioDTO.setTipoUsuario(TipoUsuario.ARRENDATARIO);
        usuarioDTO.setComentarios("Sin comentarios");
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(usuarioRepo.findAll()).thenReturn(usuarios);
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

        List<UsuarioDTO> result = usuarioServicio.findAll();
        assertEquals(1, result.size());
        assertEquals(usuarioDTO, result.get(0));
    }

    @Test
    void testFindById() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

        Optional<UsuarioDTO> result = usuarioServicio.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(usuarioDTO, result.get());
    }

    @Test
    void testSave_ValidUsuario() {
        when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuario);
        when(usuarioRepo.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioServicio.save(usuarioDTO);
        assertEquals(usuarioDTO, result);
    }

    @Test
    void testSave_InvalidUsuario() {
        usuarioDTO.setEdad(17); // Menor de 18 años
        usuario.setEdad(17);

        when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuario);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioServicio.save(usuarioDTO),
                "Expected save() to throw, but it didn't");
        assertEquals("La edad no puede ser menor a 18", thrown.getMessage());
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepo).deleteById(1L);

        assertDoesNotThrow(() -> usuarioServicio.deleteById(1L));
        verify(usuarioRepo, times(1)).deleteById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<UsuarioDTO> result = usuarioServicio.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_Empty() {
        when(usuarioRepo.findAll()).thenReturn(new ArrayList<>());

        List<UsuarioDTO> result = usuarioServicio.findAll();
        assertTrue(result.isEmpty());
    }
}