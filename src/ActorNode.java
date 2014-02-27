import java.util.ArrayList;


public class ActorNode {
	
		private String name;
		private ArrayList<String> movies = new ArrayList<String>();
		private int distance;
		private ActorNode prev = null;

		public ActorNode(String name) {
			this.name = name;
		}
		   
		public String getName() {
			return this.name;
		}
		
		public ArrayList<String> getMovies(){
		    return this.movies;
		}
		//gets number of collaborations
		public int getCollaborations() {
			return movies.size();
		}	
		
		public String toString() {
			return name + ": " + movies.toString();
		}
		
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		public int getDistance() {
			return this.distance;
		}
		
		public void setPrev(ActorNode node) {
			this.prev = node;
		}
		public ActorNode getPrev() {
			return this.prev;
		}
		
		
}
