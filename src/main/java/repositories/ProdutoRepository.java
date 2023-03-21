package repositories;

import entities.Produto;
import factories.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final String INSERIR = String.join(
            "\n",
            "insert into produtos(nome,quantidade,preco)",
            "values(?,?,?)"
    );

    private final String LISTAR = String.join(
            "\n",
            "select *",
            "from produtos",
            "order by nome"
    );

    public void salvar(Produto produto){
        try (   Connection conexao = ConexaoFactory.getConexao();
                PreparedStatement comando = conexao.prepareStatement(INSERIR);) {

            comando.setString(1, produto.getNome());
            comando.setFloat(2, produto.getPreco());
            comando.setInt(3, produto.getQuantiade());

            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto>listar(){
        try (   Connection conexao = ConexaoFactory.getConexao();
                PreparedStatement comando = conexao.prepareStatement(LISTAR);
                ResultSet resultado = comando.executeQuery();) {

            List<Produto> produtos = new ArrayList<>();

            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setCodigo(resultado.getInt("codigo"));
                produto.setNome(resultado.getString("nome"));
                produto.setQuantiade(resultado.getInt("quantidade"));
                produto.setPreco(resultado.getInt("preco"));

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[]args){
        Produto produto = new Produto();
        produto.setNome("Arroz");
        produto.setPreco(12);
        produto.setQuantiade(1);

        ProdutoRepository produtoRepository = new ProdutoRepository();
        produtoRepository.salvar(produto);

        produtoRepository.listar();
    }
}
