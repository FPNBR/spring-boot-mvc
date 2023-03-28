package br.com.fpnbr.springbootmvc;

import br.com.fpnbr.springbootmvc.models.Usuario;
import br.com.fpnbr.springbootmvc.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringBootMvcApplicationTests {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	public void salvar() {
		Usuario usuario = new Usuario();
		usuario.setLogin("teste3");
		usuario.setSenha(bCryptPasswordEncoder.encode("teste3"));
		usuarioRepository.save(usuario);
	}

	@Test
	public void deletar() {
		usuarioRepository.deleteById(1L);
	}
}
