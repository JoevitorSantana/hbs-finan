//package com.hbs.hbsfinan.config; // Ou um pacote de configuração adequado
//
//import com.hbs.hbsfinan.infra.db.Conexao;
//import com.hbs.hbsfinan.repository.implementation.ProdutosRepository;
//import com.hbs.hbsfinan.repository.interfaces.IProdutosRepository; // Importe a interface
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppConfig { // Você pode nomear como preferir, ex: RepositoryConfig

//    @Bean
//    public IProdutosRepository produtosRepository() {
//        // Obtenha a instância de Conexao como você faz em outros lugares
//        Conexao conexao = Conexao.getInstance();
//        if (conexao == null) {
//            // Lançar uma exceção mais apropriada se a conexão não puder ser obtida
//            throw new IllegalStateException("Não foi possível obter a instância de Conexao.");
//        }
//        return new ProdutosRepository(conexao);
//    }

    // Se você tiver outros repositórios que seguem o mesmo padrão (construtor com Conexao),
    // você pode definir beans para eles aqui também.
    // Exemplo:
    // @Bean
    // public IFuncionarioRepository funcionarioRepository() {
    //     Conexao conexao = Conexao.getInstance();
    //     return new FuncionarioRepository(conexao);
    // }
    //
    // @Bean
    // public IMovimentacaoEstoqueRepository movimentacaoEstoqueRepository() {
    //     Conexao conexao = Conexao.getInstance();
    //     return new MovimentacaoEstoqueRepository(conexao);
    // }
//}