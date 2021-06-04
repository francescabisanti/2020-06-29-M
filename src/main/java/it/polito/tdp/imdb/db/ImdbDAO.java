package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertici(Map <Integer, Director> idMap, int anno){
		String sql = "SELECT DISTINCT d.id, d.first_name, d.last_name "
				+ "FROM directors d, movies_directors m, movies mm "
				+ "WHERE d.id=m.director_id AND mm.id=m.movie_id AND mm.year=? ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				if(!idMap.containsKey(director.getId())) {
					idMap.put(director.getId(), director);
				}
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	
	public List <Adiacenza> getAdiacenza(Map <Integer, Director> idMap, int anno){
		String sql = "SELECT m1.director_id as d1, m2.director_id as d2, COUNT(*) AS peso "
				+ "FROM movies_directors m1, movies_directors m2, movies mm1, movies mm2, roles r1, roles r2 "
				+ "WHERE  r1.actor_id=r2.actor_id  "
				+ "AND m1.director_id> m2.director_id "
				+ "AND mm1.id=r1.movie_id AND mm2.id=r2.movie_id AND mm1.year=? AND mm2.year=? AND mm1.id=m1.movie_id AND mm2.id=m2.movie_id "
				+ "GROUP BY m1.director_id, m2.director_id "
				+ "HAVING peso>0 "
				+ "ORDER BY peso DESC";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director1 = idMap.get(res.getInt("d1"));
				Director director2 = idMap.get(res.getInt("d2"));
				
				
				Adiacenza a = new Adiacenza(director1, director2, res.getDouble("peso"));
				result.add(a);
			
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	
	
}
