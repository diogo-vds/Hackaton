package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.application.port.in.VacinaUseCase;
import br.com.fiap.msvacinas.domain.model.Vacina;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {
    private final VacinaUseCase useCase;

    public VacinaController(VacinaUseCase useCase) { this.useCase = useCase; }

    @PostMapping
    public ResponseEntity<VacinaResponse> criar(@Valid @RequestBody VacinaRequest request) {
        var criada = useCase.criar(toDomain(request));
        return ResponseEntity.created(URI.create("/vacinas/" + criada.id())).body(VacinaResponse.from(criada));
    }

    @GetMapping
    public List<VacinaResponse> listar() {
        return useCase.listar().stream().map(VacinaResponse::from).toList();
    }

    @GetMapping("/{id}")
    public VacinaResponse buscar(@PathVariable UUID id) { return VacinaResponse.from(useCase.buscarPorId(id)); }

    @PutMapping("/{id}")
    public VacinaResponse atualizar(@PathVariable UUID id, @Valid @RequestBody VacinaRequest request) {
        return VacinaResponse.from(useCase.atualizar(id, toDomain(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        useCase.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Vacina toDomain(VacinaRequest request) {
        return new Vacina(null, request.nome(), request.descricao(), request.idadeAlvoEmMeses());
    }
}
