import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class KBNumber {

	private Map<String, List<ActorNode>> graph;
	Set<String> keySet;
	private ActorNode baconNode;
	
	/**
	 * Constructor
	 * 
	 * @param fileName The name of the file (e.g. "movies.txt")
	 */
	public KBNumber(String fileName) {
		graph = new HashMap<String, List<ActorNode>>();
		keySet = new HashSet<String>();
		try {
			makeGraph(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void makeGraph(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String nextLine;
		String movie;
		nextLine = in.readLine();
		//Parse the file
		while(nextLine != null) {
			String[] parsed = nextLine.split("/");
			movie = parsed[0];
			for(int i=1; i<parsed.length; i++) {
				keySet.add(parsed[i]);
			}
			addActors(movie);
			keySet.clear();
			nextLine = in.readLine();
		}
		in.close();
	}
	
	//adds actors to the graph
	private void addActors(String movie) {		
		for(String key: keySet) {
			String mainKey = key;
	//		keySet.remove(key);

			// if mainkey is already in the graph
			if(graph.get(mainKey)!=null) {			
				for(String left: keySet) {
					if(!left.equals(mainKey)) {
						Boolean contains = false;
						List<ActorNode> list = graph.get(mainKey);
						if(!list.isEmpty()){
							for(ActorNode a: list) {
								if(a.getName().equals(left)) {
									a.getMovies().add(movie);
									contains = true;
									break;
								} 
							}
							if(!contains) {
								ActorNode newNode = new ActorNode(left);
								newNode.getMovies().add(movie);
								graph.get(mainKey).add(newNode);
							}
						}
					}
				}
			}
			// if mainKey is not in the graph
			if(graph.get(mainKey) == null) {
				List<ActorNode> newList = new ArrayList<ActorNode>();
				for(String left: keySet){
					if(!left.equals(mainKey)) {
						ActorNode node = new ActorNode(left);
						node.getMovies().add(movie);
						newList.add(node);
					}
				}
				graph.put(mainKey, newList);
			}
		}
	}
	
	
	/**
	 * Find the pair(s) of actors that cooperate in most movies, return the
	 * number of movies they have collaborated in
	 * 
	 * @return the number of movies they have collaborated in
	 */
	public int mostCollaboration() {
		int max = 0;

		for(List<ActorNode> list: graph.values()) {
			for(ActorNode node: list) {
				if (node.getCollaborations() > max) {
					max = node.getCollaborations();
				}
			}
		}
		return max;
	}

	/**
	 * Given the name of an actor, output a list of all other actors that he/she
	 * has been in a movie with
	 * 
	 * @param actor
	 *            The name of the actor
	 * @return A list of UNIQUE actor names in any order that actor has been in
	 *         a movie with. Do not include the actor himself. In the case that
	 *         an actor is in all movies alone, return an empty list.
	 * @throws IllegalArgumentException
	 *             If the name of the actor is null or not contained in the
	 *             graph
	 */
	public List<String> findCostars(String actor) throws IllegalArgumentException {
		if(!graph.containsKey(actor) || actor == null) {
			throw new IllegalArgumentException();
		}
		List<ActorNode> list = graph.get(actor);
		List<String> answer = new ArrayList<String>();
		for(ActorNode node: list){
			answer.add(node.getName());
		}
		return answer;
	}

	/**
	 * Implement a BFS on your graph to calculate the Kevin Bacon number of a
	 * given actor
	 * 
	 * @param actor
	 *            The name of the actor
	 * @return If actor is bacon, return 0; if bacon cannot be found from graph,
	 *         return -1; else return bacon number
	 * @throws IllegalArgumentException
	 *             If the name of the actor is null or not contained in the
	 *             graph
	 */
	public int findBaconNumber(String actor) throws IllegalArgumentException {
		if(actor.equals("Bacon, Kevin")) {
			return 0;
		}
		if(!graph.containsKey(actor) || actor == null) {
			throw new IllegalArgumentException();
		}
		Queue<ActorNode> queue = new LinkedList<ActorNode>();
		ActorNode mainActor = new ActorNode(actor);
		mainActor.setDistance(0);
		queue.add(mainActor);
		Set<String> visited = new HashSet<String>();
		visited.add(actor);
		while(!queue.isEmpty()) {
			ActorNode last = queue.remove();
			String test = last.getName();
			if(test.equals("Bacon, Kevin"))  {
				baconNode = last.getPrev();
				return last.getDistance();
			}
			for(ActorNode node: graph.get(test)) {
				node.setDistance(last.getDistance() + 1);
				node.setPrev(last);
				if(!visited.contains(node.getName())) {
					queue.add(node);
					visited.add(node.getName());
				}
			}
		}
		return -1;
	}
	
	/**
	 * Given the name of an actor, return a list of strings representing the
	 * path along your BFS from the given actor to Kevin Bacon, starting from
	 * the actor and following an actor->movie->actor->movie pattern.
	 * 
	 * If two actors have appeared in multiple movies together, it does not
	 * matter which of those movies links them in the list.
	 * 
	 * If there are multiple paths in the BFS from a given actor to Kevin Bacon,
	 * it does not matter which path is returned as long as it is accurate and 
	 * there is no shorter path (i.e. your path provides the correct Bacon number).
	 * 
	 * If the actor is Kevin Bacon, the list should contain one string (Bacon, Kevin) only.
	 * 
	 * If there is no path to Kevin Bacon from the given actor, return null.
	 * 
	 * @param actor
	 *            The name of the actor
	 * @return A list of strings showing the path from actor to Kevin Bacon as
	 *         strings alternating between actor and movie, starting from the
	 *         original actor and ending at Bacon.
	 * 
	 *         example List (NOT A TEST CASE) for actor = "Damon, Matt": (Bacon
	 *         number = 2)
	 * 
	 *         Damon, Matt 
	 *         The Informant! (2009) 
	 *         Pistor, Ludger 
	 *         X-Men: First Class (2011) 
	 *         Bacon, Kevin
	 * 
	 * @throws IllegalArgumentException
	 *             If the name of the actor is null or not contained in the
	 *             graph
	 */
	public List<String> findBaconPath (String actor) throws IllegalArgumentException {
		if(actor==null || !graph.containsKey(actor)) {
			throw new IllegalArgumentException();
		}
		int a =	findBaconNumber(actor);
		if(a == -1) {
			return null;
		}
		List<String> actors = new ArrayList<String>();
		ActorNode node = baconNode;
		actors.add("Bacon, Kevin");
		actors.add(getMovie(node, new ActorNode("Bacon, Kevin")));
		while(node.getPrev()!=null) {
			actors.add(node.getName());
			actors.add(getMovie(node, node.getPrev()));
			node = node.getPrev();
		}
		actors.add(node.getName());
		return reverse(actors);
	}
	
	private String getMovie(ActorNode node1, ActorNode node2) {
		List<ActorNode> list = graph.get(node1.getName());
		for(ActorNode node: list) {
			if (node.getName().equals(node2.getName())) {
				String movie = node.getMovies().get(0);
				return movie;
			}
		}
		return null;
	}
	
	private List<String> reverse(List<String> list) {
	   List<String> reversed = new ArrayList<String>();
	   for (int i = list.size() - 1 ; i >= 0 ; i--){
	       String next = list.get(i) ;
	       reversed.add(next) ;
	   }
	   return reversed ;
	}
	

	
}
