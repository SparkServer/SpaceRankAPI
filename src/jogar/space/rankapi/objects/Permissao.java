package jogar.space.rankapi.objects;

import java.security.KeyStore.ProtectionParameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;



public class Permissao {
	public static boolean temPermissaoCargo(Player p, String perm) {
		try {
			Connection con = jogar.space.rankapi.database.Connection.getConnection();
			PreparedStatement st = con.prepareStatement("select * from jogador, cargo , permissao where jogador.nick_jogador = '"+p.getName()+"' and cargo.id_cargo = jogador.id_cargo AND permissao.nome_permissao = '"+perm+"' AND cargo.hierarquia_cargo >= permissao.hierarquia_permissao");
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				con.close();
				return true;
			}
			con.close();
			return false;
		}catch(SQLException e) {
			return false;
		}

	}
}
