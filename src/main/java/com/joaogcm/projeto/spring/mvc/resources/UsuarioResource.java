package com.joaogcm.projeto.spring.mvc.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.joaogcm.projeto.spring.mvc.entities.Detalhe;
import com.joaogcm.projeto.spring.mvc.entities.Usuario;
import com.joaogcm.projeto.spring.mvc.repositories.DetalheRepository;
import com.joaogcm.projeto.spring.mvc.repositories.ProfissaoRepository;
import com.joaogcm.projeto.spring.mvc.repositories.UsuarioRepository;

import javassist.NotFoundException;
import net.sf.jasperreports.engine.JRException;

@Controller
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private DetalheRepository detalheRepository;

	@Autowired
	private RelatorioUtil relatorioUtil;

	@Autowired
	private ProfissaoRepository profissaoRepository;

	@GetMapping(value = "/cadastroUsuario")
	public ModelAndView inicio() {
		ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
		objView.addObject("usuarioObj", new Usuario());
		Iterable<Usuario> usuarios = usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		objView.addObject("usuarios", usuarios);
		objView.addObject("profissoes", profissaoRepository.findAll());

		return objView;
	}

	@GetMapping(value = "/paginacaoUsuario")
	public ModelAndView chargeUsuarioByPagination(@PageableDefault(size = 5) Pageable paginacao, ModelAndView obj,
			@RequestParam("pesquisaNome") String pesquisaNome) {
		Page<Usuario> paginacaoUsuario = usuarioRepository.findUsuarioByNamePage(pesquisaNome, paginacao);
		obj.addObject("usuarios", paginacaoUsuario);
		obj.addObject("usuarioObj", new Usuario());
		obj.addObject("pesquisaNome", pesquisaNome);
		obj.setViewName("cadastro/cadastroUsuario");

		return obj;
	}

	@PostMapping(value = "**/salvarUsuario")
	public ModelAndView insert(@Valid Usuario obj, BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
			objView.addObject("usuarios", usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			objView.addObject("usuarioObj", obj);

			List<String> mensagens = new ArrayList<String>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				mensagens.add(objectError.getDefaultMessage());
			}

			objView.addObject("mensagem", mensagens);
			objView.addObject("profissoes", profissaoRepository.findAll());

			return objView;
		}

		usuarioRepository.save(obj);

		ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
		objView.addObject("usuarios", usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		objView.addObject("usuarioObj", new Usuario());

		return objView;
	}

	@GetMapping(value = "/listarUsuario")
	public ModelAndView findAll() {
		ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
		objView.addObject("usuarios", usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		objView.addObject("usuarioObj", new Usuario());

		return objView;
	}

	@GetMapping(value = "/editarUsuario/{idUsuario}")
	public ModelAndView update(@PathVariable("idUsuario") Long idUsuario) {
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

		ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
		objView.addObject("usuarioObj", usuario.get());
		objView.addObject("profissoes", profissaoRepository.findAll());

		return objView;
	}

	@GetMapping(value = "/removerUsuario/{idUsuario}")
	public ModelAndView delete(@PathVariable("idUsuario") Long idUsuario) throws NotFoundException {
		try {
			usuarioRepository.deleteById(idUsuario);
			ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
			objView.addObject("usuarios", usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			objView.addObject("usuarioObj", new Usuario());

			return objView;
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	@PostMapping(value = "**/findUsuario")
	public ModelAndView findUsuario(@RequestParam("pesquisaNome") String pesquisaNome,
			@RequestParam("sexo") String sexo, @PageableDefault(size = 5, sort = { "nome" }) Pageable paginacao) {
		Page<Usuario> usuarios = null;

		if (sexo != null && !sexo.isEmpty()) {
			usuarios = usuarioRepository.findUsuarioByNomeAndSexoPage(pesquisaNome, sexo, paginacao);

		} else {
			usuarios = usuarioRepository.findUsuarioByNamePage(pesquisaNome, paginacao);
		}

		ModelAndView objView = new ModelAndView("cadastro/cadastroUsuario");
		objView.addObject("usuarios", usuarios);
		objView.addObject("usuarioObj", new Usuario());
		objView.addObject("pesquisaNome", pesquisaNome);

		return objView;
	}

	@GetMapping(value = "**/findUsuario")
	public void imprimePDF(@RequestParam("pesquisaNome") String pesquisaNome, @RequestParam("sexo") String sexo,
			HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

		List<Usuario> usuarios = new ArrayList<Usuario>();

		/* Busca por nome e sexo */
		if (sexo != null && !sexo.isEmpty() && pesquisaNome != null && !pesquisaNome.isEmpty()) {
			usuarios = usuarioRepository.findUsuarioByNameAndSexo(pesquisaNome, sexo);

			/* Busca por nome */
		} else if (pesquisaNome != null && !pesquisaNome.isEmpty()) {
			usuarios = usuarioRepository.findUsuarioByName(pesquisaNome);

			/* Busca por sexo */
		} else if (sexo != null && !sexo.isEmpty()) {
			usuarios = usuarioRepository.findUsuarioBySexo(sexo);

			/* Busca todos */
		} else {
			Iterable<Usuario> iterator = usuarioRepository.findAll();
			for (Usuario usuario : iterator) {
				usuarios.add(usuario);
			}
		}

		/* Chama o serviço que faz a geração do relatório */
		byte[] pdf = relatorioUtil.gerarRelatorio(usuarios, "usuario", request.getServletContext());

		/* Tamanho da resposta pro Navegador */
		response.setContentLength(pdf.length);

		/* Definir na resposta o tipo de arquivo */
		response.setContentType("application/octet-stream");

		/* Definir o cabeçalho da resposta */
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);

		/* Finaliza a resposta pro Navegador */
		response.getOutputStream().write(pdf);
	}

	@GetMapping(value = "/detalhes/{idUsuario}")
	public ModelAndView detalhes(@PathVariable("idUsuario") Long idUsuario) {
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

		ModelAndView objView = new ModelAndView("cadastro/cadastroDetalhes");
		objView.addObject("usuarioObj", usuario.get());
		objView.addObject("detalhes", detalheRepository.detalhes(idUsuario));

		return objView;
	}

	@PostMapping(value = "**/addDetalheUsuario/{idUsuario}")
	public ModelAndView detalheUsuario(Detalhe detalhe, @PathVariable("idUsuario") Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();

		if (detalhe != null && detalhe.getNumero().isEmpty() || detalhe.getTipo().isEmpty()) {

			ModelAndView objView = new ModelAndView("cadastro/cadastroDetalhes");
			objView.addObject("usuarioObj", usuario);
			objView.addObject("detalhes", detalheRepository.detalhes(idUsuario));

			List<String> mensagens = new ArrayList<String>();
			if (detalhe.getNumero().isEmpty()) {
				mensagens.add("Numero deve ser informado");
			}

			if (detalhe.getTipo().isEmpty()) {
				mensagens.add("Tipo deve ser informado");
			}

			objView.addObject("mensagem", mensagens);

			return objView;
		}

		ModelAndView objView = new ModelAndView("cadastro/cadastroDetalhes");
		detalhe.setUsuario(usuario);
		detalheRepository.save(detalhe);
		objView.addObject("usuarioObj", usuario);
		objView.addObject("detalhes", detalheRepository.detalhes(idUsuario));

		return objView;
	}

	@GetMapping(value = "/removerDetalhe/{idDetalhe}")
	public ModelAndView removerDetalhe(@PathVariable("idDetalhe") Long idDetalhe) {

		Usuario usuario = detalheRepository.findById(idDetalhe).get().getUsuario();
		detalheRepository.deleteById(idDetalhe);

		ModelAndView objView = new ModelAndView("cadastro/cadastroDetalhes");
		objView.addObject("usuarioObj", usuario);
		objView.addObject("detalhes", detalheRepository.detalhes(usuario.getId()));

		return objView;
	}
}